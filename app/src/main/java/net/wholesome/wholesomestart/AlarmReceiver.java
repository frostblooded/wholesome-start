package net.wholesome.wholesomestart;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import net.wholesome.wholesomestart.helpers.GeneralHelpers;

import org.json.JSONArray;

import okhttp3.OkHttpClient;

public class AlarmReceiver extends BroadcastReceiver {
    private static OkHttpClient client = new OkHttpClient();

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCreator.createNewNotification(context);
    }
}