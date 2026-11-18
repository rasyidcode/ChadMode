package com.rasyidcode.chadmode.ui.emergency;

/**
 * Stages of the 4-4-4-4 Box Breathing protocol.
 */
public enum BreathingStage {
    INHALE("INHALE", "Fill lungs deeply with calm air", 4000L),
    HOLD_IN("HOLD", "Keep stillness in mind and chest", 4000L),
    EXHALE("EXHALE", "Slowly release tension and cravings", 4000L),
    HOLD_OUT("REST", "Empty baseline. Mind reset.", 4000L);

    private final String label;
    private final String description;
    private final long durationMillis;

    BreathingStage(String label, String description, long durationMillis) {
        this.label = label;
        this.description = description;
        this.durationMillis = durationMillis;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

    public long getDurationMillis() {
        return durationMillis;
    }

    public BreathingStage getNext() {
        switch (this) {
            case INHALE:
                return HOLD_IN;
            case HOLD_IN:
                return EXHALE;
            case EXHALE:
                return HOLD_OUT;
            case HOLD_OUT:
            default:
                return INHALE;
        }
    }
}
