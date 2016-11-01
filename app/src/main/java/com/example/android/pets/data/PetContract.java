package com.example.android.pets.data;

import android.provider.BaseColumns;

/**
 * Defines the contract for the Pets database, holding the constants for the table names and
 * their column names.
 */

public final class PetContract {

    /**
     * This class simply defines the constants for the database, so PetContract objects should not
     * be instantiated. Thus it has a private access modifier.
     */
    private PetContract() {}

    /* Defines the 'pets' table and its column names, as well as constants for the gender column. */
    public class PetEntry implements BaseColumns {
        // The name of the table
        public static final String TABLE_NAME = "pets";

        // The column names for this table
        public static final String _ID = BaseColumns._ID; // Not sure this line is necessary?
        public static final String COLUMN_PET_NAME = "name";
        public static final String COLUMN_PET_BREED = "breed";
        public static final String COLUMN_PET_GENDER = "gender";
        public static final String COLUMN_PET_WEIGHT = "weight";

        // Constants for the gender column
        public static final int GENDER_UNKNOWN = 0;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;
    }
}
