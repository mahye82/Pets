package com.example.android.pets.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.android.pets.data.PetContract.PetEntry;

/**
 * Database helper for Pets app. Manages database creation and version management.
 */
public class PetDbHelper extends SQLiteOpenHelper {
    private static final String LOG_TAG = PetDbHelper.class.getName();

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;
    /** Name of the database file */
    private static final String DATABASE_NAME = "shelter.db";

    // Constants to construct SQLite statements
    private static final String INTEGER = " INTEGER";
    private static final String TEXT = " TEXT";
    private static final String NOT_NULL = " NOT NULL";
    private static final String PRIMARY_KEY_AUTOINCREMENT = " PRIMARY KEY AUTOINCREMENT";
    private static final String DEFAULT = " DEFAULT";
    private static final String DEFAULT_VALUE = " 0";
    private static final String COMMA_SEP = ", ";

    // Constants for SQLite statements themselves;
    private static final String SQL_CREATE_PETS_TABLE =
            "CREATE TABLE " + PetEntry.TABLE_NAME  + " (" +
                    PetEntry._ID + INTEGER + PRIMARY_KEY_AUTOINCREMENT + COMMA_SEP +
                    PetEntry.COLUMN_PET_NAME + TEXT + NOT_NULL + COMMA_SEP +
                    PetEntry.COLUMN_PET_BREED + TEXT + COMMA_SEP +
                    PetEntry.COLUMN_PET_GENDER + INTEGER + NOT_NULL + COMMA_SEP +
                    PetEntry.COLUMN_PET_WEIGHT + INTEGER + NOT_NULL + DEFAULT + DEFAULT_VALUE +
                    ");";

    /**
     * Constructs a new instance of {@link PetDbHelper}.
     *
     * @param context of the app
     */
    public PetDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(LOG_TAG, "SQL STATEMENT TO CREATE PETS TABLE: " + SQL_CREATE_PETS_TABLE);
        db.execSQL(SQL_CREATE_PETS_TABLE);
    }

    /**
     * Called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here
    }
}
