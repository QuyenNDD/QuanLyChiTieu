package com.example.quanlychitieu.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlychitieu.R;
import com.example.quanlychitieu.model.TransactionWithCategory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CalendarTransactionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnTransactionClickListener {
        void onTransactionClick(TransactionWithCategory transaction);
    }

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private final List<Object> displayItems = new ArrayList<>();
    private final OnTransactionClickListener listener;

    public CalendarTransactionAdapter(OnTransactionClickListener listener) {
        this.listener = listener;
    }

    public void setData(Map<String, List<TransactionWithCategory>> groupedMap) {
        displayItems.clear();

        if (groupedMap != null && !groupedMap.isEmpty()) {
            for (Map.Entry<String, List<TransactionWithCategory>> entry : groupedMap.entrySet()) {
                String date = entry.getKey();
                List<TransactionWithCategory> transactions = entry.getValue();

                double dayTotal = 0;
                for (TransactionWithCategory item : transactions) {
                    if ("EXPENSE".equalsIgnoreCase(item.getType())) {
                        dayTotal -= item.getAmount();
                    } else {
                        dayTotal += item.getAmount();
                    }
                }

                displayItems.add(new HeaderItem(date, dayTotal));
                displayItems.addAll(transactions);
            }
        }

        notifyDataSetChanged();
    }

    public int findHeaderPositionByDate(String date) {
        for (int i = 0; i < displayItems.size(); i++) {
            Object item = displayItems.get(i);
            if (item instanceof HeaderItem) {
                HeaderItem headerItem = (HeaderItem) item;
                if (headerItem.date.equals(date)) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public int getItemViewType(int position) {
        return displayItems.get(position) instanceof HeaderItem ? TYPE_HEADER : TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return displayItems.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == TYPE_HEADER) {
            View view = inflater.inflate(R.layout.item_calendar_transaction_group, parent, false);
            return new HeaderViewHolder(view);
        }

        View view = inflater.inflate(R.layout.item_calendar_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = displayItems.get(position);

        if (holder instanceof HeaderViewHolder) {
            bindHeader((HeaderViewHolder) holder, (HeaderItem) item);
        } else if (holder instanceof TransactionViewHolder) {
            bindTransaction((TransactionViewHolder) holder, (TransactionWithCategory) item);
        }
    }

    private void bindHeader(@NonNull HeaderViewHolder holder, @NonNull HeaderItem item) {
        holder.tvGroupDate.setText(formatHeaderDate(item.date));
        holder.tvGroupTotal.setText(formatSignedMoney(item.total));
    }

    private void bindTransaction(@NonNull TransactionViewHolder holder, @NonNull TransactionWithCategory item) {
        holder.tvCategoryName.setText(item.getCategoryName());

        String note = item.getNote();
        if (note != null && !note.trim().isEmpty()) {
            holder.tvNote.setVisibility(View.VISIBLE);
            holder.tvNote.setText("(" + note + ")");
        } else {
            holder.tvNote.setVisibility(View.GONE);
        }

        holder.tvAmount.setText(formatMoney(item.getAmount()) + "đ");

        int iconResId = holder.itemView.getContext()
                .getResources()
                .getIdentifier(
                        item.getCategoryIcon(),
                        "drawable",
                        holder.itemView.getContext().getPackageName()
                );

        if (iconResId != 0) {
            holder.imgCategoryIcon.setImageResource(iconResId);
        } else {
            holder.imgCategoryIcon.setImageResource(R.drawable.ic_cat_deleted);
        }

        if ("Không có".equals(item.getCategoryName())) {
            holder.imgCategoryIcon.setColorFilter(android.graphics.Color.parseColor("#BDBDBD"));
        } else {
            holder.imgCategoryIcon.setColorFilter(item.getCategoryColor());
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTransactionClick(item);
            }
        });
    }

    private String formatHeaderDate(String yyyyMMdd) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(yyyyMMdd);
            if (date == null) return yyyyMMdd;

            String dateText = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date);
            String dayText = new SimpleDateFormat("u", Locale.getDefault()).format(date);
            return dateText + " (Th " + dayText + ")";
        } catch (Exception e) {
            return yyyyMMdd;
        }
    }

    private String formatMoney(double amount) {
        return String.format(Locale.getDefault(), "%,.0f", amount);
    }

    private String formatSignedMoney(double amount) {
        String sign = amount >= 0 ? "+" : "-";
        return sign + String.format(Locale.getDefault(), "%,.0fđ", Math.abs(amount));
    }

    static class HeaderItem {
        String date;
        double total;

        HeaderItem(String date, double total) {
            this.date = date;
            this.total = total;
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView tvGroupDate;
        TextView tvGroupTotal;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGroupDate = itemView.findViewById(R.id.tvGroupDate);
            tvGroupTotal = itemView.findViewById(R.id.tvGroupTotal);
        }
    }

    static class TransactionViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCategoryIcon;
        TextView tvCategoryName;
        TextView tvNote;
        TextView tvAmount;
        ImageView ivArrow;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCategoryIcon = itemView.findViewById(R.id.imgCategoryIcon);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            tvNote = itemView.findViewById(R.id.tvNote);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            ivArrow = itemView.findViewById(R.id.ivArrow);
        }
    }
}