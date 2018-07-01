package ke.co.samek.alcjournal;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import ke.co.samek.alcjournal.database.AppDatabase;

// class extends ViewModel ViewModelProvider.NewInstanceFactory
public class AddJournalEntryViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    // two member variables. One for the database and one for the entryId
    private final AppDatabase mDb;
    private final int mEntryId;

    // Initialize the member variables in the constructor with the parameters received
    public AddJournalEntryViewModelFactory(AppDatabase database, int entryId) {
        mDb = database;
        mEntryId = entryId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new AddJournalEntryViewModel(mDb, mEntryId);
    }
}
