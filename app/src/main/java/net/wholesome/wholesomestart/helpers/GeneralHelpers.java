package net.wholesome.wholesomestart.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Random;

public class GeneralHelpers {
    private static String SHARED_PREFS_NAME = "wholesome_start";
    private static String TIME_HOUR_SHARED_PREF = "timeHour";
    private static String TIME_MINUTE_SHARED_PREF = "timeMinute";

    private static int DEFAULT_TIME_HOUR = 8;
    private static int DEFAULT_TIME_MINUTE = 0;

    private static String[] GREETINGS = {
            "Have a great day!",
            "ʕʘ‿ʘʔ You're amazing!",
            "You're the best friend!",
            "Be the best you!",
            "You go, gurl!",
            "Love today!",
            "(ﾟヮﾟ) Appreciate the day!",
            "Be a great person!",
            "\u2764 I love you! \u2764", // <3 <3 <3
            "Your friends love you!",
            "Go love yourself!",
            "I believe in you!",
            "◕ ◡ ◕ You can do it!",
            "Never give up!",
            "(づ｡◕‿‿◕｡)づ *virtual hug*",
            "Believe in yourself!",
            "ಠ_ರೃ You can do anything!",
            "♥╭╮♥ You sexy beast!",
            "Be strong!",
            "Slash - Promise",
            "(｡･_･｡) You look cute today.",
            "Do your best!"
    };

    private static String LOG_TAG = "WholesomeStart";

    public static String getRandomGreeting() {
        Random random = new Random();
        return GREETINGS[random.nextInt(GREETINGS.length)];
    }

    public static void saveTime(Context context, int hour, int minute) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREFS_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putInt(TIME_HOUR_SHARED_PREF, hour);
        editor.putInt(TIME_MINUTE_SHARED_PREF, minute);
        editor.apply();
    }

    public static int getTimeHour(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREFS_NAME,
                Context.MODE_PRIVATE);
        return sp.getInt(TIME_HOUR_SHARED_PREF, DEFAULT_TIME_HOUR);
    }

    public static int getTimeMinute(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREFS_NAME,
                Context.MODE_PRIVATE);
        return sp.getInt(TIME_MINUTE_SHARED_PREF, DEFAULT_TIME_MINUTE);
    }

    public static void Log(String message) {
        Log.i(LOG_TAG, message);
    }
}
