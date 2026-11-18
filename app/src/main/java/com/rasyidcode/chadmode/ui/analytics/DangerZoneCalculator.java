package com.rasyidcode.chadmode.ui.analytics;

import com.rasyidcode.chadmode.data.local.entity.RelapseRecord;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Computes vulnerability metrics and danger zone patterns from relapse records.
 * Local-first, 100% offline mathematical analysis.
 */
public final class DangerZoneCalculator {

    private DangerZoneCalculator() {
        // Utility
    }

    public static class DangerZoneStats {
        private final int totalRelapses;
        private final String peakDangerPeriod;
        private final String topTrigger;
        private final String topLocation;
        private final long averageStreakMs;
        private final long longestStreakMs;

        public DangerZoneStats(int totalRelapses, String peakDangerPeriod,
                               String topTrigger, String topLocation,
                               long averageStreakMs, long longestStreakMs) {
            this.totalRelapses = totalRelapses;
            this.peakDangerPeriod = peakDangerPeriod;
            this.topTrigger = topTrigger;
            this.topLocation = topLocation;
            this.averageStreakMs = averageStreakMs;
            this.longestStreakMs = longestStreakMs;
        }

        public int getTotalRelapses() {
            return totalRelapses;
        }

        public String getPeakDangerPeriod() {
            return peakDangerPeriod;
        }

        public String getTopTrigger() {
            return topTrigger;
        }

        public String getTopLocation() {
            return topLocation;
        }

        public long getAverageStreakMs() {
            return averageStreakMs;
        }

        public long getLongestStreakMs() {
            return longestStreakMs;
        }
    }

    public static DangerZoneStats compute(List<RelapseRecord> records) {
        if (records == null || records.isEmpty()) {
            return new DangerZoneStats(
                    0,
                    "No data yet",
                    "None",
                    "None",
                    0,
                    0
            );
        }

        int total = records.size();
        long totalStreakMs = 0;
        long longestStreakMs = 0;

        int lateNightCount = 0; // 23:00 - 05:00 (Red Zone)
        int morningCount = 0;   // 05:00 - 12:00
        int afternoonCount = 0; // 12:00 - 17:00
        int eveningCount = 0;   // 17:00 - 23:00

        Map<String, Integer> triggerCounts = new HashMap<>();
        Map<String, Integer> locationCounts = new HashMap<>();

        Calendar cal = Calendar.getInstance();

        for (RelapseRecord record : records) {
            totalStreakMs += record.getStreakDurationMs();
            if (record.getStreakDurationMs() > longestStreakMs) {
                longestStreakMs = record.getStreakDurationMs();
            }

            cal.setTimeInMillis(record.getTimestampMs());
            int hour = cal.get(Calendar.HOUR_OF_DAY);

            if (hour >= 23 || hour < 5) {
                lateNightCount++;
            } else if (hour < 12) {
                morningCount++;
            } else if (hour < 17) {
                afternoonCount++;
            } else {
                eveningCount++;
            }

            String trigger = record.getTriggerCategory();
            if (trigger != null && !trigger.trim().isEmpty()) {
                triggerCounts.put(trigger, triggerCounts.getOrDefault(trigger, 0) + 1);
            }

            String location = record.getLocationContext();
            if (location != null && !location.trim().isEmpty()) {
                locationCounts.put(location, locationCounts.getOrDefault(location, 0) + 1);
            }
        }

        long averageStreakMs = total > 0 ? (totalStreakMs / total) : 0;

        // Determine peak danger period
        String peakPeriod = "Late Night (23:00 - 05:00)";
        int maxPeriodCount = lateNightCount;

        if (eveningCount > maxPeriodCount) {
            maxPeriodCount = eveningCount;
            peakPeriod = "Evening (17:00 - 23:00)";
        }
        if (afternoonCount > maxPeriodCount) {
            maxPeriodCount = afternoonCount;
            peakPeriod = "Afternoon (12:00 - 17:00)";
        }
        if (morningCount > maxPeriodCount) {
            maxPeriodCount = morningCount;
            peakPeriod = "Morning (05:00 - 12:00)";
        }

        int percentage = (int) Math.round(((double) maxPeriodCount / total) * 100);
        String formattedDangerPeriod = String.format(Locale.US, "%s • %d%% of relapses", peakPeriod, percentage);

        // Find top trigger
        String topTrigger = "None";
        int maxTriggerCount = 0;
        for (Map.Entry<String, Integer> entry : triggerCounts.entrySet()) {
            if (entry.getValue() > maxTriggerCount) {
                maxTriggerCount = entry.getValue();
                topTrigger = entry.getKey();
            }
        }

        // Find top location
        String topLocation = "None";
        int maxLocationCount = 0;
        for (Map.Entry<String, Integer> entry : locationCounts.entrySet()) {
            if (entry.getValue() > maxLocationCount) {
                maxLocationCount = entry.getValue();
                topLocation = entry.getKey();
            }
        }

        return new DangerZoneStats(
                total,
                formattedDangerPeriod,
                topTrigger,
                topLocation,
                averageStreakMs,
                longestStreakMs
        );
    }
}
