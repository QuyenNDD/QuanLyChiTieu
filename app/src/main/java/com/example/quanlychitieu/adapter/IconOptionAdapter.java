package com.example.quanlychitieu.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlychitieu.R;
import com.example.quanlychitieu.model.IconOption;

import java.util.List;

public class IconOptionAdapter extends RecyclerView.Adapter<IconOptionAdapter.IconViewHolder> {

    public interface OnIconSelectedListener {
        void onIconSelected(IconOption iconOption);
    }

    private final List<IconOption> iconList;
    private final OnIconSelectedListener listener;
    private int selectedPosition = 0;

    public IconOptionAdapter(List<IconOption> iconList, String selectedIconName, OnIconSelectedListener listener) {
        this.iconList = iconList;
        this.listener = listener;

        if (selectedIconName != null) {
            for (int i = 0; i < iconList.size(); i++) {
                if (selectedIconName.equals(iconList.get(i).getIconName())) {
                    selectedPosition = i;
                    break;
                }
            }
        }
    }

    @NonNull
    @Override
    public IconViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_icon_option, parent, false);
        return new IconViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IconViewHolder holder, int position) {
        IconOption iconOption = iconList.get(position);

        holder.imgIcon.setImageResource(iconOption.getIconResId());

        if (position == selectedPosition) {
            holder.container.setBackgroundResource(R.drawable.bg_icon_option_selected);
            holder.imgIcon.setColorFilter(0xFFFFFFFF);
        } else {
            holder.container.setBackgroundResource(R.drawable.bg_icon_option_normal);
            holder.imgIcon.setColorFilter(0xFFFFFFFF);
        }

        holder.itemView.setOnClickListener(v -> {
            int oldPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition();

            notifyItemChanged(oldPosition);
            notifyItemChanged(selectedPosition);

            if (listener != null) {
                listener.onIconSelected(iconOption);
            }
        });
    }

    @Override
    public int getItemCount() {
        return iconList.size();
    }

    static class IconViewHolder extends RecyclerView.ViewHolder {
        FrameLayout container;
        ImageView imgIcon;

        public IconViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.layoutIconItem);
            imgIcon = itemView.findViewById(R.id.imgIcon);
        }
    }
}