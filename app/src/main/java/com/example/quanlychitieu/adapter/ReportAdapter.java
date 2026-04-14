package com.example.quanlychitieu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.quanlychitieu.R;
import com.example.quanlychitieu.model.ReportItem;
import java.util.List;

public class ReportAdapter extends ArrayAdapter<ReportItem> {
    private Context context;
    private int resource;

    public ReportAdapter(@NonNull Context context, int resource, @NonNull List<ReportItem> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ReportItem item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        View viewColor = convertView.findViewById(R.id.viewColor);
        TextView tvName = convertView.findViewById(R.id.tvCategoryName);
        TextView tvAmount = convertView.findViewById(R.id.tvAmount);
        TextView tvPercent = convertView.findViewById(R.id.tvPercentage);

        if (item != null) {
            tvName.setText(item.getCategoryName());
            tvAmount.setText(String.format("%,.0fđ", item.getAmount()));
            tvPercent.setText(String.format("%.1f%%", item.getPercentage()));
            viewColor.setBackgroundColor(item.getColor());
        }

        return convertView;
    }
}