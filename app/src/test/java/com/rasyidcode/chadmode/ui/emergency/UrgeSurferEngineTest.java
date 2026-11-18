package com.rasyidcode.chadmode.ui.emergency;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class UrgeSurferEngineTest {

    @Test
    public void getStage_variousElapsedTimes_returnsCorrectStages() {
        assertEquals(UrgeWaveStage.RISING, UrgeSurferEngine.getStage(0));
        assertEquals(UrgeWaveStage.RISING, UrgeSurferEngine.getStage(TimeUnit.MINUTES.toMillis(2)));

        assertEquals(UrgeWaveStage.CREST, UrgeSurferEngine.getStage(TimeUnit.MINUTES.toMillis(4)));
        assertEquals(UrgeWaveStage.CREST, UrgeSurferEngine.getStage(TimeUnit.MINUTES.toMillis(6)));

        assertEquals(UrgeWaveStage.DESCENT, UrgeSurferEngine.getStage(TimeUnit.MINUTES.toMillis(8)));
        assertEquals(UrgeWaveStage.DESCENT, UrgeSurferEngine.getStage(TimeUnit.MINUTES.toMillis(10)));

        assertEquals(UrgeWaveStage.CALM, UrgeSurferEngine.getStage(TimeUnit.MINUTES.toMillis(12)));
        assertEquals(UrgeWaveStage.CALM, UrgeSurferEngine.getStage(TimeUnit.MINUTES.toMillis(14)));

        assertEquals(UrgeWaveStage.COMPLETED, UrgeSurferEngine.getStage(UrgeSurferEngine.TOTAL_SURF_MILLIS));
        assertEquals(UrgeWaveStage.COMPLETED, UrgeSurferEngine.getStage(UrgeSurferEngine.TOTAL_SURF_MILLIS + 5000));
    }

    @Test
    public void getProgressPercent_variousElapsedTimes_returnsBoundedValues() {
        assertEquals(0, UrgeSurferEngine.getProgressPercent(0));
        assertEquals(0, UrgeSurferEngine.getProgressPercent(-100));

        long halfWay = UrgeSurferEngine.TOTAL_SURF_MILLIS / 2;
        assertEquals(50, UrgeSurferEngine.getProgressPercent(halfWay));

        assertEquals(100, UrgeSurferEngine.getProgressPercent(UrgeSurferEngine.TOTAL_SURF_MILLIS));
        assertEquals(100, UrgeSurferEngine.getProgressPercent(UrgeSurferEngine.TOTAL_SURF_MILLIS + 10000));
    }

    @Test
    public void formatRemaining_variousRemainingTimes_returnsFormattedString() {
        assertEquals("15:00", UrgeSurferEngine.formatRemaining(UrgeSurferEngine.TOTAL_SURF_MILLIS));
        assertEquals("07:30", UrgeSurferEngine.formatRemaining(TimeUnit.MINUTES.toMillis(7) + TimeUnit.SECONDS.toMillis(30)));
        assertEquals("00:45", UrgeSurferEngine.formatRemaining(TimeUnit.SECONDS.toMillis(45)));
        assertEquals("00:00", UrgeSurferEngine.formatRemaining(0));
        assertEquals("00:00", UrgeSurferEngine.formatRemaining(-500));
    }
}
