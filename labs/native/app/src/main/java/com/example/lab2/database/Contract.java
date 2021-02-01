package com.example.lab2.database;

import android.provider.BaseColumns;

public final class Contract {
    private Contract() {}

    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "flowersTable";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_Name = "name";
        public static final String COLUMN_NAME_Type = "type";
        public static final String COLUMN_NAME_Color = "color";
        public static final String COLUMN_NAME_Number = "number";
        public static final String COLUMN_NAME_Price = "price";
    }

}
