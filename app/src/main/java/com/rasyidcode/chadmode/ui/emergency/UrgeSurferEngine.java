package com.rasyidcode.chadmode.ui.emergency;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Manages calculations and time milestones for the 15-minute Urge Surfer mechanism.
 */
public final class UrgeSurferEngine {

    public static final long TOTAL_SURF_MILLIS = 15 * 60 * 1000L; // 15 minutes

    private UrgeSurferEngine() {
        // Utility
    }

    /**
     * Determines current wave stage from elapsed time in milliseconds.
     */
    public static UrgeWaveStage getStage(long elapsedMillis) {
        if (elapsedMillis >= TOTAL_SURF_MILLIS) {
            return UrgeWaveStage.COMPLETED;
        }

        long elapsedMinutes = TimeUnit.MILLISECONDS.toMinutes(elapsedMillis);

        if (elapsedMinutes < 3) {
            return UrgeWaveStage.RISING;
        } else if (elapsedMinutes < 7) {
            return UrgeWaveStage.CREST;
        } else if (elapsedMinutes < 11) {
            return UrgeWaveStage.DESCENT;
        } else {
            return UrgeWaveStage.CALM;
        }
    }

    /**
     * Computes completion percentage (0 - 100).
     */
    public static int getProgressPercent(long elapsedMillis) {
        if (elapsedMillis <= 0) {
            return 0;
        }
        if (elapsedMillis >= TOTAL_SURF_MILLIS) {
            return 100;
        }
        return (int) Math.round(((double) elapsedMillis / TOTAL_SURF_MILLIS) * 100);
    }

    /**
     * Formats milliseconds remaining into mm:ss.
     */
    public static String formatRemaining(long remainingMillis) {
        if (remainingMillis <= 0) {
            return "00:00";
        }
        long minutes = TimeUnit.MILLISECONDS.toMinutes(remainingMillis);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(remainingMillis) % 60;
        return String.format(Locale.US, "%02d:%02d", minutes, seconds);
    }
}
