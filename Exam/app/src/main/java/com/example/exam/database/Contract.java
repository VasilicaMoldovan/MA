package com.example.exam.database;

import android.provider.BaseColumns;

public final class Contract {
    private Contract() {}

    public static class FeedEntry implements BaseColumns {
        //TODO: if you edit the columns, change the table as well(in the begining)

        public static final String TABLE_NAME = "examTable";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_Name = "name";
        public static final String COLUMN_NAME_Level = "level";
        public static final String COLUMN_NAME_Status = "status";
        public static final String COLUMN_NAME_From1 = "from1";
        public static final String COLUMN_NAME_To = "to1";
    }

}

