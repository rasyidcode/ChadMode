package com.rasyidcode.chadmode.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.rasyidcode.chadmode.data.local.entity.RelapseRecord;

import java.util.List;

/**
 * Data Access Object for local relapse logs and trigger reflections.
 */
@Dao
public interface RelapseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(RelapseRecord record);

    @Query("SELECT * FROM relapse_records ORDER BY timestampMs DESC")
    LiveData<List<RelapseRecord>> getAllRelapses();

    @Query("SELECT * FROM relapse_records ORDER BY timestampMs DESC")
    List<RelapseRecord> getAllRelapsesSync();

    @Query("SELECT MAX(streakDurationMs) FROM relapse_records")
    LiveData<Long> getLongestRecordedStreak();

    @Query("SELECT COUNT(*) FROM relapse_records")
    LiveData<Integer> getRelapseCount();

    @Query("SELECT * FROM relapse_records WHERE triggerCategory = :category ORDER BY timestampMs DESC")
    LiveData<List<RelapseRecord>> getRelapsesByTrigger(String category);

    @Query("DELETE FROM relapse_records")
    void deleteAll();
}
