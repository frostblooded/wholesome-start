package net.wholesome.wholesomestart.helpers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import net.wholesome.wholesomestart.AlarmReceiver;

import java.util.Calendar;

public class GeneralHelpers {
    private static String SHARED_PREFS_NAME = "wholesome_start";
    private static String USER_NAME_SHARED_PREF = "userName";
    private static String TIME_HOUR_SHARED_PREF = "timeHour";
    private static String TIME_MINUTE_SHARED_PREF = "timeMinute";

    private static String LOG_TAG = "WholesomeStart";

    public static void saveTime(Context context, int hour, int minute) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREFS_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putInt(TIME_HOUR_SHARED_PREF, hour);
        editor.putInt(TIME_MINUTE_SHARED_PREF, minute);
        editor.apply();
    }

    public static void saveName(Context context, String name) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREFS_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString(USER_NAME_SHARED_PREF, name);
        editor.apply();
    }

    public static String getName(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREFS_NAME,
                Context.MODE_PRIVATE);
        return sp.getString(USER_NAME_SHARED_PREF, null);
    }

    public static int getTimeHour(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREFS_NAME,
                Context.MODE_PRIVATE);
        return sp.getInt(TIME_HOUR_SHARED_PREF, 0);
    }

    public static int getTimeMinute(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREFS_NAME,
                Context.MODE_PRIVATE);
        return sp.getInt(TIME_MINUTE_SHARED_PREF, 0);
    }

    public static void setAlarm(Context context, int timeHour, int timeMinute) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        Calendar calendar = getTimeForAlarm(timeHour, timeMinute);
        GeneralHelpers.Log("Setting alarm for " + calendar.toString());
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private static Calendar getTimeForAlarm(int timeHour, int timeMinute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
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

    public static void Log(String message) {
        Log.i(LOG_TAG, message);
    }
}
