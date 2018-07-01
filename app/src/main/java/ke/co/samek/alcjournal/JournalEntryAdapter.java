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

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ke.co.samek.alcjournal.database.JournalEntry;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * This JournalEntryAdapter creates and binds ViewHolders, that hold the description and priority of a task,
 * to a RecyclerView to efficiently display data.
 */
public class JournalEntryAdapter extends RecyclerView.Adapter<JournalEntryAdapter.EntryViewHolder> {

    // Constant for date format
    private static final String DATE_FORMAT = "MMM dd, hh:mm:ss";

    // Member variable to handle item clicks
    final private ItemClickListener mItemClickListener;
    // Class variables for the List that holds task data and the Context
    private List<JournalEntry> mJournalEntries;
    private Context mContext;
    // Date formatter
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    /**
     * Constructor for the JournalEntryAdapter that initializes the Context.
     *
     * @param context  the current Context
     * @param listener the ItemClickListener
     */
    public JournalEntryAdapter(Context context, ItemClickListener listener) {
        mContext = context;
        mItemClickListener = listener;
    }

    /**
     * Called when ViewHolders are created to fill a RecyclerView.
     *
     * @return A new EntryViewHolder that holds the view for each entry
     */
    @Override
    public EntryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the journal_entry_layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.journal_entry_layout, parent, false);

        return new EntryViewHolder(view);
    }

    /**
     * Called by the RecyclerView to display data at a specified position in the Cursor.
     *
     * @param holder   The ViewHolder to bind Cursor data to
     * @param position The position of the data in the Cursor
     */
    @Override
    public void onBindViewHolder(EntryViewHolder holder, int position) {
        // Determine the values of the wanted data
        JournalEntry journalEntry = mJournalEntries.get(position);
        String entry = journalEntry.getEntry();
        String journalAddedAt = dateFormat.format(journalEntry.getJournalAddedAt());

        //Set values
        holder.entryView.setText(entry);
        holder.journalAddedAtView.setText(journalAddedAt);
    }

    /**
     * Returns the number of items to display.
     */
    @Override
    public int getItemCount() {
        if (mJournalEntries == null) {
            return 0;
        }
        return mJournalEntries.size();
    }

    public List<JournalEntry> getEntries() {
        return mJournalEntries;
    }

    /**
     * When data changes, this method updates the list of journalEntries
     * and notifies the adapter to use the new values on it
     */
    public void setEntries(List<JournalEntry> journalEntries) {
        mJournalEntries = journalEntries;
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }

    // Inner class for creating ViewHolders
    class EntryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Class variables for the entry and date added TextViews
        TextView entryView;
        TextView journalAddedAtView;

        /**
         * Constructor for the EntryViewHolders.
         *
         * @param itemView The view inflated in onCreateViewHolder
         */
        public EntryViewHolder(View itemView) {
            super(itemView);

            entryView = itemView.findViewById(R.id.entry);
            journalAddedAtView = itemView.findViewById(R.id.journalAddedOn);
            //priorityView = itemView.findViewById(R.id.priorityTextView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int elementId = mJournalEntries.get(getAdapterPosition()).getId();
            mItemClickListener.onItemClickListener(elementId);
        }
    }
}