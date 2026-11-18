package com.rasyidcode.chadmode.ui.analytics;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rasyidcode.chadmode.data.local.entity.RelapseRecord;
import com.rasyidcode.chadmode.databinding.ItemRelapseHistoryBinding;
import com.rasyidcode.chadmode.util.TimeFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * RecyclerView adapter for displaying streak relapse history and reflection entries.
 */
public class RelapseHistoryAdapter extends RecyclerView.Adapter<RelapseHistoryAdapter.ViewHolder> {

    private final List<RelapseRecord> records = new ArrayList<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy • hh:mm a", Locale.getDefault());

    public void setRecords(List<RelapseRecord> newRecords) {
        records.clear();
        if (newRecords != null) {
            records.addAll(newRecords);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRelapseHistoryBinding binding = ItemRelapseHistoryBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RelapseRecord record = records.get(position);
        holder.bind(record);
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemRelapseHistoryBinding binding;

        ViewHolder(ItemRelapseHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(RelapseRecord record) {
            binding.tvHistoryDate.setText(dateFormat.format(new Date(record.getTimestampMs())));
            binding.tvHistoryDuration.setText(String.format("Streak: %s", TimeFormatter.formatSummary(record.getStreakDurationMs())));
            binding.tvHistoryTrigger.setText(record.getTriggerCategory());
            binding.tvHistoryLocation.setText(record.getLocationContext());

            if (!TextUtils.isEmpty(record.getJournalNote())) {
                binding.tvHistoryLie.setVisibility(View.VISIBLE);
                binding.tvHistoryLie.setText(String.format("\"%s\"", record.getJournalNote()));
            } else {
                binding.tvHistoryLie.setVisibility(View.GONE);
            }
        }
    }
}
