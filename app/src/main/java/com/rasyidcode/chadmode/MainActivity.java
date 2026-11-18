package com.rasyidcode.chadmode;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.rasyidcode.chadmode.databinding.ActivityMainBinding;
import com.rasyidcode.chadmode.ui.emergency.PanicActivity;
import com.rasyidcode.chadmode.ui.main.MainViewModel;
import com.rasyidcode.chadmode.ui.main.RankInfo;
import com.rasyidcode.chadmode.ui.main.StreakUiState;
import com.rasyidcode.chadmode.util.TimeFormatter;

import java.util.Locale;

/**
 * Main Dashboard Activity.
 * Renders real-time streak engine and gamified GigaChad progression.
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainViewModel viewModel;

    private final Handler tickerHandler = new Handler(Looper.getMainLooper());
    private final Runnable tickerRunnable = new Runnable() {
        @Override
        public void run() {
            if (viewModel != null) {
                viewModel.tick();
            }
            tickerHandler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Apply Edge-to-Edge System Bar Insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.rootScrollView, (view, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(insets.left, insets.top, insets.right, insets.bottom);
            return windowInsets;
        });

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        setupObservers();
        setupClickListeners();
    }

    private void setupObservers() {
        viewModel.getStreakState().observe(this, this::renderStreakState);
    }

    private void renderStreakState(StreakUiState state) {
        if (state == null) {
            return;
        }

        TimeFormatter.StreakDuration duration = state.getDuration();
        RankInfo rank = state.getRankInfo();

        // 1. Hero Streak Days & Circular Ring
        binding.tvStreakDays.setText(duration.getDaysFormatted());
        binding.progressStreakRing.setProgress(rank.getProgressPercent());

        // 2. Real-time Ticker Boxes (Hours, Minutes, Seconds)
        binding.tvHours.setText(duration.getHoursFormatted());
        binding.tvMinutes.setText(duration.getMinutesFormatted());
        binding.tvSeconds.setText(duration.getSecondsFormatted());

        // 3. Rank Card
        binding.tvRankBadge.setText(rank.getBadgeEmoji());
        binding.tvRankTitle.setText(rank.getTitle());
        binding.tvRankDesc.setText(rank.getDescription());

        // 4. Milestone Progress Bar
        if (rank.isMaxRank()) {
            binding.tvMilestoneTarget.setText(R.string.rank_day_90_title);
            binding.tvMilestonePercent.setText(R.string.label_days);
            binding.progressMilestone.setProgress(100);
        } else {
            String targetText = String.format(Locale.US, "Day %d", rank.getNextMilestoneDays());
            binding.tvMilestoneTarget.setText(targetText);
            binding.tvMilestonePercent.setText(String.format(Locale.US, "%d%%", rank.getProgressPercent()));
            binding.progressMilestone.setProgress(rank.getProgressPercent());
        }

        // 5. Stats
        binding.tvBestStreak.setText(TimeFormatter.formatSummary(state.getLongestStreakMs()));
        binding.tvStartedOn.setText(TimeFormatter.formatDate(state.getStartTimeMs()));
    }

    private void setupClickListeners() {
        // Reset streak confirmation dialog
        binding.btnResetStreak.setOnClickListener(v -> showResetConfirmationDialog());

        // Emergency Panic Shield actions
        binding.btnPanicShield.setOnClickListener(v -> openPanicShield());
        binding.btnQuickShield.setOnClickListener(v -> openPanicShield());
    }

    private void openPanicShield() {
        Intent intent = new Intent(this, PanicActivity.class);
        startActivity(intent);
    }

    private void showResetConfirmationDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.dialog_reset_title)
                .setMessage(R.string.dialog_reset_message)
                .setPositiveButton(R.string.btn_confirm_reset, (dialog, which) -> {
                    viewModel.resetStreak();
                })
                .setNegativeButton(R.string.btn_cancel, (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.refresh();
        tickerHandler.removeCallbacks(tickerRunnable);
        tickerHandler.post(tickerRunnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop ticker immediately to guarantee zero battery drain when out of view
        tickerHandler.removeCallbacks(tickerRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tickerHandler.removeCallbacks(tickerRunnable);
    }
}
