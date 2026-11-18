package com.rasyidcode.chadmode.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Utility for formatting streak durations and timestamps without battery impact.
 */
public final class TimeFormatter {

    private TimeFormatter() {
        // Prevent instantiation
    }

    public static class StreakDuration {
        private final long days;
        private final long hours;
        private final long minutes;
        private final long seconds;

        public StreakDuration(long days, long hours, long minutes, long seconds) {
            this.days = Math.max(0, days);
            this.hours = Math.max(0, hours);
            this.minutes = Math.max(0, minutes);
            this.seconds = Math.max(0, seconds);
        }

        public long getDays() {
            return days;
        }

        public long getHours() {
            return hours;
        }

        public long getMinutes() {
            return minutes;
        }

        public long getSeconds() {
            return seconds;
        }

        public String getDaysFormatted() {
            return String.valueOf(days);
        }

        public String getHoursFormatted() {
            return String.format(Locale.US, "%02d", hours);
        }

        public String getMinutesFormatted() {
            return String.format(Locale.US, "%02d", minutes);
        }

        public String getSecondsFormatted() {
            return String.format(Locale.US, "%02d", seconds);
        }
    }

    /**
     * Deconstructs elapsed milliseconds into days, hours, minutes, and seconds.
     */
    public static StreakDuration parseDuration(long elapsedMillis) {
        if (elapsedMillis <= 0) {
            return new StreakDuration(0, 0, 0, 0);
        }

        long days = TimeUnit.MILLISECONDS.toDays(elapsedMillis);
        long hours = TimeUnit.MILLISECONDS.toHours(elapsedMillis) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedMillis) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedMillis) % 60;

        return new StreakDuration(days, hours, minutes, seconds);
    }

    /**
     * Formats elapsed millis into a human-readable summary (e.g. "12d 4h 10m" or "0d 0h 5m").
     */
    public static String formatSummary(long elapsedMillis) {
        if (elapsedMillis <= 0) {
            return "0d 0h 0m";
        }
        long days = TimeUnit.MILLISECONDS.toDays(elapsedMillis);
        long hours = TimeUnit.MILLISECONDS.toHours(elapsedMillis) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedMillis) % 60;

        if (days > 0) {
            return String.format(Locale.US, "%dd %dh %dm", days, hours, minutes);
        } else if (hours > 0) {
            return String.format(Locale.US, "%dh %dm", hours, minutes);
        } else {
            long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedMillis) % 60;
            return String.format(Locale.US, "%dm %ds", minutes, seconds);
        }
    }

    /**
     * Formats timestamp into readable date, e.g., "Nov 18, 2026".
     */
    public static String formatDate(long timestampMs) {
        if (timestampMs <= 0) {
            return "Never";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
        return sdf.format(new Date(timestampMs));
    }
}
