package com.example.quanlychitieu.adapter;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlychitieu.R;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class ColorPickerAdapter extends RecyclerView.Adapter<ColorPickerAdapter.ColorViewHolder> {

    public interface OnColorClickListener {
        void onColorClick(int color);
    }

    private final List<Integer> colorList;
    private final OnColorClickListener listener;
    private int selectedColor = Color.TRANSPARENT;

    public ColorPickerAdapter(List<Integer> colorList, OnColorClickListener listener) {
        this.colorList = colorList;
        this.listener = listener;
    }

    public void setSelectedColor(int selectedColor) {
        this.selectedColor = selectedColor;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_color_picker, parent, false);
        return new ColorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorViewHolder holder, int position) {
        int color = colorList.get(position);

        GradientDrawable fill = new GradientDrawable();
        fill.setCornerRadius(10f);
        fill.setColor(color);
        holder.viewColor.setBackground(fill);

        boolean isSelected = color == selectedColor;
        holder.cardColor.setStrokeWidth(isSelected ? 3 : 1);
        holder.cardColor.setStrokeColor(isSelected ? Color.WHITE : Color.parseColor("#2A2A2A"));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onColorClick(color);
            }
        });
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }

    static class ColorViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardColor;
        View viewColor;

        public ColorViewHolder(@NonNull View itemView) {
            super(itemView);
            cardColor = itemView.findViewById(R.id.cardColor);
            viewColor = itemView.findViewById(R.id.viewColor);
        }
    }
}