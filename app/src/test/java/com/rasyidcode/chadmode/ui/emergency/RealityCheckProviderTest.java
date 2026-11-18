package com.rasyidcode.chadmode.ui.emergency;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RealityCheckProviderTest {

    private RealityCheckProvider provider;

    @Before
    public void setUp() {
        provider = new RealityCheckProvider();
    }

    @Test
    public void getQuotes_isNotEmpty() {
        assertTrue(provider.getCount() > 0);
        assertEquals(provider.getCount(), provider.getAllQuotes().size());
    }

    @Test
    public void getRandomQuote_returnsValidString() {
        for (int i = 0; i < 20; i++) {
            String quote = provider.getRandomQuote();
            assertNotNull(quote);
            assertFalse(quote.trim().isEmpty());
        }
    }

    @Test
    public void getNextIndex_wrapsAroundCleanly() {
        int count = provider.getCount();
        int next = provider.getNextIndex(count - 1);
        assertEquals(0, next);

        assertEquals(2, provider.getNextIndex(1));
    }

    @Test
    public void getQuote_outOfBounds_returnsFallback() {
        String quote = provider.getQuote(-5);
        assertNotNull(quote);
        assertEquals(provider.getQuote(0), quote);

        String quoteHigh = provider.getQuote(999);
        assertNotNull(quoteHigh);
        assertEquals(provider.getQuote(0), quoteHigh);
    }
}
