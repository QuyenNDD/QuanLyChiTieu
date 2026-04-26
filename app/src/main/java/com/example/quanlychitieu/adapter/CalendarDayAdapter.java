package com.example.quanlychitieu.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlychitieu.R;
import com.example.quanlychitieu.model.CalendarDayItem;

import java.util.List;
import java.util.Locale;

public class CalendarDayAdapter extends RecyclerView.Adapter<CalendarDayAdapter.DayViewHolder> {

    public interface OnDayClickListener {
        void onDayClick(CalendarDayItem dayItem);
        void onDayDoubleClick(CalendarDayItem dayItem);
    }

    private final List<CalendarDayItem> items;
    private final OnDayClickListener listener;

    private String lastClickedDate = null;
    private long lastClickedTime = 0;

    public CalendarDayAdapter(List<CalendarDayItem> items, OnDayClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_calendar_day, parent, false);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        CalendarDayItem item = items.get(position);

        holder.tvDay.setText(String.valueOf(item.getDayOfMonth()));

        if (item.isCurrentMonth()) {
            holder.tvDay.setTextColor(Color.WHITE);
            holder.tvIncomeAmount.setAlpha(1f);
            holder.tvExpenseAmount.setAlpha(1f);
        } else {
            holder.tvDay.setTextColor(Color.parseColor("#666666"));
            holder.tvIncomeAmount.setAlpha(0.35f);
            holder.tvExpenseAmount.setAlpha(0.35f);
        }

        if (item.getIncomeTotal() > 0) {
            holder.tvIncomeAmount.setText(formatMoney(item.getIncomeTotal()));
        } else {
            holder.tvIncomeAmount.setText("");
        }

        if (item.getExpenseTotal() > 0) {
            holder.tvExpenseAmount.setText(formatMoney(item.getExpenseTotal()));
        } else {
            holder.tvExpenseAmount.setText("");
        }

        holder.itemView.setOnClickListener(v -> {
            long currentTime = System.currentTimeMillis();
            String currentDate = item.getDate();

            if (currentDate != null
                    && currentDate.equals(lastClickedDate)
                    && currentTime - lastClickedTime < 300) {

                if (listener != null) {
                    listener.onDayDoubleClick(item);
                }

                lastClickedDate = null;
                lastClickedTime = 0;
            } else {
                if (listener != null) {
                    listener.onDayClick(item);
                }

                lastClickedDate = currentDate;
                lastClickedTime = currentTime;
            }
        });
    }

    private String formatMoney(double amount) {
        return String.format(Locale.getDefault(), "%,.0f", amount);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class DayViewHolder extends RecyclerView.ViewHolder {
        TextView tvDay;
        TextView tvIncomeAmount;
        TextView tvExpenseAmount;

        public DayViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDay = itemView.findViewById(R.id.tvDay);
            tvIncomeAmount = itemView.findViewById(R.id.tvIncomeAmount);
            tvExpenseAmount = itemView.findViewById(R.id.tvExpenseAmount);
        }
    }
}