package com.example.quanlychitieu.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.quanlychitieu.database.TransactionDao;
import com.example.quanlychitieu.model.Category;
import com.example.quanlychitieu.model.Transaction;
import com.example.quanlychitieu.model.TransactionWithCategory;
import com.example.quanlychitieu.preference.SessionManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TransactionFormFragment extends Fragment {

    private static final String ARG_SELECTED_DATE = "arg_selected_date";
    private static final String ARG_SHOW_SKIP = "arg_show_skip";

    private static final String ARG_EDIT_TRANSACTION_ID = "arg_edit_transaction_id";

    public static final String TYPE_EXPENSE = "EXPENSE";
    public static final String TYPE_INCOME = "INCOME";

    private TextView tvSkip;
    private TextView tvExpenseTab;
    private TextView tvIncomeTab;
    private TextView tvSelectedDate;
    private TextView tvPreviousDate;
    private TextView tvNextDate;
    private TextView tvAmountLabel;
    private Button btnSubmit;
    private EditText edtNote;
    private EditText edtAmount;
    private ImageView ivEdit;
    private RecyclerView rvCategories;

    private TextView tvDelete;

    private final Calendar selectedCalendar = Calendar.getInstance();
    private final List<Category> categoryList = new ArrayList<>();

    private CategoryAdapter categoryAdapter;
    private CategoryDao categoryDao;
    private TransactionDao transactionDao;

    private Category selectedCategory;
    private String currentType = TYPE_EXPENSE;
    private int currentUserId;
    private boolean showSkipButton = false;

    private int editingTransactionId = -1;
    private boolean isEditMode = false;

    public TransactionFormFragment() {
    }

    public static TransactionFormFragment newInstance(boolean showSkip) {
        TransactionFormFragment fragment = new TransactionFormFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_SHOW_SKIP, showSkip);
        fragment.setArguments(args);
        return fragment;
    }

    public static TransactionFormFragment newInstance(String selectedDate, boolean showSkip) {
        TransactionFormFragment fragment = new TransactionFormFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SELECTED_DATE, selectedDate);
        args.putBoolean(ARG_SHOW_SKIP, showSkip);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
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

        readArguments();
        setupRecyclerView();
        setupListeners();
        setupSkipButton();
        setupDeleteButton();
        loadCategoriesFromDatabase();
        if (isEditMode) {
            loadTransactionForEdit();
        } else {
            updateDateText();
            updateTypeUI();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadCategoriesFromDatabase();
    }

    private void initViews(@NonNull View view) {
        tvSkip = view.findViewById(R.id.tvSkip);
        tvExpenseTab = view.findViewById(R.id.tvExpenseTab);
        tvIncomeTab = view.findViewById(R.id.tvIncomeTab);
        tvSelectedDate = view.findViewById(R.id.tvSelectedDate);
        tvPreviousDate = view.findViewById(R.id.tvPreviousDate);
        tvNextDate = view.findViewById(R.id.tvNextDate);
        tvAmountLabel = view.findViewById(R.id.tvAmountLabel);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        edtNote = view.findViewById(R.id.edtNote);
        edtAmount = view.findViewById(R.id.edtAmount);
        ivEdit = view.findViewById(R.id.ivEdit);
        rvCategories = view.findViewById(R.id.rvCategories);
        tvDelete = view.findViewById(R.id.tvDelete);
    }

    private void readArguments() {
        Bundle args = getArguments();
        if (args == null) {
            return;
        }

        showSkipButton = args.getBoolean(ARG_SHOW_SKIP, false);

        editingTransactionId = args.getInt(ARG_EDIT_TRANSACTION_ID, -1);
        isEditMode = editingTransactionId > 0;

        String selectedDate = args.getString(ARG_SELECTED_DATE);
        if (!TextUtils.isEmpty(selectedDate)) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                java.util.Date date = sdf.parse(selectedDate);
                if (date != null) {
                    selectedCalendar.setTime(date);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setupRecyclerView() {
        categoryAdapter = new CategoryAdapter(categoryList, (category, position) -> {
            if (category == null) {
                return;
            }

            if (isEditItem(category)) {
                openManageCategoryScreen();
                return;
            }

            selectedCategory = category;
        });

        rvCategories.setLayoutManager(new GridLayoutManager(requireContext(), 4));
        rvCategories.setAdapter(categoryAdapter);
    }

    private void setupListeners() {
        setupAmountInput();

        tvExpenseTab.setOnClickListener(v -> {
            currentType = TYPE_EXPENSE;
            selectedCategory = null;
            categoryAdapter.clearSelection();
            updateTypeUI();
            loadCategoriesFromDatabase();
        });

        tvIncomeTab.setOnClickListener(v -> {
            currentType = TYPE_INCOME;
            selectedCategory = null;
            categoryAdapter.clearSelection();
            updateTypeUI();
            loadCategoriesFromDatabase();
        });

        tvSelectedDate.setOnClickListener(v -> showDatePicker());

        tvPreviousDate.setOnClickListener(v -> {
            selectedCalendar.add(Calendar.DAY_OF_MONTH, -1);
            updateDateText();
        });

        tvNextDate.setOnClickListener(v -> {
            selectedCalendar.add(Calendar.DAY_OF_MONTH, 1);
            updateDateText();
        });

        ivEdit.setOnClickListener(v -> openManageCategoryScreen());

        btnSubmit.setOnClickListener(v -> submitTransaction());
    }

    private void setupSkipButton() {
        tvSkip.setVisibility(showSkipButton ? View.VISIBLE : View.GONE);
        tvSkip.setOnClickListener(v -> closeFormAndBack());
    }

    private void setupAmountInput() {
        edtAmount.setOnFocusChangeListener((v, hasFocus) -> {
            String amountText = edtAmount.getText().toString().trim();

            if (hasFocus) {
                if ("0".equals(amountText)) {
                    edtAmount.setText("");
                }
            } else {
                if (TextUtils.isEmpty(amountText)) {
                    edtAmount.setText("0");
                }
            }
        });

        edtAmount.setOnClickListener(v -> {
            String amountText = edtAmount.getText().toString().trim();

            if ("0".equals(amountText)) {
                edtAmount.setText("");
            }
        });
    }

    private void updateTypeUI() {
        if (TYPE_EXPENSE.equals(currentType)) {
            tvExpenseTab.setBackgroundResource(R.drawable.bg_tab_selected);
            tvIncomeTab.setBackgroundResource(android.R.color.transparent);
            tvExpenseTab.setTextColor(0xFFFFFFFF);
            tvIncomeTab.setTextColor(0xFFC9C9C9);
            tvAmountLabel.setText("Tiền chi");

            if (isEditMode) {
                btnSubmit.setText("Chỉnh sửa khoản chi");
            } else {
                btnSubmit.setText("Nhập khoản chi");
            }
        } else {
            tvIncomeTab.setBackgroundResource(R.drawable.bg_tab_selected);
            tvExpenseTab.setBackgroundResource(android.R.color.transparent);
            tvIncomeTab.setTextColor(0xFFFFFFFF);
            tvExpenseTab.setTextColor(0xFFC9C9C9);
            tvAmountLabel.setText("Tiền thu");

            if (isEditMode) {
                btnSubmit.setText("Chỉnh sửa khoản thu");
            } else {
                btnSubmit.setText("Nhập khoản thu");
            }
        }
    }

    private void updateDateText() {
        SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        tvSelectedDate.setText(displayFormat.format(selectedCalendar.getTime()));
    }

    private void showDatePicker() {
        DatePickerDialog dialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {
                    selectedCalendar.set(Calendar.YEAR, year);
                    selectedCalendar.set(Calendar.MONTH, month);
                    selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateDateText();
                },
                selectedCalendar.get(Calendar.YEAR),
                selectedCalendar.get(Calendar.MONTH),
                selectedCalendar.get(Calendar.DAY_OF_MONTH)
        );
        dialog.show();
    }

    private void loadCategoriesFromDatabase() {
        currentUserId = SessionManager.getCurrentUserId(requireContext());

        categoryList.clear();

        // Chỉ lấy danh mục của user đang đăng nhập
        categoryList.addAll(categoryDao.getCategoriesByType(currentUserId, currentType));

        // Item này chỉ để mở màn hình quản lý danh mục
        Category editItem = new Category("Chỉnh sửa", currentType);
        editItem.setIcon("ic_arrow_right");
        editItem.setColorValue(0xFFFFFFFF);
        categoryList.add(editItem);

        categoryAdapter.notifyDataSetChanged();
    }

    private boolean isEditItem(Category category) {
        return category.getId() == 0 && "Chỉnh sửa".equalsIgnoreCase(category.getName());
    }

    private void openManageCategoryScreen() {
        Intent intent = new Intent(requireContext(), ManageCategoryActivity.class);
        intent.putExtra(ManageCategoryActivity.EXTRA_CATEGORY_TYPE, currentType);
        startActivity(intent);
    }

    private void submitTransaction() {
        String amountText = edtAmount.getText().toString().trim().replace(",", "");
        String note = edtNote.getText().toString().trim();

        if (TextUtils.isEmpty(amountText)) {
            Toast.makeText(requireContext(), "Vui lòng nhập số tiền", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedCategory == null || selectedCategory.getId() <= 0) {
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

        if (isEditMode) {
            transaction.setId(editingTransactionId);

            int result = transactionDao.updateTransaction(transaction);

            if (result > 0) {
                Toast.makeText(requireContext(), "Cập nhật giao dịch thành công", Toast.LENGTH_SHORT).show();
                closeFormAndBack();
            } else {
                Toast.makeText(requireContext(), "Cập nhật giao dịch thất bại", Toast.LENGTH_SHORT).show();
            }
        } else {
            long result = transactionDao.insertTransaction(transaction);

            if (result > 0) {
                Toast.makeText(requireContext(), "Lưu giao dịch thành công", Toast.LENGTH_SHORT).show();

                if (showSkipButton) {
                    closeFormAndBack();
                } else {
                    clearFormAfterSave();
                }
            } else {
                Toast.makeText(requireContext(), "Lưu giao dịch thất bại", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void clearFormAfterSave() {
        edtAmount.setText("0");
        edtNote.setText("");
        selectedCategory = null;
        categoryAdapter.clearSelection();
    }

    private void closeFormAndBack() {
        if (!isAdded()) {
            return;
        }

        if (requireActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
            requireActivity().getSupportFragmentManager().popBackStack();
        }
    }
    public static TransactionFormFragment newEditInstance(int transactionId, boolean showSkip) {
        TransactionFormFragment fragment = new TransactionFormFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_EDIT_TRANSACTION_ID, transactionId);
        args.putBoolean(ARG_SHOW_SKIP, showSkip);
        fragment.setArguments(args);
        return fragment;
    }
    private Category findCategoryById(int categoryId) {
        for (Category category : categoryList) {
            if (category.getId() == categoryId) {
                return category;
            }
        }
        return null;
    }
    private void loadTransactionForEdit() {
        TransactionWithCategory transaction =
                transactionDao.getTransactionWithCategoryById(editingTransactionId, currentUserId);

        if (transaction == null) {
            Toast.makeText(requireContext(), "Không tìm thấy giao dịch", Toast.LENGTH_SHORT).show();
            return;
        }

        currentType = transaction.getType();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            java.util.Date date = sdf.parse(transaction.getTransactionDate());
            if (date != null) {
                selectedCalendar.setTime(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        edtAmount.setText(String.format(Locale.getDefault(), "%,.0f", transaction.getAmount()));
        edtNote.setText(transaction.getNote() != null ? transaction.getNote() : "");

        updateDateText();
        updateTypeUI();
        loadCategoriesFromDatabase();

        selectedCategory = findCategoryById(transaction.getCategoryId());
        if (selectedCategory != null) {
            categoryAdapter.setSelectedCategoryId(transaction.getCategoryId());
        }
    }
    private void setupDeleteButton() {
        if (tvDelete == null) {
            return;
        }

        tvDelete.setVisibility(isEditMode ? View.VISIBLE : View.GONE);
        tvDelete.setOnClickListener(v -> confirmDeleteTransaction());
    }

    private void confirmDeleteTransaction() {
        if (!isEditMode || editingTransactionId <= 0) {
            return;
        }

        new AlertDialog.Builder(requireContext())
                .setTitle("Xóa giao dịch")
                .setMessage("Bạn có chắc muốn xóa giao dịch này không?")
                .setNegativeButton("Hủy", null)
                .setPositiveButton("Xóa", (dialog, which) -> deleteCurrentTransaction())
                .show();
    }
    private void deleteCurrentTransaction() {
        int result = transactionDao.deleteTransaction(editingTransactionId, currentUserId);
        if (result > 0) {
            Toast.makeText(requireContext(), "Xóa giao dịch thành công", Toast.LENGTH_SHORT).show();
            closeFormAndBack();
        } else {
            Toast.makeText(requireContext(), "Xóa giao dịch thất bại", Toast.LENGTH_SHORT).show();
        }
    }
}