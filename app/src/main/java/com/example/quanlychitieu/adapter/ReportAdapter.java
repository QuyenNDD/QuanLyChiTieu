package com.example.quanlychitieu.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.quanlychitieu.R;
import com.example.quanlychitieu.model.ReportItem;
import java.util.List;

public class ReportAdapter extends ArrayAdapter<ReportItem> {
    private Context context;
    private int resource;
    private List<ReportItem> objects;

    public ReportAdapter(@NonNull Context context, int resource, @NonNull List<ReportItem> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ReportItem item = objects.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        // --- ÁNH XẠ CÁC VIEW THEO XML MỚI ---
        TextView tvDateHeader = convertView.findViewById(R.id.tvDateHeader);
        View viewColor = convertView.findViewById(R.id.viewColor);
        ImageView imgIcon = convertView.findViewById(R.id.imgCategoryIcon);
        TextView tvName = convertView.findViewById(R.id.tvCategoryName);
        TextView tvNote = convertView.findViewById(R.id.tvNote);
        TextView tvAmount = convertView.findViewById(R.id.tvAmount);

        if (item != null) {
            // 1. Logic ẩn hiện Header Ngày (Group by Date)
            // Nếu là mục đầu tiên HOẶC ngày hiện tại khác ngày của mục phía trước
            // Sửa lại logic so sánh ngày để tránh Null
            String currentDate = (item.getDate() != null) ? item.getDate() : "";
            String previousDate = (position > 0 && objects.get(position - 1).getDate() != null)
                    ? objects.get(position - 1).getDate() : "";

            if (position == 0 || !currentDate.equals(previousDate)) {
                tvDateHeader.setVisibility(View.VISIBLE);
                tvDateHeader.setText(currentDate);
            } else {
                tvDateHeader.setVisibility(View.GONE);
            }

            // 2. Hiển thị thông tin chính
            tvName.setText(item.getCategoryName());
            tvAmount.setText(String.format("%,.0fđ", item.getAmount()));

            // Hiển thị ghi chú (note)
            if (item.getNote() != null && !item.getNote().isEmpty()) {
                tvNote.setVisibility(View.VISIBLE);
                tvNote.setText(item.getNote());
            } else {
                tvNote.setVisibility(View.GONE);
            }

            // 3. Hiển thị màu sắc và Icon từ Database
            viewColor.setBackgroundColor(item.getColor());

            if (item.getIcon() != null) {
                android.util.Log.e("IconCheck", "Gia tri icon: " + item.getIcon());
                int resId = context.getResources().getIdentifier(
                        item.getIcon(), "drawable", context.getPackageName());
                if (resId != 0) {
                    imgIcon.setImageResource(resId);
                } else {
                    imgIcon.setImageResource(R.drawable.ic_launcher_background); // Hình mặc định
                }
            }
        }

        return convertView;
    }
}