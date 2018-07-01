package ke.co.samek.alcjournal.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "JournalEntry")
public class JournalEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String entry;
    @ColumnInfo(name = "journal_added_at")
    private Date journalAddedAt;

    @Ignore
    public JournalEntry(String entry, Date journalAddedAt) {
        this.entry = entry;
        this.journalAddedAt = journalAddedAt;
    }

    public JournalEntry(int id, String entry, Date journalAddedAt) {
        this.id = id;
        this.entry = entry;
        this.journalAddedAt = journalAddedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public Date getJournalAddedAt() {
        return journalAddedAt;
    }

    public void setJournalAddedAt(Date journalAddedAt) {
        this.journalAddedAt = journalAddedAt;
    }
}
