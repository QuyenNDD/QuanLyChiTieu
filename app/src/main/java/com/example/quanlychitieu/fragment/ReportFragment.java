package com.example.quanlychitieu.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.quanlychitieu.adapter.ReportAdapter;
import com.example.quanlychitieu.adapter.ReportPagerAdapter;
import com.example.quanlychitieu.R;
import com.example.quanlychitieu.database.TransactionDao;
import com.example.quanlychitieu.model.ReportItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Calendar;
import java.util.List;

public class ReportFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private TextView tvTotalIncome, tvTotalExpense, tvBalance;
    private TextView tvSelectDate;
    private int selectedYear, selectedMonth;
    private TransactionDao transactionDao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Nạp layout fragment_report.xml mà chúng ta đã thống nhất
        View view = inflater.inflate(R.layout.activity_report, container, false);

        transactionDao = new TransactionDao(getContext());

        Calendar calendar = Calendar.getInstance();
        // 1. Ánh xạ các view (Tầng 2, 3, 4)
        tvTotalIncome = view.findViewById(R.id.tvTotalIncome);
        tvTotalExpense = view.findViewById(R.id.tvTotalExpense);
        tvBalance = view.findViewById(R.id.tvBalance);
        tabLayout = view.findViewById(R.id.tabLayoutReport);
        viewPager = view.findViewById(R.id.viewPagerReport);
        selectedYear = calendar.get(Calendar.YEAR);
        selectedMonth = calendar.get(Calendar.MONTH);
        tvSelectDate = view.findViewById(R.id.tvSelectDate);

        // Gắn sự kiện Click cho Tầng 1
        tvSelectDate.setText(String.format("Tháng %02d/%d", (selectedMonth + 1), selectedYear));
        tvSelectDate.setOnClickListener(v -> {
            showMonthYearPickerCustom();
        });

        // 2. Thiết lập Adapter cho ViewPager2
        ReportPagerAdapter adapter = new ReportPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // 3. Kết nối TabLayout với ViewPager2 bằng TabLayoutMediator
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == 0) {
                    tab.setText("CHI TIÊU");
                } else {
                    tab.setText("THU NHẬP");
                }
            }
        }).attach();
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                refreshChildFragments();
            }
        });

        updateReportData();

        return view;
    }
    private void showMonthYearPickerCustom() {
        // 1. Khởi tạo Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.dialog_month_year_picker, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();

        // 2. Ánh xạ các view trong Dialog
        TextView tvYear = view.findViewById(R.id.tvSelectedYear);
        ImageButton btnPrev = view.findViewById(R.id.btnPrevYear);
        ImageButton btnNext = view.findViewById(R.id.btnNextYear);
        GridView gvMonths = view.findViewById(R.id.gvMonths);

        // Dữ liệu 12 tháng
        String[] months = {"T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", "T11", "T12"};

        // Dùng ArrayAdapter đơn giản để đổ dữ liệu vào GridView (Theo Chương 3 - ListView/GridView)
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.item_month, months);
        gvMonths.setAdapter(adapter);

        tvYear.setText(String.valueOf(selectedYear));

        // 3. Xử lý đổi Năm
        tvYear.setText(String.valueOf(selectedYear));
        btnPrev.setOnClickListener(v -> {
            selectedYear--;
            tvYear.setText(String.valueOf(selectedYear));
        });
        btnNext.setOnClickListener(v -> {
            selectedYear++;
            tvYear.setText(String.valueOf(selectedYear));
        });

        // 4. Xử lý chọn Tháng
        gvMonths.setOnItemClickListener((parent, v, position, id) -> {
            selectedMonth = position; // position từ 0 đến 11

            // Cập nhật lên Tầng 1
            tvSelectDate.setText(String.format("Tháng %02d/%d", (selectedMonth + 1), selectedYear));

            // Đóng dialog và load lại dữ liệu báo cáo
            dialog.dismiss();

            updateReportData();
        });
        dialog.show();
    }
    private void updateReportData() {
        if (transactionDao == null) return;
        // --- TẦNG 2: CẬP NHẬT TỔNG QUÁT ---
        double income = transactionDao.getTotalAmountByMonth(selectedMonth + 1, selectedYear, "INCOME");
        double expense = transactionDao.getTotalAmountByMonth(selectedMonth + 1, selectedYear, "EXPENSE");
        double balance = income - expense;

        tvTotalIncome.setText(String.format("+%,.0fđ", income));
        tvTotalExpense.setText(String.format("-%,.0fđ", expense));
        tvBalance.setText(String.format("%,.0fđ", balance));

        // --- TẦNG 4: CẬP NHẬT BIỂU ĐỒ VÀ DANH SÁCH ---
        // Tìm Fragment con đang hiển thị để ra lệnh cập nhật
        refreshChildFragments();
    }

    private void refreshChildFragments() {
        if (viewPager == null) return;

        // f0: Expense, f1: Income
        Fragment currentFragment = getChildFragmentManager().findFragmentByTag("f" + viewPager.getCurrentItem());

        if (currentFragment instanceof ExpenseReportFragment) {
            ((ExpenseReportFragment) currentFragment).updateData(selectedMonth, selectedYear);
        } else if (currentFragment instanceof IncomeReportFragment) {
            ((IncomeReportFragment) currentFragment).updateData(selectedMonth, selectedYear);
        }
    }
}
