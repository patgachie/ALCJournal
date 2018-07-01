package ke.co.samek.alcjournal;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import ke.co.samek.alcjournal.database.AppDatabase;
import ke.co.samek.alcjournal.database.JournalEntry;

public class AddJournalEntryViewModel extends ViewModel {

    //Journal Entry member variable for the JournalEntry object wrapped in a LiveData
    private LiveData<JournalEntry> entry;

    // A constructor where loadEntryById of the entryDao is called to initialize the entries variable
    // the constructor receives the database and the entryId
    public AddJournalEntryViewModel(AppDatabase database, int entryId) {
        entry = database.entryDao().loadEntryById(entryId);
    }

    // a getter for the entry variable
    public LiveData<JournalEntry> getEntry() {
        return entry;
    }
}
