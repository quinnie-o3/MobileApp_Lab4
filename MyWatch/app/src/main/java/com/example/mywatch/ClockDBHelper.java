package com.example.mywatch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ClockDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ClockDB";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_LAPS = "Laps";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_LAP_TIME = "lap_time";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_LAPS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_LAP_TIME + " TEXT, " +
                    COLUMN_TIMESTAMP + " INTEGER" +
                    ");";

    public ClockDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LAPS);
        onCreate(db);
    }

    public void insertLap(String lapTime, long timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LAP_TIME, lapTime);
        values.put(COLUMN_TIMESTAMP, timestamp);
        db.insert(TABLE_LAPS, null, values);
        db.close();
    }

    public Cursor getAllLaps() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_LAPS, null, null, null, null, null, COLUMN_TIMESTAMP + " DESC");
    }
}
