package net.wholesome.wholesomestart.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.wholesome.wholesomestart.helpers.GeneralHelpers;

import org.json.JSONException;
import org.json.JSONObject;

public class DatabaseConnection extends SQLiteOpenHelper {
    private static int DATABASE_VERSION = 1;
    private static String DATABASE_NAME = "WholesomeStart.db";

    private static String TABLE_NAME = "previous_posts";
    private static String COLUMN_POST_EXTERNAL_ID = "external_id";

    private static String SQL_CREATE_POSTS =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    "id INTEGER PRIMARY KEY, " +
                    COLUMN_POST_EXTERNAL_ID + " char(16))";

    private static String SQL_DELETE_POSTS =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    private static DatabaseConnection dbConnection = null;

    private DatabaseConnection(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_POSTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_POSTS);
        onCreate(sqLiteDatabase);
    }

    public static DatabaseConnection getConnection(Context context) {
        if(dbConnection == null)
            dbConnection = new DatabaseConnection(context);

        return dbConnection;
    }

    public void savePreviousPost(JSONObject post) {
        try {
            ContentValues values = new ContentValues();
            String postId = post.getString("id");
            values.put(COLUMN_POST_EXTERNAL_ID, postId);
            getWritableDatabase().insert(TABLE_NAME, null, values);
            GeneralHelpers.Log("Saved previous post " + postId);
        } catch (JSONException e) {
            GeneralHelpers.Log("Failed getting previous post id to save to DB: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
