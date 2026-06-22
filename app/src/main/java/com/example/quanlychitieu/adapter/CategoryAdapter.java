package com.example.quanlychitieu.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlychitieu.R;
import com.example.quanlychitieu.model.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category, int position);
    }

    private final List<Category> categoryList;
    private final OnCategoryClickListener listener;
    private int selectedPosition = 0;

    public CategoryAdapter(List<Category> categoryList, OnCategoryClickListener listener) {
        this.categoryList = categoryList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.tvCategoryName.setText(category.getName());

        bindCategoryIcon(holder, category);

        if (position == selectedPosition) {
            holder.itemView.setBackgroundResource(R.drawable.bg_category_selected);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.bg_category_normal);
        }

        holder.itemView.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition == RecyclerView.NO_POSITION) {
                return;
            }

            int oldPosition = selectedPosition;
            selectedPosition = adapterPosition;

            notifyItemChanged(oldPosition);
            notifyItemChanged(selectedPosition);

            if (listener != null) {
                listener.onCategoryClick(category, adapterPosition);
            }
        });
    }

    private void bindCategoryIcon(@NonNull CategoryViewHolder holder, @NonNull Category category) {
        if ("Chỉnh sửa".equalsIgnoreCase(category.getName())) {
            holder.ivCategoryIcon.setImageResource(R.drawable.arrow);
            holder.ivCategoryIcon.clearColorFilter();
            return;
        }

        String iconName = category.getIcon();

        if (iconName == null || iconName.trim().isEmpty()) {
            holder.ivCategoryIcon.setImageResource(R.drawable.ic_cat_food);
            holder.ivCategoryIcon.clearColorFilter();
            return;
        }

        int iconResId = holder.itemView.getContext()
                .getResources()
                .getIdentifier(iconName, "drawable",
                        holder.itemView.getContext().getPackageName());

        if (iconResId != 0) {
            holder.ivCategoryIcon.setImageResource(iconResId);
        } else {
            holder.ivCategoryIcon.setImageResource(R.drawable.ic_cat_food);
        }

        holder.ivCategoryIcon.setColorFilter(category.getColorValue());
    }

    @Override
    public int getItemCount() {
        return categoryList == null ? 0 : categoryList.size();
    }

    public void clearSelection() {
        int oldPosition = selectedPosition;
        selectedPosition = RecyclerView.NO_POSITION;
        if (oldPosition != RecyclerView.NO_POSITION) {
            notifyItemChanged(oldPosition);
        }
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public Category getSelectedCategory() {
        if (categoryList == null || categoryList.isEmpty()) {
            return null;
        }
        if (selectedPosition < 0 || selectedPosition >= categoryList.size()) {
            return null;
        }
        return categoryList.get(selectedPosition);
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCategoryIcon;
        TextView tvCategoryName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCategoryIcon = itemView.findViewById(R.id.ivCategoryIcon);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
        }
    }
    public void setSelectedCategoryId(int categoryId) {
        int oldPosition = selectedPosition;
        selectedPosition = -1;

        for (int i = 0; i < categoryList.size(); i++) {
            if (categoryList.get(i).getId() == categoryId) {
                selectedPosition = i;
                break;
            }
        }

        if (oldPosition >= 0) {
            notifyItemChanged(oldPosition);
        }
        if (selectedPosition >= 0) {
            notifyItemChanged(selectedPosition);
        }
    }
}