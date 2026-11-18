package com.rasyidcode.chadmode.ui.main;

import java.util.concurrent.TimeUnit;

/**
 * Calculates user rank and progress toward the next sobriety milestone
 * according to the GigaChad progression hierarchy.
 */
public final class RankManager {

    private RankManager() {
        // Utility class
    }

    public static RankInfo calculateRank(long elapsedMillis) {
        if (elapsedMillis < 0) {
            elapsedMillis = 0;
        }

        long totalDays = TimeUnit.MILLISECONDS.toDays(elapsedMillis);
        double dayFraction = elapsedMillis / (double) (1000L * 60 * 60 * 24);

        if (totalDays < 3) {
            int progress = (int) Math.round((dayFraction / 3.0) * 100);
            return new RankInfo(
                    0,
                    "Dopamine Reset",
                    "Neural reset initiated. The brain is adapting.",
                    "🧠",
                    0,
                    3,
                    progress,
                    false
            );
        } else if (totalDays < 7) {
            int progress = (int) Math.round(((dayFraction - 3.0) / 4.0) * 100);
            return new RankInfo(
                    1,
                    "Awakening",
                    "Physical withdrawal fading. Mental clarity emerging.",
                    "🔥",
                    3,
                    7,
                    progress,
                    false
            );
        } else if (totalDays < 14) {
            int progress = (int) Math.round(((dayFraction - 7.0) / 7.0) * 100);
            return new RankInfo(
                    2,
                    "The Spark",
                    "Energy surge and self-respect returning.",
                    "⚡",
                    7,
                    14,
                    progress,
                    false
            );
        } else if (totalDays < 30) {
            int progress = (int) Math.round(((dayFraction - 14.0) / 16.0) * 100);
            return new RankInfo(
                    3,
                    "Iron Discipline",
                    "Brain fog lifting. Focus and presence sharpening.",
                    "🛡️",
                    14,
                    30,
                    progress,
                    false
            );
        } else if (totalDays < 60) {
            int progress = (int) Math.round(((dayFraction - 30.0) / 30.0) * 100);
            return new RankInfo(
                    4,
                    "Sigma Grindset",
                    "Dopamine baseline stabilized. Habitual control.",
                    "🐺",
                    30,
                    60,
                    progress,
                    false
            );
        } else if (totalDays < 90) {
            int progress = (int) Math.round(((dayFraction - 60.0) / 30.0) * 100);
            return new RankInfo(
                    5,
                    "Titan",
                    "High emotional resilience. Neural pathways rewired.",
                    "🏛️",
                    60,
                    90,
                    progress,
                    false
            );
        } else {
            return new RankInfo(
                    6,
                    "Ascended GigaChad",
                    "Unshakable self-mastery. Complete neurological transformation.",
                    "🗿",
                    90,
                    90,
                    100,
                    true
            );
        }
    }
}
