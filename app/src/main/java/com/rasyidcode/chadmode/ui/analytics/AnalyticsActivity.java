package com.rasyidcode.chadmode.ui.analytics;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.rasyidcode.chadmode.data.local.AppDatabase;
import com.rasyidcode.chadmode.databinding.ActivityAnalyticsBinding;

/**
 * Analytics and Danger Zone Activity.
 * Analyzes local Room relapse records to reveal physiological and situational vulnerabilities.
 */
public class AnalyticsActivity extends AppCompatActivity {

    private ActivityAnalyticsBinding binding;
    private RelapseHistoryAdapter adapter;
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());

        binding = ActivityAnalyticsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Apply Edge-to-Edge System Bar Insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.rootAnalyticsScrollView, (view, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(insets.left, insets.top, insets.right, insets.bottom);
            return windowInsets;
        });

        database = AppDatabase.getInstance(this);

        setupRecyclerView();
        setupObservers();
        setupClickListeners();
    }

    private void setupRecyclerView() {
        adapter = new RelapseHistoryAdapter();
        binding.rvRelapseHistory.setLayoutManager(new LinearLayoutManager(this));
        binding.rvRelapseHistory.setAdapter(adapter);
    }

    private void setupObservers() {
        database.relapseDao().getAllRelapses().observe(this, records -> {
            if (records == null || records.isEmpty()) {
                binding.cardEmptyState.setVisibility(View.VISIBLE);
                binding.cardDangerZone.setVisibility(View.GONE);
                binding.rvRelapseHistory.setVisibility(View.GONE);
            } else {
                binding.cardEmptyState.setVisibility(View.GONE);
                binding.cardDangerZone.setVisibility(View.VISIBLE);
                binding.rvRelapseHistory.setVisibility(View.VISIBLE);

                // Compute danger zone statistics
                DangerZoneCalculator.DangerZoneStats stats = DangerZoneCalculator.compute(records);
                binding.tvDangerPeriod.setText(stats.getPeakDangerPeriod());
                binding.tvDangerTrigger.setText(stats.getTopTrigger());
                binding.tvDangerLocation.setText(stats.getTopLocation());

                adapter.setRecords(records);
            }
        });
    }

    private void setupClickListeners() {
        binding.btnCloseAnalytics.setOnClickListener(v -> finish());
    }
}
