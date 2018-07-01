package ke.co.samek.alcjournal;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import ke.co.samek.alcjournal.database.AppDatabase;
import ke.co.samek.alcjournal.database.JournalEntry;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    // Constant for logging
    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<JournalEntry>> tasks;

    public MainViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
        tasks = database.entryDao().loadAllEntries();
    }

    public LiveData<List<JournalEntry>> getTasks() {
        return tasks;
    }
}
