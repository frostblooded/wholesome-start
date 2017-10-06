package net.wholesome.wholesomestart.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import net.wholesome.wholesomestart.AlarmCreator;
import net.wholesome.wholesomestart.NotificationCreator;
import net.wholesome.wholesomestart.helpers.GeneralHelpers;

import okhttp3.OkHttpClient;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCreator.createNewNotification(context);

        boolean alarmIsRetrying = intent.getBooleanExtra(AlarmCreator.RETRYING_KEY, false);
        GeneralHelpers.Log("Alarm is retrying: " + alarmIsRetrying);

        // If alarm was retrying, set the next one for the right time
        if(alarmIsRetrying) {
            AlarmCreator.startAlarm(context);
        }
    }
}
