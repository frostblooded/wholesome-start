package net.wholesome.wholesomestart;

import android.app.NotificationManager;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import net.wholesome.wholesomestart.helpers.GeneralHelpers;
import net.wholesome.wholesomestart.helpers.NetworkHelpers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

class NotificationCreator {
    private static String WHOLESOME_TOP_DAILY_URL = "https://nm.reddit.com/r/wholesomememes/top.json?sort=top&t=day";
    private static int NOTIFICATION_ID = 1;

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
                    NotificationCreator.makeNotification(context, responseData.getJSONArray("children"));
                } catch (JSONException e) {
                    GeneralHelpers.Log("JSON parsing the /r/wholesomememes response failed: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    private static void makeNotification(Context context, JSONArray posts) {
        JSONObject post = NotificationCreator.choosePost(posts);

        try {
            GeneralHelpers.Log("Chosen post: " + post.getString("title"));
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("Here is today's post!")
                            .setContentText(post.getString("title"));

            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
        } catch (JSONException e) {
            GeneralHelpers.Log("Failed to get title of post: " + e.getMessage());
            e.printStackTrace();
        }
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
