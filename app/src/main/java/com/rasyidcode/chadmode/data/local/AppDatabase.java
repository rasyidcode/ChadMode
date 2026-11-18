package com.rasyidcode.chadmode.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.rasyidcode.chadmode.data.local.entity.RelapseRecord;

/**
 * Main Room Database for ChadMode local persistence.
 * 100% offline, privacy critical.
 */
@Database(entities = {RelapseRecord.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "chadmode_local.db";
    private static volatile AppDatabase sInstance;

    public abstract RelapseDao relapseDao();

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            DATABASE_NAME
                    ).fallbackToDestructiveMigration().build();
                }
            }
        }
        return sInstance;
    }
}
