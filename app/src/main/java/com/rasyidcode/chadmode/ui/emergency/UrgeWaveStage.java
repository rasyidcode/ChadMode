package com.rasyidcode.chadmode.ui.emergency;

/**
 * Stages of the 15-Minute Urge Surfing neurochemical wave.
 */
public enum UrgeWaveStage {
    RISING("Wave Rising", "Cravings are intensifying. Breathe and observe the impulse without reacting."),
    CREST("The Crest (Peak Craving)", "Peak craving intensity. Do NOT touch, do NOT peek. Stand like a statue."),
    DESCENT("The Descent", "Chemical dopamine spike is dissolving. Self-control is re-established."),
    CALM("Calm Waters", "The craving wave has collapsed. Your baseline is restored."),
    COMPLETED("Urge Conquered! 🗿", "You mastered the wave without breaking. Self-respect intact.");

    private final String title;
    private final String description;

    UrgeWaveStage(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
