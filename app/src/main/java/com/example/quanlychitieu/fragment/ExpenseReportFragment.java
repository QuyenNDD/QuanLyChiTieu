package com.example.quanlychitieu.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.quanlychitieu.ReportCategoryDetailActivity;
import com.example.quanlychitieu.database.ReportDao;
import com.example.quanlychitieu.preference.SessionManager;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import com.example.quanlychitieu.R;
import com.example.quanlychitieu.adapter.ReportAdapter;
import com.example.quanlychitieu.model.ReportItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ExpenseReportFragment extends Fragment {
    private PieChart pieChart;
    private ListView lvDetails;
    private ReportDao reportDAO;
    private ReportAdapter adapter;
    private List<ReportItem> reportDataList = new ArrayList<>();

    private int userId;
    private int mSelectedMonth;
    private int mSelectedYear;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_report_content, container, false);

        // Lấy userId từ SessionManager
        userId = SessionManager.getCurrentUserId(requireContext());

        // Khởi tạo thời gian mặc định là tháng hiện tại
        Calendar calendar = Calendar.getInstance();
        mSelectedMonth = calendar.get(Calendar.MONTH);
        mSelectedYear = calendar.get(Calendar.YEAR);

        pieChart = view.findViewById(R.id.pieChart);
        lvDetails = view.findViewById(R.id.lvReportDetails);
        reportDAO = new ReportDao(requireContext());

        // Load dữ liệu mặc định lần đầu
        updateData(mSelectedMonth, mSelectedYear);

        // Sự kiện click chuyển sang trang chi tiết danh mục
        lvDetails.setOnItemClickListener((parent, view1, position, id) -> {
            if (reportDataList.isEmpty()) return;

            ReportItem selectedItem = reportDataList.get(position);
            Intent intent = new Intent(getContext(), ReportCategoryDetailActivity.class);

            // Truyền dữ liệu sang Activity chi tiết
            // Không cần truyền userId vì ReportCategoryDetailActivity đã tự lấy từ SessionManager
            intent.putExtra("categoryId", selectedItem.getCategoryId());
            intent.putExtra("categoryName", selectedItem.getCategoryName());
            intent.putExtra("selectedMonth", mSelectedMonth + 1); // Trang detail nhận 1-12
            intent.putExtra("selectedYear", mSelectedYear);

            startActivity(intent);
        });

        return view;
    }

    /**
     * Hàm này được gọi từ ReportFragment mỗi khi người dùng đổi tháng/năm
     */
    public void updateData(int month, int year) {
        this.mSelectedMonth = month;
        this.mSelectedYear = year;

        if (reportDAO == null) {
            reportDAO = new ReportDao(requireContext());
        }

        userId = SessionManager.getCurrentUserId(requireContext());

        // Lấy dữ liệu chi tiêu từ DAO
        reportDataList = reportDAO.getReportData(userId, month + 1, year, "EXPENSE");

        // Cập nhật ListView
        adapter = new ReportAdapter(requireContext(), R.layout.report_item_transaction_detail, reportDataList);
        lvDetails.setAdapter(adapter);

        // Cập nhật PieChart
        setupPieChart(reportDataList);

        // Xử lý hiển thị khi không có dữ liệu
        if (reportDataList.isEmpty()) {
            pieChart.clear();
            pieChart.setNoDataText("Không có dữ liệu chi tiêu tháng " + (month + 1));
            pieChart.setNoDataTextColor(Color.GRAY);
            pieChart.invalidate();
        }
    }

    public void setupPieChart(List<ReportItem> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            return;
        }

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
        dataSet.setValueFormatter(new com.github.mikephil.charting.formatter.PercentFormatter(pieChart));

        PieData data = new PieData(dataSet);
        pieChart.setData(data);

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(40f);
        pieChart.setTransparentCircleRadius(45f);
        pieChart.setCenterText("CHI TIÊU");
        pieChart.setCenterTextSize(16f);

        pieChart.animateY(1000);
        pieChart.invalidate();
    }
}