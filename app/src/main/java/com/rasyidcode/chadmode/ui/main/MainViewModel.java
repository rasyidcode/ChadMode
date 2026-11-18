package com.rasyidcode.chadmode.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.rasyidcode.chadmode.data.pref.PreferenceManager;
import com.rasyidcode.chadmode.util.AppExecutors;
import com.rasyidcode.chadmode.util.TimeFormatter;

/**
 * ViewModel for the Main Dashboard.
 * Survives configuration changes and manages streak calculations cleanly.
 */
public class MainViewModel extends AndroidViewModel {

    private final PreferenceManager preferenceManager;
    private final AppExecutors appExecutors;
    private final MutableLiveData<StreakUiState> streakState = new MutableLiveData<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
        this.preferenceManager = PreferenceManager.getInstance(application);
        this.appExecutors = AppExecutors.getInstance();

        this.preferenceManager.initStreakIfEmpty();
        refresh();
    }

    public LiveData<StreakUiState> getStreakState() {
        return streakState;
    }

    /**
     * Periodic ticker update invoked by foreground loop.
     * Computes elapsed streak state in memory for zero battery drain.
     */
    public void tick() {
        long now = System.currentTimeMillis();
        long start = preferenceManager.getStreakStartTime();
        long elapsed = Math.max(0, now - start);

        TimeFormatter.StreakDuration duration = TimeFormatter.parseDuration(elapsed);
        RankInfo rank = RankManager.calculateRank(elapsed);

        streakState.setValue(new StreakUiState(
                elapsed,
                duration,
                rank,
                start,
                preferenceManager.getLongestStreak(),
                preferenceManager.getTotalResets()
        ));
    }

    /**
     * Refreshes the UI state immediately.
     */
    public void refresh() {
        tick();
    }

    /**
     * Resets the active streak to the current timestamp.
     * Runs disk persistence off the main thread.
     */
    public void resetStreak() {
        appExecutors.diskIO().execute(() -> {
            long now = System.currentTimeMillis();
            preferenceManager.resetStreak(now);
            appExecutors.mainThread().execute(this::refresh);
        });
    }

    /**
     * Allows setting a custom start time (e.g. if recovering historical streak).
     */
    public void setCustomStartTime(long timestampMs) {
        appExecutors.diskIO().execute(() -> {
            preferenceManager.setStreakStartTime(timestampMs);
            appExecutors.mainThread().execute(this::refresh);
        });
    }
}
