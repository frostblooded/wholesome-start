package net.wholesome.wholesomestart.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import net.wholesome.wholesomestart.AlarmCreator;
import net.wholesome.wholesomestart.helpers.GeneralHelpers;

public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        GeneralHelpers.Log("Starting alarm on device boot.");
        AlarmCreator.startAlarm(context, false);
    }
}
