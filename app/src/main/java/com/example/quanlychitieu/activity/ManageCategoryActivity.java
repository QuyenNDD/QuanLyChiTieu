package com.example.quanlychitieu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlychitieu.R;
import com.example.quanlychitieu.adapter.ManageCategoryAdapter;
import com.example.quanlychitieu.database.CategoryDao;
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

    private CategoryDao categoryDao;
    private final List<Category> categoryList = new ArrayList<>();

    private String currentType = TYPE_EXPENSE;
    private boolean isEditMode = false;

    public static final String EXTRA_CATEGORY_TYPE = "extra_category_type";

    private final ActivityResultLauncher<Intent> categoryFormLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() != RESULT_OK || result.getData() == null) {
                    return;
                }

                Intent data = result.getData();

                String mode = data.getStringExtra(AddEditCategoryActivity.EXTRA_MODE);
                int id = data.getIntExtra(AddEditCategoryActivity.EXTRA_ID, -1);
                String name = data.getStringExtra(AddEditCategoryActivity.EXTRA_NAME);
                String type = data.getStringExtra(AddEditCategoryActivity.EXTRA_TYPE);
                String icon = data.getStringExtra(AddEditCategoryActivity.EXTRA_ICON);
                int color = data.getIntExtra(AddEditCategoryActivity.EXTRA_COLOR, 0xFFFF9800);

                if (type == null || type.trim().isEmpty()) {
                    type = currentType;
                }

                if (name == null || name.trim().isEmpty()) {
                    Toast.makeText(this, "Tên danh mục không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }

                Category category = new Category(id, name.trim(), type, icon, color);

                if (AddEditCategoryActivity.MODE_ADD.equals(mode)) {
                    long resultId = categoryDao.insertCategory(category);
                    if (resultId > 0) {
                        Toast.makeText(this, "Thêm danh mục thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Thêm danh mục thất bại", Toast.LENGTH_SHORT).show();
                    }
                } else if (AddEditCategoryActivity.MODE_EDIT.equals(mode)) {
                    int updateResult = categoryDao.updateCategory(category);
                    if (updateResult > 0) {
                        Toast.makeText(this, "Cập nhật danh mục thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Cập nhật danh mục thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

                loadCategoriesFromDatabase();
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_category);

        categoryDao = new CategoryDao(this);

        initViews();
        readIntentData();
        setupRecyclerView();
        setupEvents();

        seedDefaultCategoriesIfNeeded();

        if (TYPE_INCOME.equals(currentType)) {
            toggleGroupType.check(R.id.btnIncome);
            btnIncome.setBackgroundTintList(getColorStateList(R.color.toggle_selected));
            btnExpense.setBackgroundTintList(getColorStateList(R.color.toggle_unselected));
        } else {
            toggleGroupType.check(R.id.btnExpense);
            btnExpense.setBackgroundTintList(getColorStateList(R.color.toggle_selected));
            btnIncome.setBackgroundTintList(getColorStateList(R.color.toggle_unselected));
        }

        loadCategoriesFromDatabase();
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

    private void readIntentData() {
        Intent intent = getIntent();
        if (intent == null) return;

        String receivedType = intent.getStringExtra(EXTRA_CATEGORY_TYPE);
        if (TYPE_INCOME.equals(receivedType)) {
            currentType = TYPE_INCOME;
        } else {
            currentType = TYPE_EXPENSE;
        }
    }
    private void setupRecyclerView() {
        adapter = new ManageCategoryAdapter(new ManageCategoryAdapter.OnCategoryClickListener() {
            @Override
            public void onCategoryClick(Category category, int position) {
                Intent intent = new Intent(ManageCategoryActivity.this, AddEditCategoryActivity.class);
                intent.putExtra(AddEditCategoryActivity.EXTRA_MODE, AddEditCategoryActivity.MODE_EDIT);
                intent.putExtra(AddEditCategoryActivity.EXTRA_POSITION, position);
                intent.putExtra(AddEditCategoryActivity.EXTRA_ID, category.getId());
                intent.putExtra(AddEditCategoryActivity.EXTRA_NAME, category.getName());
                intent.putExtra(AddEditCategoryActivity.EXTRA_TYPE, category.getType());
                intent.putExtra(AddEditCategoryActivity.EXTRA_ICON, category.getIcon());
                intent.putExtra(AddEditCategoryActivity.EXTRA_COLOR, category.getColorValue());
                categoryFormLauncher.launch(intent);
            }

            @Override
            public void onDeleteClick(Category category, int position) {
                int result = categoryDao.deleteCategory(category.getId());
                if (result > 0) {
                    Toast.makeText(ManageCategoryActivity.this, "Xóa danh mục thành công", Toast.LENGTH_SHORT).show();
                    loadCategoriesFromDatabase();
                } else {
                    Toast.makeText(ManageCategoryActivity.this, "Xóa danh mục thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        rvCategory.setLayoutManager(new LinearLayoutManager(this));
        rvCategory.setAdapter(adapter);
    }

    private void setupEvents() {
        btnBack.setOnClickListener(v -> finish());

        cardAddCategory.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditCategoryActivity.class);
            intent.putExtra(AddEditCategoryActivity.EXTRA_MODE, AddEditCategoryActivity.MODE_ADD);
            intent.putExtra(AddEditCategoryActivity.EXTRA_TYPE, currentType);
            categoryFormLauncher.launch(intent);
        });

        txtEdit.setOnClickListener(v -> {
            isEditMode = !isEditMode;
            txtEdit.setText(isEditMode ? "Hoàn thành" : "Chỉnh sửa");
            adapter.setEditMode(isEditMode);
        });

        toggleGroupType.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (!isChecked) return;

            if (checkedId == R.id.btnExpense) {
                currentType = TYPE_EXPENSE;
                btnExpense.setBackgroundTintList(getColorStateList(R.color.toggle_selected));
                btnIncome.setBackgroundTintList(getColorStateList(R.color.toggle_unselected));
            } else if (checkedId == R.id.btnIncome) {
                currentType = TYPE_INCOME;
                btnIncome.setBackgroundTintList(getColorStateList(R.color.toggle_selected));
                btnExpense.setBackgroundTintList(getColorStateList(R.color.toggle_unselected));
            }

            isEditMode = false;
            txtEdit.setText("Chỉnh sửa");
            adapter.setEditMode(false);
            loadCategoriesFromDatabase();
        });
    }

    private void loadCategoriesFromDatabase() {
        categoryList.clear();
        categoryList.addAll(categoryDao.getCategoriesByType(currentType));
        adapter.setData(categoryList);
    }

    private void seedDefaultCategoriesIfNeeded() {
        List<Category> expenseList = categoryDao.getCategoriesByType(TYPE_EXPENSE);
        if (expenseList.isEmpty()) {
            categoryDao.insertCategory(new Category("Ăn uống", TYPE_EXPENSE, "🍽️", 0xFFFF9800));
            categoryDao.insertCategory(new Category("Chi tiêu hàng ngày", TYPE_EXPENSE, "🧴", 0xFF00C853));
            categoryDao.insertCategory(new Category("Quần áo", TYPE_EXPENSE, "👕", 0xFF1E40AF));
            categoryDao.insertCategory(new Category("Mỹ phẩm", TYPE_EXPENSE, "💄", 0xFFEC4899));
            categoryDao.insertCategory(new Category("Phí giao lưu", TYPE_EXPENSE, "🎉", 0xFFFF4D5A));
            categoryDao.insertCategory(new Category("Y tế", TYPE_EXPENSE, "👥", 0xFF67D695));
            categoryDao.insertCategory(new Category("Giáo dục", TYPE_EXPENSE, "📚", 0xFFF2AE72));
            categoryDao.insertCategory(new Category("Tiền điện", TYPE_EXPENSE, "🚰", 0xFF29B6F6));
            categoryDao.insertCategory(new Category("Đi lại", TYPE_EXPENSE, "🚆", 0xFFFFB020));
            categoryDao.insertCategory(new Category("Phí liên lạc", TYPE_EXPENSE, "📱", 0xFF8D8D8D));
            categoryDao.insertCategory(new Category("Tiền nhà", TYPE_EXPENSE, "🏠", 0xFFB7794B));
            categoryDao.insertCategory(new Category("Tiết kiệm", TYPE_EXPENSE, "💰", 0xFF9C27B0));
        }

        List<Category> incomeList = categoryDao.getCategoriesByType(TYPE_INCOME);
        if (incomeList.isEmpty()) {
            categoryDao.insertCategory(new Category("Tiền lương", TYPE_INCOME, "💰", 0xFFFF9800));
            categoryDao.insertCategory(new Category("Tiền thưởng", TYPE_INCOME, "🎁", 0xFFEC4899));
            categoryDao.insertCategory(new Category("Thu nhập phụ", TYPE_INCOME, "🛒", 0xFF1E40AF));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCategoriesFromDatabase();
    }
}