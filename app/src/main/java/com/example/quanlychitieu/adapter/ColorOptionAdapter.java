package com.example.quanlychitieu.adapter;

import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlychitieu.R;

import java.util.List;

public class ColorOptionAdapter extends RecyclerView.Adapter<ColorOptionAdapter.ColorViewHolder> {

    public interface OnColorSelectedListener {
        void onColorSelected(int colorValue);
    }

    private final List<Integer> colorList;
    private final OnColorSelectedListener listener;
    private int selectedPosition = 0;

    public ColorOptionAdapter(List<Integer> colorList, int selectedColor, OnColorSelectedListener listener) {
        this.colorList = colorList;
        this.listener = listener;

        int index = colorList.indexOf(selectedColor);
        if (index >= 0) {
            selectedPosition = index;
        }
    }

    @NonNull
    @Override
    public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_color_option, parent, false);
        return new ColorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorViewHolder holder, int position) {
        int color = colorList.get(position);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(8f);
        holder.viewColor.setBackground(drawable);

        if (position == selectedPosition) {
            holder.container.setBackgroundResource(R.drawable.bg_color_option_selected);
        } else {
            holder.container.setBackgroundResource(R.drawable.bg_icon_option_normal);
        }

        holder.itemView.setOnClickListener(v -> {
            int oldPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition();

            notifyItemChanged(oldPosition);
            notifyItemChanged(selectedPosition);

            if (listener != null) {
                listener.onColorSelected(color);
            }
        });
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }

    static class ColorViewHolder extends RecyclerView.ViewHolder {
        FrameLayout container;
        View viewColor;

        public ColorViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.layoutColorItem);
            viewColor = itemView.findViewById(R.id.viewColor);
        }
    }
}