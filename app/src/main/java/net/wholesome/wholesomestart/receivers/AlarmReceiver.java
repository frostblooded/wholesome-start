package net.wholesome.wholesomestart.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import net.wholesome.wholesomestart.AlarmCreator;
import net.wholesome.wholesomestart.NotificationCreator;

import okhttp3.OkHttpClient;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCreator.createNewNotification(context);

        // If alarm was retrying, set the next one for the right time
        if(intent.getBooleanExtra(AlarmCreator.RETRYING_KEY, false)) {
            AlarmCreator.startAlarm(context, false);
        }
    }
}
