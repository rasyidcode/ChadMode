package com.rasyidcode.chadmode.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Room entity representing an individual relapse record and self-reflection journal.
 * Local-first, 100% offline.
 */
@Entity(tableName = "relapse_records")
public class RelapseRecord {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private long timestampMs;
    private long streakDurationMs;
    private String triggerCategory;
    private String locationContext;
    private int moodRating;
    private String journalNote;

    public RelapseRecord(long timestampMs, long streakDurationMs, String triggerCategory,
                         String locationContext, int moodRating, String journalNote) {
        this.timestampMs = timestampMs;
        this.streakDurationMs = streakDurationMs;
        this.triggerCategory = triggerCategory;
        this.locationContext = locationContext;
        this.moodRating = moodRating;
        this.journalNote = journalNote;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTimestampMs() {
        return timestampMs;
    }

    public void setTimestampMs(long timestampMs) {
        this.timestampMs = timestampMs;
    }

    public long getStreakDurationMs() {
        return streakDurationMs;
    }

    public void setStreakDurationMs(long streakDurationMs) {
        this.streakDurationMs = streakDurationMs;
    }

    public String getTriggerCategory() {
        return triggerCategory;
    }

    public void setTriggerCategory(String triggerCategory) {
        this.triggerCategory = triggerCategory;
    }

    public String getLocationContext() {
        return locationContext;
    }

    public void setLocationContext(String locationContext) {
        this.locationContext = locationContext;
    }

    public int getMoodRating() {
        return moodRating;
    }

    public void setMoodRating(int moodRating) {
        this.moodRating = moodRating;
    }

    public String getJournalNote() {
        return journalNote;
    }

    public void setJournalNote(String journalNote) {
        this.journalNote = journalNote;
    }
}
