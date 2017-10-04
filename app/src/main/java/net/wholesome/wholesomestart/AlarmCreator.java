package net.wholesome.wholesomestart;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import net.wholesome.wholesomestart.helpers.GeneralHelpers;

import java.util.Calendar;

public class AlarmCreator {
    private static int MINUTES_BEFORE_RETRY = 5;
    public static String RETRYING_KEY = "retrying";

    public static void startAlarm(Context context, boolean retrying) {
        int timeHour = GeneralHelpers.getTimeHour(context);
        int timeMinute = GeneralHelpers.getTimeMinute(context);

        startAlarm(context, timeHour, timeMinute, retrying);
    }

    public static void startAlarm(Context context, int timeHour, int timeMinute) {
        startAlarm(context, timeHour, timeMinute, false);
    }

    // The retrying parameter shows if the alarm is being tried again.
    // This may happen if the alarm has already triggered, but there was no internet connection.
    // In that case, the alarm is going to be tried again after a short delay.
    public static void startAlarm(Context context, int timeHour, int timeMinute, boolean retrying) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);

        Bundle bundle = new Bundle();
        bundle.putBoolean(RETRYING_KEY, retrying);
        intent.putExtras(bundle);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        Calendar calendar = getTimeForAlarm(timeHour, timeMinute, retrying);
        GeneralHelpers.Log("Setting alarm for " + calendar.toString());
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private static Calendar getTimeForAlarm(int timeHour, int timeMinute, boolean retrying) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        if(retrying) {
            calendar.add(Calendar.MINUTE, MINUTES_BEFORE_RETRY);
            GeneralHelpers.Log("Waiting " + MINUTES_BEFORE_RETRY + " minutes before retrying.");
            return calendar;
        }

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
