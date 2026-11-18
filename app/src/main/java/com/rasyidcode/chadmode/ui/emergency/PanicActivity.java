package com.rasyidcode.chadmode.ui.emergency;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.rasyidcode.chadmode.R;
import com.rasyidcode.chadmode.databinding.ActivityPanicBinding;

/**
 * Emergency Panic Shield Activity.
 * Provides high-friction craving intervention via 4-4-4-4 Box Breathing
 * and the 15-Minute Urge Surfer timer.
 */
public class PanicActivity extends AppCompatActivity {

    private ActivityPanicBinding binding;
    private EmergencyViewModel viewModel;

    private final Handler urgeTickerHandler = new Handler(Looper.getMainLooper());
    private final Runnable urgeTickerRunnable = new Runnable() {
        @Override
        public void run() {
            if (viewModel != null) {
                viewModel.tickUrgeSurfer();
            }
            urgeTickerHandler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());

        binding = ActivityPanicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Apply Edge-to-Edge System Bar Insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.rootPanicScrollView, (view, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(insets.left, insets.top, insets.right, insets.bottom);
            return windowInsets;
        });

        viewModel = new ViewModelProvider(this).get(EmergencyViewModel.class);

        setupBreathingVisualizer();
        setupUrgeSurfer();
        setupModeToggle();
        setupRealityCheck();
        setupActions();

        // Auto-start breathing immediately on shield activation for instant calming
        binding.breathingVisualizer.start();
        binding.btnBreathingToggle.setText(R.string.btn_pause);
    }

    private void setupBreathingVisualizer() {
        binding.breathingVisualizer.setListener((stage, cycle) -> {
            binding.tvBreathingDesc.setText(stage.getDescription());
        });

        binding.btnBreathingToggle.setOnClickListener(v -> {
            if (binding.breathingVisualizer.isRunning()) {
                binding.breathingVisualizer.pause();
                binding.btnBreathingToggle.setText(R.string.btn_start);
            } else {
                binding.breathingVisualizer.start();
                binding.btnBreathingToggle.setText(R.string.btn_pause);
            }
        });

        binding.btnBreathingReset.setOnClickListener(v -> {
            binding.breathingVisualizer.reset();
            binding.btnBreathingToggle.setText(R.string.btn_start);
            binding.tvBreathingDesc.setText(BreathingStage.INHALE.getDescription());
        });
    }

    private void setupUrgeSurfer() {
        viewModel.getUrgeRemainingMillis().observe(this, remainingMs -> {
            binding.tvUrgeTimer.setText(UrgeSurferEngine.formatRemaining(remainingMs));
        });

        viewModel.getUrgeProgress().observe(this, progress -> {
            binding.progressUrgeWave.setProgress(progress);
        });

        viewModel.getUrgeStage().observe(this, stage -> {
            binding.tvUrgeStageTitle.setText(stage.getTitle());
            binding.tvUrgeStageDesc.setText(stage.getDescription());
        });

        viewModel.getIsUrgeRunning().observe(this, isRunning -> {
            if (isRunning) {
                binding.btnUrgeToggle.setText(R.string.btn_pause);
                urgeTickerHandler.removeCallbacks(urgeTickerRunnable);
                urgeTickerHandler.post(urgeTickerRunnable);
            } else {
                binding.btnUrgeToggle.setText(R.string.btn_start);
                urgeTickerHandler.removeCallbacks(urgeTickerRunnable);
            }
        });

        binding.btnUrgeToggle.setOnClickListener(v -> {
            Boolean running = viewModel.getIsUrgeRunning().getValue();
            if (running != null && running) {
                viewModel.pauseUrgeSurfer();
            } else {
                viewModel.startUrgeSurfer();
            }
        });

        binding.btnUrgeReset.setOnClickListener(v -> viewModel.resetUrgeSurfer());
    }

    private void setupModeToggle() {
        binding.toggleModeGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (!isChecked) return;

            if (checkedId == R.id.btnTabBreathing) {
                binding.layoutBreathingContainer.setVisibility(View.VISIBLE);
                binding.layoutUrgeSurferContainer.setVisibility(View.GONE);
                viewModel.setActiveTab(0);
            } else if (checkedId == R.id.btnTabUrgeSurfer) {
                binding.layoutBreathingContainer.setVisibility(View.GONE);
                binding.layoutUrgeSurferContainer.setVisibility(View.VISIBLE);
                viewModel.setActiveTab(1);
            }
        });

        Integer currentTab = viewModel.getActiveTab().getValue();
        if (currentTab != null && currentTab == 1) {
            binding.toggleModeGroup.check(R.id.btnTabUrgeSurfer);
        } else {
            binding.toggleModeGroup.check(R.id.btnTabBreathing);
        }
    }

    private void setupRealityCheck() {
        viewModel.getRealityCheckQuote().observe(this, quote -> {
            binding.tvRealityCheckQuote.setText(quote);
        });

        View.OnClickListener nextQuoteListener = v -> viewModel.nextRealityCheck();
        binding.btnNextRealityCheck.setOnClickListener(nextQuoteListener);
        binding.btnShuffleRealityCheck.setOnClickListener(nextQuoteListener);
    }

    private void setupActions() {
        binding.btnClosePanic.setOnClickListener(v -> finish());

        binding.btnSurvivedUrge.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.congrats_urge_conquered_title)
                    .setMessage(R.string.congrats_urge_conquered_message)
                    .setPositiveButton(R.string.btn_dismiss, (dialog, which) -> {
                        dialog.dismiss();
                        finish();
                    })
                    .show();
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        urgeTickerHandler.removeCallbacks(urgeTickerRunnable);
        binding.breathingVisualizer.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        urgeTickerHandler.removeCallbacks(urgeTickerRunnable);
    }
}
