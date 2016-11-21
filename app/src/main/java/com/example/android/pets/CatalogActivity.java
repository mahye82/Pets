package com.example.android.pets;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.android.pets.data.PetContract.PetEntry;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity {

    // The name of this class, for the purposes of logs
    private static final String LOG_TAG = CatalogActivity.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // Find the ListView which will be populated with the pet data
        ListView petListView = (ListView) findViewById(R.id.list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        petListView.setEmptyView(emptyView);
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */
    private void displayDatabaseInfo() {
        // Define the projection (i.e. the column names you want returned in the Cursor object
        String[] projection = {
                PetEntry._ID,
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_BREED,
                PetEntry.COLUMN_PET_GENDER,
                PetEntry.COLUMN_PET_WEIGHT
        };

        // Perform a query on the ContentProvider using the ContentResolver.
        // Use the PetEntry.CONTENT_URI to access the data from the pets table.
        //
        // The ContentResolver finds the correct ContentProvider to use (i.e. the PetProvider).
        // It does this by checking the content authority section of the PetEntry.CONTENT_URI.
        //
        // After it's found the PetProvider, it and calls query() on it.
        //
        // Returns a Cursor that contains all rows from the pets table, for all columns
        // since the projection includes all columns in the table, and no other selection was made
        Cursor cursor = getContentResolver().query(
                PetEntry.CONTENT_URI,       // The content URI
                projection,                 // The columns to return for each row
                null,                       // The columns for the WHERE clause in SQLite
                null,                       // The values for the WHERE clause in SQLite
                null                        // The sort order for returned rows
        );

        // Find the ListView which will be populated with the pet data.
        ListView petListView = (ListView) findViewById(R.id.list);

        // Setup an Adapter to create a list item for each row of pet data in the Cursor.
        PetCursorAdapter adapter = new PetCursorAdapter(this, cursor);

        // Attach the adapter to the ListView.
        petListView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertPet();
                // Update the UI to show the state of the database
                displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Inserts a pet into the pets table of the database.
     */
    private void insertPet() {
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_PET_NAME, "Toto");
        values.put(PetEntry.COLUMN_PET_BREED, "Terrier");
        values.put(PetEntry.COLUMN_PET_GENDER, PetEntry.GENDER_MALE);
        values.put(PetEntry.COLUMN_PET_WEIGHT, 7);

        // Insert Toto's attributes into the PetProvider, using the ContentResolver.
        // Use the PetEntry.CONTENT_URI to indicate that we want to insert into the pets table.
        // Receive the content URI of the new row.
        Uri newRowUri = getContentResolver().insert(PetEntry.CONTENT_URI, values);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Display the state of the database here rather than in onCreate() because if a child
        // Activity like EditorActivity finishes and returns to this activity, we want to display
        // the state of the database correctly
        displayDatabaseInfo();
    }
}