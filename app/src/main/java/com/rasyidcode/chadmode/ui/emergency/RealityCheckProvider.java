package com.rasyidcode.chadmode.ui.emergency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Provides hard-hitting stoic, scientific, and psychological reality checks
 * to disrupt compulsive craving loops during urge spikes.
 */
public class RealityCheckProvider {

    private static final List<String> REALITY_CHECKS;

    static {
        List<String> list = new ArrayList<>();
        list.add("10 seconds of cheap, artificial dopamine is not worth throwing away your hard-earned discipline.");
        list.add("An urge is just an electrical spike in your nervous system. It has zero power over your physical body unless you consent.");
        list.add("You are not truly craving pleasure; you are running from boredom, stress, or loneliness. Face the actual emotion.");
        list.add("Remember the instant wave of disgust, brain fog, and self-loathing immediately after relapsing. Do not buy that ticket again.");
        list.add("Every time you say NO to an urge, you physically weaken the old neural pathways and reinforce the GigaChad mindset.");
        list.add("A king commands his mind; a slave obeys his impulses. Decide who sits on the throne of your consciousness.");
        list.add("The craving will dissipate whether you surrender or conquer it. Only one choice leaves your self-respect intact.");
        list.add("The pain of self-discipline weighs ounces; the crushing weight of regret weighs tons. Stand your ground.");
        list.add("Close your eyes. Put your phone down. Wait 15 minutes. Watch the wave collapse on its own.");
        list.add("You came here because you were tired of being average. Do not return to the pit you fought to escape.");
        REALITY_CHECKS = Collections.unmodifiableList(list);
    }

    private final Random random = new Random();

    public List<String> getAllQuotes() {
        return REALITY_CHECKS;
    }

    public int getCount() {
        return REALITY_CHECKS.size();
    }

    public String getQuote(int index) {
        if (index < 0 || index >= REALITY_CHECKS.size()) {
            return REALITY_CHECKS.get(0);
        }
        return REALITY_CHECKS.get(index);
    }

    public String getRandomQuote() {
        int index = random.nextInt(REALITY_CHECKS.size());
        return REALITY_CHECKS.get(index);
    }

    public int getNextIndex(int currentIndex) {
        return (currentIndex + 1) % REALITY_CHECKS.size();
    }
}
