package net.wholesome.wholesomestart.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

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

    public static void Log(String message) {
        Log.i(LOG_TAG, message);
    }
}
