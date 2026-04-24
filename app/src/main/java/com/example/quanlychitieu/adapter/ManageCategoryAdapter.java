package com.example.quanlychitieu.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlychitieu.R;
import com.example.quanlychitieu.model.Category;

import java.util.ArrayList;
import java.util.List;

public class ManageCategoryAdapter extends RecyclerView.Adapter<ManageCategoryAdapter.CategoryViewHolder> {

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category, int position);
        void onDeleteClick(Category category, int position);
    }

    private final List<Category> categoryList = new ArrayList<>();
    private final OnCategoryClickListener listener;
    private boolean isEditMode = false;

    public ManageCategoryAdapter(OnCategoryClickListener listener) {
        this.listener = listener;
    }

    public void setData(List<Category> data) {
        categoryList.clear();
        if (data != null) {
            categoryList.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void setEditMode(boolean editMode) {
        isEditMode = editMode;
        notifyDataSetChanged();
    }

    public boolean isEditMode() {
        return isEditMode;
    }

    public void removeItem(int position) {
        if (position >= 0 && position < categoryList.size()) {
            categoryList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, categoryList.size());
        }
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_manage_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);

        holder.txtCategoryName.setText(category.getName());

        // Có thể thay bằng icon thật sau này
        holder.viewFakeIcon.setVisibility(View.VISIBLE);

        if (isEditMode) {
            holder.imgDelete.setVisibility(View.VISIBLE);
            holder.imgDrag.setVisibility(View.VISIBLE);
            holder.imgArrow.setVisibility(View.GONE);
        } else {
            holder.imgDelete.setVisibility(View.GONE);
            holder.imgDrag.setVisibility(View.GONE);
            holder.imgArrow.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (isEditMode) {
                return;
            }

            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition == RecyclerView.NO_POSITION) return;

            if (listener != null) {
                listener.onCategoryClick(category, adapterPosition);
            }
        });

        holder.imgDelete.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition == RecyclerView.NO_POSITION) return;

            if (listener != null) {
                listener.onDeleteClick(category, adapterPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        View viewFakeIcon;
        TextView txtCategoryName;
        ImageView imgArrow;
        ImageView imgDelete;
        ImageView imgDrag;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            viewFakeIcon = itemView.findViewById(R.id.viewFakeIcon);
            txtCategoryName = itemView.findViewById(R.id.txtCategoryName);
            imgArrow = itemView.findViewById(R.id.imgArrow);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            imgDrag = itemView.findViewById(R.id.imgDrag);
        }
    }
}