package com.example.quanlychitieu.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.quanlychitieu.R;
import com.example.quanlychitieu.adapter.ReportAdapter;
import com.example.quanlychitieu.database.TransactionDao;
import com.example.quanlychitieu.model.ReportItem;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class IncomeReportFragment extends Fragment {
    private PieChart pieChart;
    private ListView lvDetails;
    private TransactionDao transactionDAO; // Khai báo DAO
    private ReportAdapter adapter;
    private List<ReportItem> reportDataList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_report_content, container, false);

        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);
        // 1. Ánh xạ
        pieChart = view.findViewById(R.id.pieChart);
        lvDetails = view.findViewById(R.id.lvReportDetails);

        // 2. Khởi tạo DAO
        transactionDAO = new TransactionDao(getContext());

        // 3. Tải dữ liệu từ Database
        updateData(currentMonth,currentYear);

        return view;
    }

    // Hàm này dùng để tải dữ liệu và cập nhật UI
    public void updateData(int month, int year) {
        // Gọi DAO với tháng và năm mới
        // Lưu ý: month trong Calendar từ 0-11 nên khi gọi SQL thường +1
        reportDataList = transactionDAO.getReportData(month + 1, year, "INCOME");

        // Đổ dữ liệu vào LisView
        adapter = new ReportAdapter(getContext(), R.layout.report_item_category, reportDataList);
        lvDetails.setAdapter(adapter);
        // vẻ biểu đồ
        setupPieChart(reportDataList);

        if (reportDataList.isEmpty()) {
            pieChart.clear();
            pieChart.setNoDataText("Không có dữ liệu chi tiêu tháng " + (month + 1));
        }
    }

    public void setupPieChart(List<ReportItem> dataList) {
        List<PieEntry> entries = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();

        for (ReportItem item : dataList) {
            entries.add(new PieEntry((float) item.getAmount(), item.getCategoryName()));
            colors.add(item.getColor());
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colors);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.WHITE);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("THU NHẬP");
        pieChart.animateY(1000); // Thêm hiệu ứng xoay cho đẹp
        pieChart.invalidate(); // Làm mới biểu đồ
    }
}