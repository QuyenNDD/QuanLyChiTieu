package com.example.quanlychitieu;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlychitieu.adapter.ReportAdapter;
import com.example.quanlychitieu.database.ReportDao;
import com.example.quanlychitieu.model.ReportItem;
import com.example.quanlychitieu.preference.SessionManager;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.List;

public class ReportCategoryDetailActivity extends BaseActivity {
    private BarChart barChart;
    private ListView lvTransactions;
    private TextView tvTitle;
    private ImageButton btnBack;
    private ReportDao reportDao;

    private int userId;
    private int catId, selMonth, selYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_activity_category_detail);

        tvTitle = findViewById(R.id.tvDetailTitle);
        barChart = findViewById(R.id.barChart);
        lvTransactions = findViewById(R.id.lvCategoryTransactions);
        btnBack = findViewById(R.id.btnBack);

        // Lấy userId từ 1 chỗ duy nhất
        userId = SessionManager.getCurrentUserId(this);

        catId = getIntent().getIntExtra("categoryId", -1);
        selMonth = getIntent().getIntExtra("selectedMonth", 1);
        selYear = getIntent().getIntExtra("selectedYear", 2026);
        String categoryName = getIntent().getStringExtra("categoryName");

        if (catId == -1) {
            Toast.makeText(this, "Lỗi: Không tìm thấy danh mục!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (categoryName != null) {
            tvTitle.setText(categoryName);
        }

        btnBack.setOnClickListener(v -> finish());

        reportDao = new ReportDao(this);

        setupBarChart();
        loadTransactionList(selMonth);
    }

    private void setupBarChart() {
        List<BarEntry> entries = reportDao.getBarChartData(userId, catId, selYear);

        BarDataSet dataSet = new BarDataSet(entries, "Tổng chi tiêu");
        dataSet.setColor(Color.parseColor("#4CAF50"));
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(10f);

        BarData barData = new BarData(dataSet);
        barChart.setData(barData);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(12);

        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                int clickedMonth = (int) e.getX();
                loadTransactionList(clickedMonth);
            }

            @Override
            public void onNothingSelected() {
            }
        });

        barChart.getDescription().setEnabled(false);
        barChart.animateY(1000);
        barChart.invalidate();
    }

    private void loadTransactionList(int month) {
        List<ReportItem> data = reportDao.getTransactionsByCategoryDetail(
                userId,
                catId,
                month,
                selYear
        );

        ReportAdapter adapter = new ReportAdapter(
                this,
                R.layout.report_item_transaction_detail,
                data
        );

        lvTransactions.setAdapter(adapter);

        if (data.isEmpty()) {
            Toast.makeText(this, "Không có giao dịch trong tháng " + month, Toast.LENGTH_SHORT).show();
        }
    }
}