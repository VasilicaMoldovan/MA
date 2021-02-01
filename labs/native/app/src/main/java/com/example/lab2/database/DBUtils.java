package com.example.lab2.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBUtils extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "flowers.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Contract.FeedEntry.TABLE_NAME + " (" +
                    Contract.FeedEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    Contract.FeedEntry.COLUMN_NAME_Name + " TEXT," +
                    Contract.FeedEntry.COLUMN_NAME_Type + " TEXT," +
                    Contract.FeedEntry.COLUMN_NAME_Color + " TEXT," +
                    Contract.FeedEntry.COLUMN_NAME_Number + " TEXT," +
                    Contract.FeedEntry.COLUMN_NAME_Price + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Contract.FeedEntry.TABLE_NAME;

    public DBUtils(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}