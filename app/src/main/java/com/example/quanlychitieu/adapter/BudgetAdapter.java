package com.example.quanlychitieu.adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlychitieu.R;
import com.example.quanlychitieu.model.Budget;

import java.text.DecimalFormat;
import java.util.List;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder> {

    private List<Budget> budgetList;
    private final DecimalFormat moneyFormat = new DecimalFormat("#,### đ");

    // Khai báo các màu sắc chuẩn
    private final int COLOR_SAFE = Color.parseColor("#4CAF50"); // Xanh lục
    private final int COLOR_WARNING = Color.parseColor("#FF9800"); // Vàng cam
    private final int COLOR_ALERT = Color.parseColor("#F44336"); // Đỏ
    private final int COLOR_TEXT_DEFAULT = Color.parseColor("#555555"); // Xám đậm

    public BudgetAdapter(List<Budget> budgetList) {
        this.budgetList = budgetList;
    }

    @NonNull
    @Override
    public BudgetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_budget, parent, false);
        return new BudgetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetViewHolder holder, int position) {
        Budget budget = budgetList.get(position);
        int percentage = budget.getPercentage();

        // 1. Set text cơ bản
        holder.tvCategoryName.setText(budget.getCategoryName());
        String iconName = budget.getIconName();
        int resId = holder.itemView.getContext().getResources().getIdentifier(
                iconName, "drawable", holder.itemView.getContext().getPackageName());

        // Nếu không tìm thấy, dùng icon mặc định
        if (resId == 0) resId = android.R.drawable.ic_menu_myplaces;

        holder.ivCategoryIcon.setImageResource(resId);
        holder.tvPercentage.setText(percentage + "%");

        String formattedSpent = moneyFormat.format(budget.getSpentAmount());
        String formattedLimit = moneyFormat.format(budget.getTotalLimit());
        holder.tvAmountInfo.setText(formattedSpent + " / " + formattedLimit);

        // 2. Set ProgressBar value (tối đa 100% để thanh không bị lỗi hiển thị)
        holder.pbBudget.setProgress(Math.min(percentage, 100));

        // 3. Xử lý Logic Hiển thị (Visual Feedback)
        if (percentage >= 100) {
            // Mức độ Báo động
            holder.pbBudget.setProgressTintList(ColorStateList.valueOf(COLOR_ALERT));
            holder.tvAmountInfo.setTextColor(COLOR_ALERT);

            holder.tvWarning.setVisibility(View.VISIBLE);
            holder.tvWarning.setText("Vượt hạn mức!");
            holder.tvWarning.setTextColor(COLOR_ALERT);
            holder.tvWarning.setTypeface(null, Typeface.BOLD);

        } else if (percentage >= 70) {
            // Mức độ Chú ý
            holder.pbBudget.setProgressTintList(ColorStateList.valueOf(COLOR_WARNING));
            holder.tvAmountInfo.setTextColor(COLOR_TEXT_DEFAULT);

            holder.tvWarning.setVisibility(View.VISIBLE);
            holder.tvWarning.setText("Sắp đạt giới hạn");
            holder.tvWarning.setTextColor(COLOR_WARNING);
            holder.tvWarning.setTypeface(null, Typeface.NORMAL);

        } else {
            // Mức độ An toàn
            holder.pbBudget.setProgressTintList(ColorStateList.valueOf(COLOR_SAFE));
            holder.tvAmountInfo.setTextColor(COLOR_TEXT_DEFAULT);
            holder.tvWarning.setVisibility(View.GONE); // Ẩn cảnh báo
        }
    }

    @Override
    public int getItemCount() {
        return budgetList != null ? budgetList.size() : 0;
    }

    public static class BudgetViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCategoryIcon;
        TextView tvCategoryName, tvPercentage, tvAmountInfo, tvWarning;
        ProgressBar pbBudget;

        public BudgetViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCategoryIcon = itemView.findViewById(R.id.ivCategoryIcon);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            tvPercentage = itemView.findViewById(R.id.tvPercentage);
            tvAmountInfo = itemView.findViewById(R.id.tvAmountInfo);
            tvWarning = itemView.findViewById(R.id.tvWarning);
            pbBudget = itemView.findViewById(R.id.pbBudget);
        }
    }
}