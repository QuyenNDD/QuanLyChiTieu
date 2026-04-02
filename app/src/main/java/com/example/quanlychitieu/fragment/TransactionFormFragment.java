package com.example.quanlychitieu.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlychitieu.R;
import com.example.quanlychitieu.adapter.CategoryAdapter;
import com.example.quanlychitieu.model.Category;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TransactionFormFragment extends Fragment {

    private TextView tvPreviousDate;
    private TextView tvSelectedDate;
    private TextView tvNextDate;

    private TextView tvExpenseTab;
    private TextView tvIncomeTab;
    private TextView tvAmountLabel;
    private TextView btnSubmit;

    private RecyclerView rvCategories;
    private CategoryAdapter categoryAdapter;
    private final List<Category> categoryList = new ArrayList<>();

    private final Calendar selectedCalendar = Calendar.getInstance();
    private String currentType = "expense";

    public TransactionFormFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_transaction_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvPreviousDate = view.findViewById(R.id.tvPreviousDate);
        tvSelectedDate = view.findViewById(R.id.tvSelectedDate);
        tvNextDate = view.findViewById(R.id.tvNextDate);

        tvExpenseTab = view.findViewById(R.id.tvExpenseTab);
        tvIncomeTab = view.findViewById(R.id.tvIncomeTab);
        tvAmountLabel = view.findViewById(R.id.tvAmountLabel);
        btnSubmit = view.findViewById(R.id.btnSubmit);

        rvCategories = view.findViewById(R.id.rvCategories);
        rvCategories.setLayoutManager(new GridLayoutManager(requireContext(), 3));

        categoryAdapter = new CategoryAdapter(categoryList);
        rvCategories.setAdapter(categoryAdapter);

        selectedCalendar.setTimeInMillis(System.currentTimeMillis());
        updateDisplayedDate();

        tvPreviousDate.setOnClickListener(v -> {
            selectedCalendar.add(Calendar.DAY_OF_MONTH, -1);
            updateDisplayedDate();
        });

        tvNextDate.setOnClickListener(v -> {
            selectedCalendar.add(Calendar.DAY_OF_MONTH, 1);
            updateDisplayedDate();
        });

        tvSelectedDate.setOnClickListener(v -> showDatePicker());

        tvExpenseTab.setOnClickListener(v -> {
            currentType = "expense";
            updateTransactionTypeUI();
            loadCategoriesFromLocal();
        });

        tvIncomeTab.setOnClickListener(v -> {
            currentType = "income";
            updateTransactionTypeUI();
            loadCategoriesFromLocal();
        });

        updateTransactionTypeUI();
        loadCategoriesFromLocal();
    }

    private void showDatePicker() {
        int year = selectedCalendar.get(Calendar.YEAR);
        int month = selectedCalendar.get(Calendar.MONTH);
        int day = selectedCalendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                requireContext(),
                (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
                    selectedCalendar.set(Calendar.YEAR, selectedYear);
                    selectedCalendar.set(Calendar.MONTH, selectedMonth);
                    selectedCalendar.set(Calendar.DAY_OF_MONTH, selectedDayOfMonth);
                    updateDisplayedDate();
                },
                year,
                month,
                day
        );

        dialog.show();
    }

    private void updateDisplayedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String displayText = sdf.format(selectedCalendar.getTime()) + " (" + getVietnameseDayOfWeek(selectedCalendar) + ")";
        tvSelectedDate.setText(displayText);
    }

    private String getVietnameseDayOfWeek(Calendar calendar) {
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                return "Th 2";
            case Calendar.TUESDAY:
                return "Th 3";
            case Calendar.WEDNESDAY:
                return "Th 4";
            case Calendar.THURSDAY:
                return "Th 5";
            case Calendar.FRIDAY:
                return "Th 6";
            case Calendar.SATURDAY:
                return "Th 7";
            case Calendar.SUNDAY:
                return "CN";
            default:
                return "";
        }
    }

    private void updateTransactionTypeUI() {
        if ("expense".equals(currentType)) {
            tvExpenseTab.setBackgroundResource(R.drawable.bg_tab_selected);
            tvIncomeTab.setBackground(null);

            tvExpenseTab.setTextColor(0xFFFFFFFF);
            tvIncomeTab.setTextColor(0xFFC9C9C9);

            tvAmountLabel.setText("Tiền chi");
            btnSubmit.setText("Nhập khoản chi");
        } else {
            tvIncomeTab.setBackgroundResource(R.drawable.bg_tab_selected);
            tvExpenseTab.setBackground(null);

            tvIncomeTab.setTextColor(0xFFFFFFFF);
            tvExpenseTab.setTextColor(0xFFC9C9C9);

            tvAmountLabel.setText("Tiền thu");
            btnSubmit.setText("Nhập khoản thu");
        }
    }

    private void loadCategoriesFromLocal() {
        categoryList.clear();

        if ("expense".equals(currentType)) {
            categoryList.add(new Category("Ăn uống"));
            categoryList.add(new Category("Quần áo"));
            categoryList.add(new Category("Đi lại"));
            categoryList.add(new Category("Y tế"));
            categoryList.add(new Category("Giáo dục"));
            categoryList.add(new Category("Tiền nhà"));
            categoryList.add(new Category("Phí liên lạc"));
            categoryList.add(new Category("Mỹ phẩm"));
            categoryList.add(new Category("Khác"));
        } else {
            categoryList.add(new Category("Lương"));
            categoryList.add(new Category("Thưởng"));
            categoryList.add(new Category("Bán hàng"));
            categoryList.add(new Category("Đầu tư"));
            categoryList.add(new Category("Hoàn tiền"));
            categoryList.add(new Category("Khác"));
        }

        categoryAdapter.notifyDataSetChanged();
    }
}