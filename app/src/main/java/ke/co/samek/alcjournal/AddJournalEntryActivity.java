/*
* Copyright (C) 2016 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package ke.co.samek.alcjournal;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import ke.co.samek.alcjournal.database.AppDatabase;
import ke.co.samek.alcjournal.database.JournalEntry;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class AddJournalEntryActivity extends AppCompatActivity {

    // Extra for the journal entry ID to be received in the intent
    public static final String EXTRA_ENTRY_ID = "extraTaskId";
    // Extra for the journal entry ID to be received after rotation
    public static final String INSTANCE_ENTRY_ID = "instanceTaskId";
    // Constant for default entry id to be used when not in update mode
    private static final int DEFAULT_ENTRY_ID = -1;
    //Date Format
    private static final String DATE_FORMAT = "MMM dd, hh:mm:ss";

    // Constant for logging
    private static final String TAG = AddJournalEntryActivity.class.getSimpleName();
    // Fields for views
    EditText mEditText;
    FloatingActionButton mButton;
    TextView mJournalDate;

    //Variable to hold the state of the button, whether edit or save
    private static String mButtonStatus = "Edit";

    // Date formatter
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    private int mEntryId = DEFAULT_ENTRY_ID;

    private Date mEntryDate = new Date();

    // Member variable for the Database
    private AppDatabase mDb;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journal_entry);

        initViews();

        mDb = AppDatabase.getInstance(getApplicationContext());

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_ENTRY_ID)) {
            mEntryId = savedInstanceState.getInt(INSTANCE_ENTRY_ID, DEFAULT_ENTRY_ID);
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_ENTRY_ID)) {
            //change title bar to show that you are viewing an entry
            ((AppCompatActivity)this).getSupportActionBar().setTitle(getString(R.string.view_entry_activity_name));

            //hide keyboard
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

            //button should show the edit icon so that when clicked it allows you to edit
            mButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_edit));

            //disable editing the edit text because we are just viewing
            mEditText.setEnabled(false);
            mEditText.setTextColor(Color.BLACK);
            if (mEntryId == DEFAULT_ENTRY_ID) {
                // populate the UI
                mEntryId = intent.getIntExtra(EXTRA_ENTRY_ID, DEFAULT_ENTRY_ID);

                // Declare an AddJournalEntryViewModelFactory using mDb and mEntryId
                AddJournalEntryViewModelFactory factory = new AddJournalEntryViewModelFactory(mDb, mEntryId);
                // Declare an AddJournalEntryViewModel variable and initialize it by calling ViewModelProviders.of
                // for that use the factory created above AddJournalEntryViewModel
                final AddJournalEntryViewModel viewModel
                        = ViewModelProviders.of(this, factory).get(AddJournalEntryViewModel.class);

                // Observe the LiveData object in the ViewModel. Use it also when removing the observer
                viewModel.getEntry().observe(this, new Observer<JournalEntry>() {
                    @Override
                    public void onChanged(@Nullable JournalEntry journalEntry) {
                        viewModel.getEntry().removeObserver(this);
                        populateUI(journalEntry);
                    }
                });
            }
        }
        else{
            mButtonStatus = "Save";
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_ENTRY_ID, mEntryId);
        super.onSaveInstanceState(outState);
    }

    /**
     * initViews is called from onCreate to init the member variable views
     */
    private void initViews() {
        mEditText = findViewById(R.id.editTextTaskDescription);

        mJournalDate = findViewById(R.id.journalDate);
        mJournalDate.setText(dateFormat.format(mEntryDate));

        mButton = findViewById(R.id.saveButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveButtonClicked();
            }
        });
    }

    /**
     * populateUI would be called to populate the UI when in update mode
     *
     * @param entry the JournalEntry to populate the UI
     */
    private void populateUI(JournalEntry entry) {
        if (entry == null) {
            return;
        }

        mEditText.setText(entry.getEntry());

        mEntryDate = entry.getJournalAddedAt();

        mJournalDate.setText(dateFormat.format(mEntryDate));
    }

    /**
     * onSaveButtonClicked is called when the "save" button is clicked.
     * It retrieves user input and inserts that new entry data into the underlying database.
     */
    public void onSaveButtonClicked() {
        if(mButtonStatus == "Edit"){
            //change title bar to show that you are viewing an entry
            ((AppCompatActivity)this).getSupportActionBar().setTitle(getString(R.string.edit_entry_activity_name));

            //Toggle the mButtonStatus value
            mButtonStatus = "Save";
            //change button icon to save and change the edittext to editable
            mEditText.setEnabled(true);
            mEditText.requestFocus();
            mButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_save));
        }
        else {
            //Toggle the mButtonStatus value
            mButtonStatus = "Edit";
            String entryText = mEditText.getText().toString();

            final JournalEntry entry = new JournalEntry(entryText, mEntryDate);
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    if (mEntryId == DEFAULT_ENTRY_ID) {
                        // insert new entry
                        mDb.entryDao().insertEntry(entry);
                    } else {
                        //update entry
                        entry.setId(mEntryId);
                        entry.setJournalAddedAt(mEntryDate);
                        mDb.entryDao().updateEntry(entry);
                    }
                    finish();
                }
            });
        }
    }

}
