package net.wholesome.wholesomestart;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

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

class NotificationCreator {
    private static String WHOLESOME_TOP_DAILY_URL = "https://nm.reddit.com/r/wholesomememes/top.json?sort=top&t=day";
    private static String REDDIT_BASE_URL = "https://www.reddit.com";
    private static int NOTIFICATION_ID = 1;
    private static int PENDING_INTENT_ID = 0;

    public static void createNewNotification(final Context context) {
        NetworkHelpers.get(WHOLESOME_TOP_DAILY_URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                GeneralHelpers.Log("Failed to get /r/wholesomememes posts: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject jsonResponse = new JSONObject(response.body().string());
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
        JSONObject post = NotificationCreator.choosePost(posts);

        try {
            String postTitle = post.getString("title");
            String url = post.getString("permalink");
            String fullUrl = REDDIT_BASE_URL + url;
            String imgUrl = post.getString("thumbnail");

            GeneralHelpers.Log("Chosen post: " + postTitle);
            NotificationCreator.getThumbnail(context, postTitle, fullUrl, imgUrl);
        } catch (JSONException e) {
            GeneralHelpers.Log("Failed to get title of post: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void getThumbnail(final Context context, final String title, final String postUrl, String imgUrl) {
        NetworkHelpers.get(imgUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                GeneralHelpers.Log("Failed to get thumbnail for notification: " + e.getMessage());
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

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_stat_name)
                        .setLargeIcon(image)
                        .setContentTitle("Here is today's post!")
                        .setContentText(title)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    @Nullable
    private static JSONObject choosePost(JSONArray posts) {
        try {
            return posts.getJSONObject(0).getJSONObject("data");
        } catch (JSONException e) {
            GeneralHelpers.Log("Getting single post from posts array failed: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}
