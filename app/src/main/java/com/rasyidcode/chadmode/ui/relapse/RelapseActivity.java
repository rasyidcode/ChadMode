package com.rasyidcode.chadmode.ui.relapse;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.chip.Chip;
import com.rasyidcode.chadmode.R;
import com.rasyidcode.chadmode.data.local.AppDatabase;
import com.rasyidcode.chadmode.data.local.entity.RelapseRecord;
import com.rasyidcode.chadmode.data.pref.PreferenceManager;
import com.rasyidcode.chadmode.databinding.ActivityRelapseBinding;
import com.rasyidcode.chadmode.util.AppExecutors;
import com.rasyidcode.chadmode.util.TimeFormatter;

/**
 * Honest Post-Relapse Reflection Activity.
 * Replaces guilt with scientific self-reflection, logging triggers into local Room DB.
 */
public class RelapseActivity extends AppCompatActivity {

    private ActivityRelapseBinding binding;
    private PreferenceManager preferenceManager;
    private AppDatabase database;
    private AppExecutors appExecutors;

    private long streakElapsedMs = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());

        binding = ActivityRelapseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Apply Edge-to-Edge System Bar Insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.rootRelapseScrollView, (view, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(insets.left, insets.top, insets.right, insets.bottom);
            return windowInsets;
        });

        preferenceManager = PreferenceManager.getInstance(this);
        database = AppDatabase.getInstance(this);
        appExecutors = AppExecutors.getInstance();

        calculateEndingStreak();
        setupDefaults();
        setupClickListeners();
    }

    private void calculateEndingStreak() {
        long startTime = preferenceManager.getStreakStartTime();
        if (startTime > 0) {
            streakElapsedMs = Math.max(0, System.currentTimeMillis() - startTime);
        }
        binding.tvStreakEndingDuration.setText(TimeFormatter.formatSummary(streakElapsedMs));
    }

    private void setupDefaults() {
        // Pre-select first chips as standard defaults
        binding.chipGroupTriggers.check(R.id.chipLateNight);
        binding.chipGroupLocations.check(R.id.chipBedroom);
    }

    private void setupClickListeners() {
        binding.btnCloseRelapse.setOnClickListener(v -> finish());
        binding.btnCancelRelapse.setOnClickListener(v -> finish());

        binding.btnSaveAndReset.setOnClickListener(v -> saveReflectionAndReset());
    }

    private void saveReflectionAndReset() {
        int selectedTriggerId = binding.chipGroupTriggers.getCheckedChipId();
        int selectedLocationId = binding.chipGroupLocations.getCheckedChipId();

        Chip triggerChip = findViewById(selectedTriggerId);
        Chip locationChip = findViewById(selectedLocationId);

        String trigger = triggerChip != null ? triggerChip.getText().toString() : "Unknown Trigger";
        String location = locationChip != null ? locationChip.getText().toString() : "Unknown Location";

        String brainsLie = "";
        if (binding.etBrainsLie.getText() != null) {
            brainsLie = binding.etBrainsLie.getText().toString().trim();
        }

        long resetTime = System.currentTimeMillis();
        RelapseRecord record = new RelapseRecord(
                resetTime,
                streakElapsedMs,
                trigger,
                location,
                3, // Baseline reflection mood rating
                brainsLie
        );

        // Execute persistence off the main UI thread
        appExecutors.diskIO().execute(() -> {
            database.relapseDao().insert(record);
            preferenceManager.resetStreak(resetTime);

            appExecutors.mainThread().execute(() -> {
                Toast.makeText(this, "Reflection saved. Resetting timer.", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }
}
