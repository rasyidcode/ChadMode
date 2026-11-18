package com.rasyidcode.chadmode.ui.emergency;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/**
 * ViewModel for the Emergency Panic Shield screen.
 * Survives configuration changes and maintains Urge Surfing timer state.
 */
public class EmergencyViewModel extends AndroidViewModel {

    private final RealityCheckProvider realityCheckProvider;
    private int currentQuoteIndex = 0;

    private final MutableLiveData<String> realityCheckQuote = new MutableLiveData<>();
    private final MutableLiveData<Long> urgeRemainingMillis = new MutableLiveData<>(UrgeSurferEngine.TOTAL_SURF_MILLIS);
    private final MutableLiveData<Boolean> isUrgeRunning = new MutableLiveData<>(false);
    private final MutableLiveData<UrgeWaveStage> urgeStage = new MutableLiveData<>(UrgeWaveStage.RISING);
    private final MutableLiveData<Integer> urgeProgress = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> activeTab = new MutableLiveData<>(0); // 0 = Breathing, 1 = Surfer

    public EmergencyViewModel(@NonNull Application application) {
        super(application);
        this.realityCheckProvider = new RealityCheckProvider();
        realityCheckQuote.setValue(realityCheckProvider.getRandomQuote());
    }

    public LiveData<String> getRealityCheckQuote() {
        return realityCheckQuote;
    }

    public LiveData<Long> getUrgeRemainingMillis() {
        return urgeRemainingMillis;
    }

    public LiveData<Boolean> getIsUrgeRunning() {
        return isUrgeRunning;
    }

    public LiveData<UrgeWaveStage> getUrgeStage() {
        return urgeStage;
    }

    public LiveData<Integer> getUrgeProgress() {
        return urgeProgress;
    }

    public LiveData<Integer> getActiveTab() {
        return activeTab;
    }

    public void setActiveTab(int tabIndex) {
        activeTab.setValue(tabIndex);
    }

    public void nextRealityCheck() {
        currentQuoteIndex = realityCheckProvider.getNextIndex(currentQuoteIndex);
        realityCheckQuote.setValue(realityCheckProvider.getQuote(currentQuoteIndex));
    }

    public void startUrgeSurfer() {
        isUrgeRunning.setValue(true);
    }

    public void pauseUrgeSurfer() {
        isUrgeRunning.setValue(false);
    }

    public void resetUrgeSurfer() {
        isUrgeRunning.setValue(false);
        urgeRemainingMillis.setValue(UrgeSurferEngine.TOTAL_SURF_MILLIS);
        urgeStage.setValue(UrgeWaveStage.RISING);
        urgeProgress.setValue(0);
    }

    /**
     * Ticks the 15-minute countdown every 1 second.
     */
    public void tickUrgeSurfer() {
        Boolean running = isUrgeRunning.getValue();
        if (running == null || !running) return;

        Long current = urgeRemainingMillis.getValue();
        if (current == null) current = UrgeSurferEngine.TOTAL_SURF_MILLIS;

        long next = Math.max(0, current - 1000);
        urgeRemainingMillis.setValue(next);

        long elapsed = UrgeSurferEngine.TOTAL_SURF_MILLIS - next;
        urgeStage.setValue(UrgeSurferEngine.getStage(elapsed));
        urgeProgress.setValue(UrgeSurferEngine.getProgressPercent(elapsed));

        if (next <= 0) {
            isUrgeRunning.setValue(false);
        }
    }
}
