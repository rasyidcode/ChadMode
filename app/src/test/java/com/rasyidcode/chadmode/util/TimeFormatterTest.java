package com.rasyidcode.chadmode.util;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TimeFormatterTest {

    @Test
    public void parseDuration_zeroAndNegative_returnsAllZeros() {
        TimeFormatter.StreakDuration zero = TimeFormatter.parseDuration(0);
        assertEquals(0, zero.getDays());
        assertEquals(0, zero.getHours());
        assertEquals(0, zero.getMinutes());
        assertEquals(0, zero.getSeconds());
        assertEquals("0", zero.getDaysFormatted());
        assertEquals("00", zero.getHoursFormatted());
        assertEquals("00", zero.getMinutesFormatted());
        assertEquals("00", zero.getSecondsFormatted());

        TimeFormatter.StreakDuration negative = TimeFormatter.parseDuration(-5000);
        assertEquals(0, negative.getDays());
        assertEquals("00", negative.getSecondsFormatted());
    }

    @Test
    public void parseDuration_exactTime_returnsCorrectComponents() {
        long millis = TimeUnit.DAYS.toMillis(5)
                + TimeUnit.HOURS.toMillis(4)
                + TimeUnit.MINUTES.toMillis(9)
                + TimeUnit.SECONDS.toMillis(42);

        TimeFormatter.StreakDuration duration = TimeFormatter.parseDuration(millis);
        assertEquals(5, duration.getDays());
        assertEquals(4, duration.getHours());
        assertEquals(9, duration.getMinutes());
        assertEquals(42, duration.getSeconds());

        assertEquals("5", duration.getDaysFormatted());
        assertEquals("04", duration.getHoursFormatted());
        assertEquals("09", duration.getMinutesFormatted());
        assertEquals("42", duration.getSecondsFormatted());
    }

    @Test
    public void formatSummary_variousRanges_returnsCleanSummaries() {
        assertEquals("0d 0h 0m", TimeFormatter.formatSummary(0));
        assertEquals("0d 0h 0m", TimeFormatter.formatSummary(-100));

        long minutesOnly = TimeUnit.MINUTES.toMillis(15) + TimeUnit.SECONDS.toMillis(30);
        assertEquals("15m 30s", TimeFormatter.formatSummary(minutesOnly));

        long hoursAndMinutes = TimeUnit.HOURS.toMillis(3) + TimeUnit.MINUTES.toMillis(25);
        assertEquals("3h 25m", TimeFormatter.formatSummary(hoursAndMinutes));

        long fullDays = TimeUnit.DAYS.toMillis(10) + TimeUnit.HOURS.toMillis(2) + TimeUnit.MINUTES.toMillis(45);
        assertEquals("10d 2h 45m", TimeFormatter.formatSummary(fullDays));
    }

    @Test
    public void formatDate_validTimestamp_returnsFormattedString() {
        long timestamp = 1700000000000L; // Nov 14, 2023
        String formatted = TimeFormatter.formatDate(timestamp);
        assertNotNull(formatted);
        assertEquals("Never", TimeFormatter.formatDate(0));
    }
}
