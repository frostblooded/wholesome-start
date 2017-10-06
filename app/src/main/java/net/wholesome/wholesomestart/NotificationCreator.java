package net.wholesome.wholesomestart;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import net.wholesome.wholesomestart.database.DatabaseConnection;
import net.wholesome.wholesomestart.helpers.GeneralHelpers;
import net.wholesome.wholesomestart.helpers.NetworkHelpers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class NotificationCreator {
    private static String WHOLESOME_TOP_DAILY_URL = "https://nm.reddit.com/r/wholesomememes/top.json?sort=top&t=day";
    private static String REDDIT_BASE_URL = "https://www.reddit.com";
    private static int NOTIFICATION_ID = 1;
    private static int PENDING_INTENT_ID = 0;

    public static void createNewNotification(final Context context) {
        NetworkHelpers.get(WHOLESOME_TOP_DAILY_URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                GeneralHelpers.Log("Failed getting /r/wholesomememes posts: " + e.getMessage());
                AlarmCreator.startAlarm(context, true);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String responseString = response.body().string();
                    JSONObject jsonResponse = new JSONObject(responseString);
                    JSONObject responseData = jsonResponse.getJSONObject("data");
                    NotificationCreator.prepareForNotification(context, responseData.getJSONArray("children"));
                } catch (JSONException e) {
                    GeneralHelpers.Log("JSON parsing the /r/wholesomememes response failed: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    private static void prepareForNotification(Context context, JSONArray posts) {
        JSONObject post = NotificationCreator.choosePost(context, posts);
        DatabaseConnection.getConnection(context).savePreviousPost(post);

        try {
            GeneralHelpers.Log("Chosen post: " + post.toString(2));
        } catch (JSONException e) {
            GeneralHelpers.Log("Error logging chosen post.");
            e.printStackTrace();
        }

        try {
            String postTitle = post.getString("title");
            String url = post.getString("permalink");
            String fullUrl = REDDIT_BASE_URL + url;
            String imgUrl = post.getString("thumbnail");

            NotificationCreator.getThumbnail(context, postTitle, fullUrl, imgUrl);
        } catch (JSONException e) {
            GeneralHelpers.Log("Failed to parse chosen post: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void getThumbnail(final Context context, final String title, final String postUrl, String imgUrl) {
        NetworkHelpers.get(imgUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                GeneralHelpers.Log("Failed getting thumbnail: " + e.getMessage());
                AlarmCreator.startAlarm(context, true);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream imageStream = response.body().byteStream();
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                NotificationCreator.makeNotification(context, title, postUrl, bitmap);
            }
        });
    }

    private static void makeNotification(Context context, String title, String url, Bitmap image) {
        //Set intent for when notification is clicked
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, PENDING_INTENT_ID,
                intent, 0);
        String contentTitle = NotificationCreator.generateContentTitle(context);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_stat_name)
                        .setLargeIcon(image)
                        .setContentTitle(contentTitle)
                        .setContentText(title)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    private static String generateContentTitle(Context context) {
        String contentTitle = "Here is today's post!";
        String name = GeneralHelpers.getName(context);

        if(name != null) {
            contentTitle = "Hello, " + name + "! " + contentTitle;
        }

        return contentTitle;
    }

    @Nullable
    private static JSONObject choosePost(Context context, JSONArray posts) {
        try {
            GeneralHelpers.Log("Posts count: " + posts.length());
            DatabaseConnection db = DatabaseConnection.getConnection(context);

            for(int i = 0; i < posts.length(); i++) {
                JSONObject currentPost = getPostData(posts, i);

                if(!db.postIsPrevious(currentPost)) {
                    GeneralHelpers.Log("Post " + currentPost.getString("id") + " is NOT previous");
                    return currentPost;
                }

                GeneralHelpers.Log("Post " + currentPost.getString("id") + " is previous");
            }

            GeneralHelpers.Log("All posts are previous. Falling back to first post.");
            return getPostData(posts, 0);
        } catch (JSONException e) {
            GeneralHelpers.Log("Getting single post from posts array failed: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    private static JSONObject getPostData(JSONArray posts, int index) throws JSONException {
        return posts.getJSONObject(index).getJSONObject("data");
    }
}
