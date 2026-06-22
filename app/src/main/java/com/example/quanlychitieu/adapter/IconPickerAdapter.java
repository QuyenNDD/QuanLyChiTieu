package com.example.quanlychitieu.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlychitieu.R;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class IconPickerAdapter extends RecyclerView.Adapter<IconPickerAdapter.IconViewHolder> {

    public interface OnIconClickListener {
        void onIconClick(String icon);
    }

    private final List<String> iconList;
    private final OnIconClickListener listener;
    private String selectedIcon = "";

    public IconPickerAdapter(List<String> iconList, OnIconClickListener listener) {
        this.iconList = iconList;
        this.listener = listener;
    }

    public void setSelectedIcon(String selectedIcon) {
        this.selectedIcon = selectedIcon;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IconViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_icon_picker, parent, false);
        return new IconViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IconViewHolder holder, int position) {
        String icon = iconList.get(position);
        holder.txtIcon.setText(icon);

        boolean isSelected = icon.equals(selectedIcon);
        holder.cardIcon.setStrokeWidth(isSelected ? 3 : 1);
        holder.cardIcon.setStrokeColor(isSelected ? Color.WHITE : Color.parseColor("#2A2A2A"));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onIconClick(icon);
            }
        });
    }

    @Override
    public int getItemCount() {
        return iconList.size();
    }

    static class IconViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardIcon;
        TextView txtIcon;

        public IconViewHolder(@NonNull View itemView) {
            super(itemView);
            cardIcon = itemView.findViewById(R.id.cardIcon);
            txtIcon = itemView.findViewById(R.id.txtIcon);
        }
    }
}