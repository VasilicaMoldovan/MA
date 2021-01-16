package com.example.tryexam.database;

import android.provider.BaseColumns;

public final class Contract {
    private Contract() {}

    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "recipeTable";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_Name = "name";
        public static final String COLUMN_NAME_Details = "details";
        public static final String COLUMN_NAME_Time = "time";
        public static final String COLUMN_NAME_Type = "type";
        public static final String COLUMN_NAME_Rating = "rating";
    }

}