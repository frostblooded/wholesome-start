package net.wholesome.wholesomestart;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import net.wholesome.wholesomestart.helpers.GeneralHelpers;
import net.wholesome.wholesomestart.receivers.AlarmReceiver;

import java.util.Calendar;
import java.util.Random;

public class AlarmCreator {
    private static int MINUTES_BEFORE_RETRY = 1;
    public static String RETRYING_KEY = "retrying";

    private static Random random = new Random();

    public static void startAlarm(Context context) {
        startAlarm(context, false);
    }

    // The retrying parameter shows if the alarm is being tried again.
    // This may happen if the alarm has already triggered, but there was no internet connection.
    // In that case, the alarm is going to be tried again after a short delay.
    public static void startAlarm(Context context, boolean retrying) {
        GeneralHelpers.saveHasBeenOpened(context);

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);

        Bundle bundle = new Bundle();
        bundle.putBoolean(RETRYING_KEY, retrying);
        intent.putExtras(bundle);

        // Use random request code so that pending intent is
        // recognised as unique and extras are passed correctly
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = getTimeForAlarm(context, retrying);
        GeneralHelpers.Log("Setting alarm for " + calendar.getTime());
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private static Calendar getTimeForAlarm(Context context, boolean retrying) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        if(retrying) {
            calendar.add(Calendar.MINUTE, MINUTES_BEFORE_RETRY);
            GeneralHelpers.Log("Waiting " + MINUTES_BEFORE_RETRY + " minutes before retrying.");
            return calendar;
        }

        int timeHour = GeneralHelpers.getTimeHour(context);
        int timeMinute = GeneralHelpers.getTimeMinute(context);

        calendar.set(Calendar.HOUR_OF_DAY, timeHour);
        calendar.set(Calendar.MINUTE, timeMinute);
        calendar.set(Calendar.SECOND, 0);

        // If this hour has passed for today, make it next day
        if(calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            GeneralHelpers.Log("Time has already passed. Making it next day.");
        }

        return calendar;
    }

}
