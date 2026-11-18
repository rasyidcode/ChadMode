package com.rasyidcode.chadmode.data.pref;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Manages local persistence for streak state and user progress via SharedPreferences.
 * 100% offline, local-first.
 */
public class PreferenceManager {

    private static final String PREF_NAME = "chadmode_streak_prefs";
    private static final String KEY_STREAK_START_TIME = "key_streak_start_time_ms";
    private static final String KEY_LONGEST_STREAK = "key_longest_streak_ms";
    private static final String KEY_TOTAL_RESETS = "key_total_resets_count";

    private static volatile PreferenceManager sInstance;
    private final SharedPreferences preferences;

    private PreferenceManager(Context context) {
        this.preferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static PreferenceManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (PreferenceManager.class) {
                if (sInstance == null) {
                    sInstance = new PreferenceManager(context);
                }
            }
        }
        return sInstance;
    }

    /**
     * Initializes the streak timestamp if this is the first launch.
     */
    public synchronized void initStreakIfEmpty() {
        if (getStreakStartTime() <= 0) {
            setStreakStartTime(System.currentTimeMillis());
        }
    }

    public long getStreakStartTime() {
        return preferences.getLong(KEY_STREAK_START_TIME, 0L);
    }

    public void setStreakStartTime(long timestampMs) {
        preferences.edit().putLong(KEY_STREAK_START_TIME, timestampMs).apply();
    }

    public long getLongestStreak() {
        return preferences.getLong(KEY_LONGEST_STREAK, 0L);
    }

    public void setLongestStreak(long durationMs) {
        preferences.edit().putLong(KEY_LONGEST_STREAK, durationMs).apply();
    }

    public int getTotalResets() {
        return preferences.getInt(KEY_TOTAL_RESETS, 0);
    }

    public void incrementTotalResets() {
        int current = getTotalResets();
        preferences.edit().putInt(KEY_TOTAL_RESETS, current + 1).apply();
    }

    /**
     * Resets the active streak to the given reset timestamp.
     * Updates longest streak if the current streak exceeded previous record.
     */
    public synchronized void resetStreak(long resetTimestampMs) {
        long startTime = getStreakStartTime();
        if (startTime > 0) {
            long currentStreak = Math.max(0, resetTimestampMs - startTime);
            long bestStreak = getLongestStreak();
            if (currentStreak > bestStreak) {
                setLongestStreak(currentStreak);
            }
        }
        setStreakStartTime(resetTimestampMs);
        incrementTotalResets();
    }
}
