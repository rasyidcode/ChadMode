package com.rasyidcode.chadmode.ui.main;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RankManagerTest {

    @Test
    public void calculateRank_dayZero_returnsDopamineReset() {
        RankInfo rank = RankManager.calculateRank(0);
        assertEquals("Dopamine Reset", rank.getTitle());
        assertEquals("🧠", rank.getBadgeEmoji());
        assertEquals(0, rank.getLevel());
        assertEquals(3, rank.getNextMilestoneDays());
        assertEquals(0, rank.getProgressPercent());
        assertFalse(rank.isMaxRank());

        // 1.5 days should be ~50% progress toward Day 3
        long oneAndHalfDays = (long) (1.5 * 24 * 60 * 60 * 1000);
        RankInfo halfWay = RankManager.calculateRank(oneAndHalfDays);
        assertEquals("Dopamine Reset", halfWay.getTitle());
        assertEquals(50, halfWay.getProgressPercent());
    }

    @Test
    public void calculateRank_dayThree_returnsAwakening() {
        long dayThree = TimeUnit.DAYS.toMillis(3);
        RankInfo rank = RankManager.calculateRank(dayThree);
        assertEquals("Awakening", rank.getTitle());
        assertEquals("🔥", rank.getBadgeEmoji());
        assertEquals(1, rank.getLevel());
        assertEquals(7, rank.getNextMilestoneDays());
        assertEquals(0, rank.getProgressPercent());
        assertFalse(rank.isMaxRank());
    }

    @Test
    public void calculateRank_daySeven_returnsTheSpark() {
        long daySeven = TimeUnit.DAYS.toMillis(7);
        RankInfo rank = RankManager.calculateRank(daySeven);
        assertEquals("The Spark", rank.getTitle());
        assertEquals("⚡", rank.getBadgeEmoji());
        assertEquals(2, rank.getLevel());
        assertEquals(14, rank.getNextMilestoneDays());
        assertFalse(rank.isMaxRank());
    }

    @Test
    public void calculateRank_dayFourteen_returnsIronDiscipline() {
        long dayFourteen = TimeUnit.DAYS.toMillis(14);
        RankInfo rank = RankManager.calculateRank(dayFourteen);
        assertEquals("Iron Discipline", rank.getTitle());
        assertEquals("🛡️", rank.getBadgeEmoji());
        assertEquals(3, rank.getLevel());
        assertEquals(30, rank.getNextMilestoneDays());
        assertFalse(rank.isMaxRank());
    }

    @Test
    public void calculateRank_dayThirty_returnsSigmaGrindset() {
        long dayThirty = TimeUnit.DAYS.toMillis(30);
        RankInfo rank = RankManager.calculateRank(dayThirty);
        assertEquals("Sigma Grindset", rank.getTitle());
        assertEquals("🐺", rank.getBadgeEmoji());
        assertEquals(4, rank.getLevel());
        assertEquals(60, rank.getNextMilestoneDays());
        assertFalse(rank.isMaxRank());
    }

    @Test
    public void calculateRank_daySixty_returnsTitan() {
        long daySixty = TimeUnit.DAYS.toMillis(60);
        RankInfo rank = RankManager.calculateRank(daySixty);
        assertEquals("Titan", rank.getTitle());
        assertEquals("🏛️", rank.getBadgeEmoji());
        assertEquals(5, rank.getLevel());
        assertEquals(90, rank.getNextMilestoneDays());
        assertFalse(rank.isMaxRank());
    }

    @Test
    public void calculateRank_dayNinetyPlus_returnsAscendedGigaChad() {
        long dayNinety = TimeUnit.DAYS.toMillis(90);
        RankInfo rank = RankManager.calculateRank(dayNinety);
        assertEquals("Ascended GigaChad", rank.getTitle());
        assertEquals("🗿", rank.getBadgeEmoji());
        assertEquals(6, rank.getLevel());
        assertEquals(100, rank.getProgressPercent());
        assertTrue(rank.isMaxRank());

        long dayOneHundredFifty = TimeUnit.DAYS.toMillis(150);
        RankInfo rank150 = RankManager.calculateRank(dayOneHundredFifty);
        assertEquals("Ascended GigaChad", rank150.getTitle());
        assertEquals(100, rank150.getProgressPercent());
        assertTrue(rank150.isMaxRank());
    }
}
