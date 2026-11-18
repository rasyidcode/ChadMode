package com.rasyidcode.chadmode.ui.main;

/**
 * Immutable value object holding calculated rank and milestone progression state.
 */
public class RankInfo {

    private final int level;
    private final String title;
    private final String description;
    private final String badgeEmoji;
    private final int minDays;
    private final int nextMilestoneDays;
    private final int progressPercent;
    private final boolean isMaxRank;

    public RankInfo(int level, String title, String description, String badgeEmoji,
                    int minDays, int nextMilestoneDays, int progressPercent, boolean isMaxRank) {
        this.level = level;
        this.title = title;
        this.description = description;
        this.badgeEmoji = badgeEmoji;
        this.minDays = minDays;
        this.nextMilestoneDays = nextMilestoneDays;
        this.progressPercent = Math.max(0, Math.min(100, progressPercent));
        this.isMaxRank = isMaxRank;
    }

    public int getLevel() {
        return level;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getBadgeEmoji() {
        return badgeEmoji;
    }

    public int getMinDays() {
        return minDays;
    }

    public int getNextMilestoneDays() {
        return nextMilestoneDays;
    }

    public int getProgressPercent() {
        return progressPercent;
    }

    public boolean isMaxRank() {
        return isMaxRank;
    }
}
