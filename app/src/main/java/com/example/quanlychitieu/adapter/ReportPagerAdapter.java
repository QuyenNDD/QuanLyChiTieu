package com.example.quanlychitieu.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.quanlychitieu.fragment.ExpenseReportFragment;
import com.example.quanlychitieu.fragment.IncomeReportFragment;

public class ReportPagerAdapter extends FragmentStateAdapter {
    public ReportPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) return new ExpenseReportFragment(); // Tab Chi tiêu
        return new IncomeReportFragment(); // Tab Thu nhập
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
