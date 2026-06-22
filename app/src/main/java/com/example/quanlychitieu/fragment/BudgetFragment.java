package com.example.quanlychitieu.fragment;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlychitieu.R;
import com.example.quanlychitieu.adapter.BudgetAdapter;
import com.example.quanlychitieu.database.BudgetDao;
import com.example.quanlychitieu.model.Budget;
import com.example.quanlychitieu.preference.SessionManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BudgetFragment extends Fragment {

    private RecyclerView rvBudgets;
    private BudgetAdapter adapter;
    private TextView tvMonthTitle;
    private ImageButton btnAddBudget;

    private BudgetDao budgetDao;
    private List<Budget> budgetList;
    private int currentMonth, currentYear, userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget, container, false);

        // Khởi tạo thông tin cơ sở
        userId = SessionManager.getCurrentUserId(requireContext());
        budgetDao = new BudgetDao(requireContext());
        budgetList = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        currentMonth = calendar.get(Calendar.MONTH) + 1;
        currentYear = calendar.get(Calendar.YEAR);

        initViews(view);
        setupRecyclerView();
        loadDataFromDatabase();

        return view;
    }

    private void initViews(View view) {
        rvBudgets = view.findViewById(R.id.rvBudgets);
        tvMonthTitle = view.findViewById(R.id.tvMonthTitle);
        btnAddBudget = view.findViewById(R.id.btnAddBudget);

        tvMonthTitle.setText("Ngân sách tháng " + currentMonth + "/" + currentYear);
        btnAddBudget.setOnClickListener(v -> showAddBudgetDialog());
    }

    private void setupRecyclerView() {
        rvBudgets.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BudgetAdapter(budgetList);
        rvBudgets.setAdapter(adapter);
    }

    private void loadDataFromDatabase() {
        budgetList.clear();
        // Lấy dữ liệu theo user đang đăng nhập và tháng hiện tại
        budgetList.addAll(budgetDao.getBudgetsByMonth(userId, currentMonth, currentYear));
        adapter.notifyDataSetChanged();
    }

    private void showAddBudgetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_budget, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        Spinner spinnerCategory = dialogView.findViewById(R.id.spinnerCategory);
        EditText edtTotalLimit = dialogView.findViewById(R.id.edtTotalLimit);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnSave = dialogView.findViewById(R.id.btnSave);

        // Đổ dữ liệu Categories vào Spinner
        Cursor cursor = budgetDao.getExpenseCategories(userId);
        if (cursor == null || cursor.getCount() == 0) {
            Toast.makeText(getContext(), "Vui lòng tạo Danh mục chi tiêu trước!", Toast.LENGTH_LONG).show();
            return;
        }

        SimpleCursorAdapter spinnerAdapter = new SimpleCursorAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                cursor,
                new String[]{"name"}, // Tên cột hiển thị
                new int[]{android.R.id.text1}, // ID view mặc định của Android
                0
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(spinnerAdapter);

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnSave.setOnClickListener(v -> {
            String limitStr = edtTotalLimit.getText().toString().trim();

            if (limitStr.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập hạn mức", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double limit = Double.parseDouble(limitStr);

                // Lấy Category ID từ Spinner
                Cursor selectedCursor = (Cursor) spinnerCategory.getSelectedItem();
                int categoryId = selectedCursor.getInt(selectedCursor.getColumnIndexOrThrow("_id"));

                boolean isSuccess = budgetDao.insertBudget(userId, categoryId, currentMonth, currentYear, limit);

                if (isSuccess) {
                    Toast.makeText(getContext(), "Tạo ngân sách thành công!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    loadDataFromDatabase();
                } else {
                    Toast.makeText(getContext(), "Lỗi khi lưu", Toast.LENGTH_SHORT).show();
                }

            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Hạn mức không hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }
}