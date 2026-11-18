package com.rasyidcode.chadmode.ui.main;

import com.rasyidcode.chadmode.util.TimeFormatter;

/**
 * Immutable UI State representing the dashboard data for the active streak.
 */
public class StreakUiState {

    private final long elapsedMillis;
    private final TimeFormatter.StreakDuration duration;
    private final RankInfo rankInfo;
    private final long startTimeMs;
    private final long longestStreakMs;
    private final int totalResets;

    public StreakUiState(long elapsedMillis,
                         TimeFormatter.StreakDuration duration,
                         RankInfo rankInfo,
                         long startTimeMs,
                         long longestStreakMs,
                         int totalResets) {
        this.elapsedMillis = elapsedMillis;
        this.duration = duration;
        this.rankInfo = rankInfo;
        this.startTimeMs = startTimeMs;
        this.longestStreakMs = longestStreakMs;
        this.totalResets = totalResets;
    }

    public long getElapsedMillis() {
        return elapsedMillis;
    }

    public TimeFormatter.StreakDuration getDuration() {
        return duration;
    }

    public RankInfo getRankInfo() {
        return rankInfo;
    }

    public long getStartTimeMs() {
        return startTimeMs;
    }

    public long getLongestStreakMs() {
        return longestStreakMs;
    }

    public int getTotalResets() {
        return totalResets;
    }
}
