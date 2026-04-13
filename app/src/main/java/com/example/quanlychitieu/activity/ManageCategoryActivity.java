package com.example.quanlychitieu.activity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlychitieu.R;
import com.example.quanlychitieu.adapter.ManageCategoryAdapter;
import com.example.quanlychitieu.model.Category;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class ManageCategoryActivity extends AppCompatActivity {

    public static final String TYPE_EXPENSE = "EXPENSE";
    public static final String TYPE_INCOME = "INCOME";

    private RecyclerView rvCategory;
    private ManageCategoryAdapter adapter;
    private MaterialButtonToggleGroup toggleGroupType;
    private MaterialButton btnExpense, btnIncome;
    private TextView txtEdit;
    private ImageButton btnBack;
    private MaterialCardView cardAddCategory;

    private String currentType = TYPE_EXPENSE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_category);

        initViews();
        setupRecyclerView();
        setupEvents();

        toggleGroupType.check(R.id.btnExpense);
        loadCategories(currentType);
    }

    private void initViews() {
        rvCategory = findViewById(R.id.rvCategory);
        toggleGroupType = findViewById(R.id.toggleGroupType);
        btnExpense = findViewById(R.id.btnExpense);
        btnIncome = findViewById(R.id.btnIncome);
        txtEdit = findViewById(R.id.txtEdit);
        btnBack = findViewById(R.id.btnBack);
        cardAddCategory = findViewById(R.id.cardAddCategory);
    }

    private void setupRecyclerView() {
        adapter = new ManageCategoryAdapter(category ->
                Toast.makeText(this, "Chọn: " + category.getName(), Toast.LENGTH_SHORT).show()
        );
        rvCategory.setLayoutManager(new LinearLayoutManager(this));
        rvCategory.setAdapter(adapter);
    }

    private void setupEvents() {
        btnBack.setOnClickListener(v -> finish());

        cardAddCategory.setOnClickListener(v ->
                Toast.makeText(this, "Mở màn hình thêm danh mục", Toast.LENGTH_SHORT).show()
        );

        txtEdit.setOnClickListener(v ->
                Toast.makeText(this, "Chuyển sang chế độ chỉnh sửa", Toast.LENGTH_SHORT).show()
        );

        toggleGroupType.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (!isChecked) return;

            if (checkedId == R.id.btnExpense) {
                currentType = TYPE_EXPENSE;
                btnExpense.setBackgroundTintList(getColorStateList(R.color.toggle_selected));
                btnIncome.setBackgroundTintList(getColorStateList(R.color.toggle_unselected));
                loadCategories(currentType);
            } else if (checkedId == R.id.btnIncome) {
                currentType = TYPE_INCOME;
                btnIncome.setBackgroundTintList(getColorStateList(R.color.toggle_selected));
                btnExpense.setBackgroundTintList(getColorStateList(R.color.toggle_unselected));
                loadCategories(currentType);
            }
        });
    }

    private void loadCategories(String type) {
        // HIỆN TẠI: dữ liệu giả
        List<Category> categoryList = getMockCategories(type);

        // SAU NÀY: đổi thành database
        // List<Category> categoryList = dbHelper.getCategoriesByType(type);

        adapter.setData(categoryList);
    }

    private List<Category> getMockCategories(String type) {
        List<Category> list = new ArrayList<>();
        if ("EXPENSE".equals(type)) {
            list.add(new Category(1, "Ăn uống", "EXPENSE"));
            list.add(new Category(2, "Chi tiêu hàng ngày", "EXPENSE"));
            list.add(new Category(3, "Quần áo", "EXPENSE"));
            list.add(new Category(4, "Mỹ phẩm", "EXPENSE"));
            list.add(new Category(5, "Phí giao lưu", "EXPENSE"));
            list.add(new Category(6, "Y tế", "EXPENSE"));
        } else {
            list.add(new Category(101, "Tiền lương", "INCOME"));
            list.add(new Category(102, "Tiền phụ cấp", "INCOME"));
            list.add(new Category(103, "Tiền thưởng", "INCOME"));
            list.add(new Category(104, "Thu nhập phụ", "INCOME"));
            list.add(new Category(105, "Đầu tư", "INCOME"));
            list.add(new Category(106, "Thu nhập tạm thời", "INCOME"));
        }

        return list;
    }
}