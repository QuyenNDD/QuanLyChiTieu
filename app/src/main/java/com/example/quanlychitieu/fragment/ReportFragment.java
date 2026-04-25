package com.example.quanlychitieu.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.quanlychitieu.adapter.ReportPagerAdapter;
import com.example.quanlychitieu.R;
import com.example.quanlychitieu.database.ReportDao;
import com.example.quanlychitieu.preference.SessionManager;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Calendar;

public class ReportFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private TextView tvTotalIncome, tvTotalExpense, tvBalance;
    private TextView tvSelectDate;
    private ImageView imageView;
    private int selectedYear, selectedMonth;
    private int userId;
    private ReportDao reportDao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_report, container, false);

        // Lấy userId từ SessionManager
        userId = SessionManager.getCurrentUserId(requireContext());

        reportDao = new ReportDao(requireContext());

        Calendar calendar = Calendar.getInstance();

        tvTotalIncome = view.findViewById(R.id.tvTotalIncome);
        tvTotalExpense = view.findViewById(R.id.tvTotalExpense);
        tvBalance = view.findViewById(R.id.tvBalance);
        tabLayout = view.findViewById(R.id.tabLayoutReport);
        viewPager = view.findViewById(R.id.viewPagerReport);
        selectedYear = calendar.get(Calendar.YEAR);
        selectedMonth = calendar.get(Calendar.MONTH);
        tvSelectDate = view.findViewById(R.id.tvSelectDate);
        imageView = view.findViewById(R.id.ivCalendarIcon);

        tvSelectDate.setText(String.format("Tháng %02d/%d", (selectedMonth + 1), selectedYear));
        tvSelectDate.setOnClickListener(v -> showMonthYearPickerCustom());
        imageView.setOnClickListener(v -> showMonthYearPickerCustom());

        ReportPagerAdapter adapter = new ReportPagerAdapter(this);
        viewPager.setAdapter(adapter);

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
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.dialog_month_year_picker, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();

        TextView tvYear = view.findViewById(R.id.tvSelectedYear);
        ImageButton btnPrev = view.findViewById(R.id.btnPrevYear);
        ImageButton btnNext = view.findViewById(R.id.btnNextYear);
        GridView gvMonths = view.findViewById(R.id.gvMonths);

        String[] months = {"T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", "T11", "T12"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.item_month, months);
        gvMonths.setAdapter(adapter);

        tvYear.setText(String.valueOf(selectedYear));

        btnPrev.setOnClickListener(v -> {
            selectedYear--;
            tvYear.setText(String.valueOf(selectedYear));
        });

        btnNext.setOnClickListener(v -> {
            selectedYear++;
            tvYear.setText(String.valueOf(selectedYear));
        });

        gvMonths.setOnItemClickListener((parent, v, position, id) -> {
            selectedMonth = position;

            tvSelectDate.setText(String.format("Tháng %02d/%d", (selectedMonth + 1), selectedYear));

            dialog.dismiss();

            updateReportData();
        });

        dialog.show();
    }

    private void updateReportData() {
        if (reportDao == null) return;

        // Lấy lại userId mới nhất từ SessionManager
        userId = SessionManager.getCurrentUserId(requireContext());

        // --- TẦNG 2: CẬP NHẬT TỔNG QUÁT ---
        double income = reportDao.getTotalAmountByMonth(userId, selectedMonth + 1, selectedYear, "INCOME");
        double expense = reportDao.getTotalAmountByMonth(userId, selectedMonth + 1, selectedYear, "EXPENSE");
        double balance = income - expense;

        tvTotalIncome.setText(String.format("+%,.0fđ", income));
        tvTotalExpense.setText(String.format("-%,.0fđ", expense));
        tvBalance.setText(String.format("%,.0fđ", balance));

        // --- TẦNG 4: CẬP NHẬT BIỂU ĐỒ VÀ DANH SÁCH ---
        refreshChildFragments();
    }

    private void refreshChildFragments() {
        if (viewPager == null) return;

        Fragment currentFragment = getChildFragmentManager().findFragmentByTag("f" + viewPager.getCurrentItem());

        if (currentFragment instanceof ExpenseReportFragment) {
            ((ExpenseReportFragment) currentFragment).updateData(selectedMonth, selectedYear);
        } else if (currentFragment instanceof IncomeReportFragment) {
            ((IncomeReportFragment) currentFragment).updateData(selectedMonth, selectedYear);
        }
    }
}