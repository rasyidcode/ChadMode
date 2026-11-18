package com.rasyidcode.chadmode.ui.analytics;

import com.rasyidcode.chadmode.data.local.entity.RelapseRecord;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DangerZoneCalculatorTest {

    @Test
    public void compute_emptyOrNullList_returnsGracefulFallbacks() {
        DangerZoneCalculator.DangerZoneStats nullStats = DangerZoneCalculator.compute(null);
        assertNotNull(nullStats);
        assertEquals(0, nullStats.getTotalRelapses());
        assertEquals(0, nullStats.getAverageStreakMs());
        assertEquals("None", nullStats.getTopTrigger());

        DangerZoneCalculator.DangerZoneStats emptyStats = DangerZoneCalculator.compute(Collections.emptyList());
        assertEquals(0, emptyStats.getTotalRelapses());
    }

    @Test
    public void compute_lateNightRecords_identifiesLateNightAsPeakDanger() {
        List<RelapseRecord> records = new ArrayList<>();

        Calendar cal = Calendar.getInstance();

        // 3 Late Night records (02:00 AM)
        cal.set(Calendar.HOUR_OF_DAY, 2);
        cal.set(Calendar.MINUTE, 30);
        long lateNightTime1 = cal.getTimeInMillis();
        long lateNightTime2 = cal.getTimeInMillis() - TimeUnit.DAYS.toMillis(7);
        long lateNightTime3 = cal.getTimeInMillis() - TimeUnit.DAYS.toMillis(14);

        // 1 Afternoon record (14:00)
        cal.set(Calendar.HOUR_OF_DAY, 14);
        long afternoonTime = cal.getTimeInMillis() - TimeUnit.DAYS.toMillis(3);

        records.add(new RelapseRecord(lateNightTime1, TimeUnit.DAYS.toMillis(3), "Late Night", "Bedroom", 3, "peeked"));
        records.add(new RelapseRecord(lateNightTime2, TimeUnit.DAYS.toMillis(5), "Late Night", "Bedroom", 3, "tired"));
        records.add(new RelapseRecord(lateNightTime3, TimeUnit.DAYS.toMillis(2), "Boredom", "Couch", 3, "bored"));
        records.add(new RelapseRecord(afternoonTime, TimeUnit.DAYS.toMillis(10), "Stress", "Alone at Desk", 3, "stressed"));

        DangerZoneCalculator.DangerZoneStats stats = DangerZoneCalculator.compute(records);

        assertEquals(4, stats.getTotalRelapses());
        assertTrue(stats.getPeakDangerPeriod().contains("Late Night"));
        assertTrue(stats.getPeakDangerPeriod().contains("75%"));

        assertEquals("Late Night", stats.getTopTrigger());
        assertEquals("Bedroom", stats.getTopLocation());
        assertEquals(TimeUnit.DAYS.toMillis(10), stats.getLongestStreakMs());
        assertEquals(TimeUnit.DAYS.toMillis(5), stats.getAverageStreakMs());
    }
}
