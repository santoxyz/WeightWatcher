package com.santox.app.weightwatcher;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.santox.app.weightwatcher.TableContract;

/**
 * Created by s on 28/09/14.
 */


public class TableDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "WeightWatcher.db";
    public static final String SORT_ASC = "ASC";
    public static final String SORT_DESC = "DESC";


    public TableDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TableContract.SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(TableContract.SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void add(long date, long gr){

// Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(TableContract.WeightEntry.DATE, date);
        values.put(TableContract.WeightEntry.G, gr);


// Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                TableContract.WeightEntry.TABLE_NAME,
                null,
                values);
 //       db.close();

    }

    public Cursor getAll(String sort){
        SQLiteDatabase db = getReadableDatabase();

        String sortOrder =
                TableContract.WeightEntry.DATE + " " + sort;

        Cursor c = db.query(
                TableContract.WeightEntry.TABLE_NAME,
                null, //new String[] { TableContract.WeightEntry.DATE, TableContract.WeightEntry.G },
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
//        db.close();
        return  c;
    }

    public void wipe(){
        SQLiteDatabase db = getWritableDatabase();
        onUpgrade(db,1,1);
    }
}