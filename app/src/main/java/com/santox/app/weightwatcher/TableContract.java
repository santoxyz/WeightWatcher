package com.santox.app.weightwatcher;

import android.provider.BaseColumns;

/**
 * Created by s on 28/09/14.
 */
public final class TableContract {

        // To prevent someone from accidentally instantiating the contract class,
        // give it an empty constructor.
        public TableContract() {}

        /* Inner class that defines the table contents */
        public static abstract class WeightEntry implements BaseColumns {
            public static final String TABLE_NAME = "weight";

            public static final String DATE = "date";
            public static final String G = "g";
        }


    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + WeightEntry.TABLE_NAME + " (" +
                    WeightEntry._ID + " INTEGER PRIMARY KEY," +
                    WeightEntry.DATE + INT_TYPE + COMMA_SEP +
                    WeightEntry.G + INT_TYPE +
            " )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + WeightEntry.TABLE_NAME;
}
