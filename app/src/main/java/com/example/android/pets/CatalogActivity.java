package com.example.android.pets;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.pets.data.PetContract.PetEntry;

import java.net.URI;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    // The name of this class, for the purposes of logs
    private static final String LOG_TAG = CatalogActivity.class.getName();

    // Unique identifier for the Loader that queries the ContentResolver for data
    // from the PetProvider class. Any value could be used here, as long as it is unique for this
    // loader.
    private static final int PET_LOADER = 0;

    // The adapter which holds the Cursor data, and has instructions for creating list item Views
    // for each row in its Cursor.
    private PetCursorAdapter mCursorAdapter;

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

        // Setup an Adapter to create a list item for each row of pet data in the Cursor.
        // There's no pet data yet (until the loader finishes), so pass in null for the Cursor.
        mCursorAdapter = new PetCursorAdapter(this, null);

        // Attach the PetCursorAdapter to the ListView.
        petListView.setAdapter(mCursorAdapter);


        // Set up an OnItemClickListener which will open up EditorActivity if a list item is clicked
        petListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Create an Intent to start EditorActivity
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);

                // Append the original content URI (content://com.example.android.pets/pets)
                // with the row id of the list item that was clicked. For example, if the list item
                // with the row ID = 3 was clicked, a URI of the form
                // content://com.example.android.pets/pets/3 should be the result.
                Uri currentPetUri = ContentUris.withAppendedId(PetEntry.CONTENT_URI, id);

                Log.i(LOG_TAG, "The content URI being passed is " + currentPetUri.toString());

                // Set the URI on the data field of the intent
                intent.setData(currentPetUri);

                // Launch the {@link EditorActivity} to display the data for the current pet.
                startActivity(intent);
            }
        });

        // Initialise the Loader.
        getLoaderManager().initLoader(PET_LOADER, null, this);
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
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        // If this is the Loader that queries the pets table,
        if (loaderId == PET_LOADER) {
            // Define the projection (i.e. the column names you want returned in the Cursor object).
            // Each list item displays only the name and breed, so we don't need the gender and
            // weight columns. However, the _id column is needed because the CursorAdapter assumes
            // that the Cursor contains a column called _id in order to work correctly.
            String[] projection = {
                    PetEntry._ID,
                    PetEntry.COLUMN_PET_NAME,
                    PetEntry.COLUMN_PET_BREED
            };

            // Create the CursorLoader with the Content URI of the pets table and a projection.
            return new CursorLoader(
                    this,                       // Parent activity context
                    PetEntry.CONTENT_URI,       // Content URI of table to query
                    projection,                 // Columns to include in the resulting Cursor
                    null,                       // No selection clause
                    null,                       // No selection arguments
                    null                        // Default sort order
            );
        }

        // If it's not the ID for the Loader that queries the pets table, don't create a Loader.
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // When the query of the pets table is completed, update the PetCursorAdapter with the
        // new Cursor containing pets data. The adapter will cause its associated ListView to
        // update, thus refreshing the UI.
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Invoked whenever the CursorLoader is reset. For example if the data in the
        // ContentProvider changes, it means data in the current Cursor has become outdated/stale.
        //
        // Clear out the adapter's reference to the outdated Cursor.
        mCursorAdapter.swapCursor(null);
    }
}