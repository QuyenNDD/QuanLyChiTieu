package com.example.quanlychitieu.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlychitieu.R;
import com.example.quanlychitieu.activity.ManageCategoryActivity;
import com.example.quanlychitieu.adapter.CategoryAdapter;
import com.example.quanlychitieu.database.CategoryDao;
import com.example.quanlychitieu.model.Category;
import com.example.quanlychitieu.preference.SessionManager;
import com.example.quanlychitieu.database.TransactionDao;
import com.example.quanlychitieu.model.Transaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TransactionFormFragment extends Fragment {

    private static final String TYPE_EXPENSE = "EXPENSE";
    private static final String TYPE_INCOME = "INCOME";
    private static final String EDIT_CATEGORY_NAME = "Chỉnh sửa";

    private int currentUserId;
    private TextView tvPreviousDate;
    private TextView tvSelectedDate;
    private TextView tvNextDate;

    private TextView tvExpenseTab;
    private TextView tvIncomeTab;
    private TextView tvAmountLabel;
    private TextView btnSubmit;

    private EditText edtNote;
    private EditText edtAmount;

    private RecyclerView rvCategories;
    private CategoryAdapter categoryAdapter;

    private final List<Category> categoryList = new ArrayList<>();
    private final Calendar selectedCalendar = Calendar.getInstance();

    private String currentType = TYPE_EXPENSE;
    private Category selectedCategory;

    private CategoryDao categoryDao;

    private TransactionDao transactionDao;

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

        initViews(view);
        categoryDao = new CategoryDao(requireContext());
        transactionDao = new TransactionDao(requireContext());
        currentUserId = SessionManager.getCurrentUserId(requireContext());
        setupRecyclerView();
        setupEvents();

        selectedCalendar.setTimeInMillis(System.currentTimeMillis());
        updateDisplayedDate();
        updateTransactionTypeUI();
        loadCategoriesFromDatabase();
    }

    private void initViews(@NonNull View view) {
        tvPreviousDate = view.findViewById(R.id.tvPreviousDate);
        tvSelectedDate = view.findViewById(R.id.tvSelectedDate);
        tvNextDate = view.findViewById(R.id.tvNextDate);

        tvExpenseTab = view.findViewById(R.id.tvExpenseTab);
        tvIncomeTab = view.findViewById(R.id.tvIncomeTab);
        tvAmountLabel = view.findViewById(R.id.tvAmountLabel);
        btnSubmit = view.findViewById(R.id.btnSubmit);

        edtNote = view.findViewById(R.id.edtNote);
        edtAmount = view.findViewById(R.id.edtAmount);

        rvCategories = view.findViewById(R.id.rvCategories);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadCategoriesFromDatabase();
    }
    private void setupRecyclerView() {
        rvCategories.setLayoutManager(new GridLayoutManager(requireContext(), 3));

        categoryAdapter = new CategoryAdapter(categoryList, new CategoryAdapter.OnCategoryClickListener() {
            @Override
            public void onCategoryClick(Category category, int position) {
                if (EDIT_CATEGORY_NAME.equalsIgnoreCase(category.getName())) {
                    openManageCategoryScreen();
                    return;
                }

                selectedCategory = category;

                Toast.makeText(
                        requireContext(),
                        "Đã chọn danh mục: " + category.getName(),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        rvCategories.setAdapter(categoryAdapter);
    }

    private void setupEvents() {
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
            currentType = TYPE_EXPENSE;
            selectedCategory = null;
            updateTransactionTypeUI();
            loadCategoriesFromDatabase();
        });

        tvIncomeTab.setOnClickListener(v -> {
            currentType = TYPE_INCOME;
            selectedCategory = null;
            updateTransactionTypeUI();
            loadCategoriesFromDatabase();
        });

        btnSubmit.setOnClickListener(v -> submitTransaction());
    }

    private void openManageCategoryScreen() {
        Intent intent = new Intent(requireContext(), ManageCategoryActivity.class);
        intent.putExtra(ManageCategoryActivity.EXTRA_CATEGORY_TYPE, currentType);
        startActivity(intent);
    }

    private void showDatePicker() {
        int year = selectedCalendar.get(Calendar.YEAR);
        int month = selectedCalendar.get(Calendar.MONTH);
        int day = selectedCalendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                requireContext(),
                (picker, selectedYear, selectedMonth, selectedDayOfMonth) -> {
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
        if (TYPE_EXPENSE.equals(currentType)) {
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

    private void loadCategoriesFromDatabase() {
        categoryList.clear();

        if (TYPE_EXPENSE.equals(currentType)) {
            categoryList.addAll(categoryDao.getCategoriesByType(currentUserId, TYPE_EXPENSE));
            categoryList.add(new Category("Chỉnh sửa", TYPE_EXPENSE));
        } else {
            categoryList.addAll(categoryDao.getCategoriesByType(currentUserId, TYPE_INCOME));
            categoryList.add(new Category("Chỉnh sửa", TYPE_INCOME));
        }

        categoryAdapter.notifyDataSetChanged();
    }

    private void submitTransaction() {
        String note = edtNote != null ? edtNote.getText().toString().trim() : "";
        String amountText = edtAmount != null ? edtAmount.getText().toString().trim() : "";

        if (amountText.isEmpty()) {
            Toast.makeText(requireContext(), "Vui lòng nhập số tiền", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedCategory == null) {
            Toast.makeText(requireContext(), "Vui lòng chọn danh mục", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Số tiền không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (amount <= 0) {
            Toast.makeText(requireContext(), "Số tiền phải lớn hơn 0", Toast.LENGTH_SHORT).show();
            return;
        }

        String transactionDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(selectedCalendar.getTime());

        Transaction transaction = new Transaction(
                currentUserId,
                selectedCategory.getId(),
                amount,
                note,
                transactionDate,
                currentType
        );

        long result = transactionDao.insertTransaction(transaction);

        if (result > 0) {
            Toast.makeText(requireContext(), "Lưu giao dịch thành công", Toast.LENGTH_SHORT).show();
            clearFormAfterSave();
        } else {
            Toast.makeText(requireContext(), "Lưu giao dịch thất bại", Toast.LENGTH_SHORT).show();
        }
    }
    private void clearFormAfterSave() {
        edtAmount.setText("");
        edtNote.setText("");
        selectedCategory = null;

        if (categoryAdapter != null) {
            categoryAdapter.clearSelection();
        }
    }
}