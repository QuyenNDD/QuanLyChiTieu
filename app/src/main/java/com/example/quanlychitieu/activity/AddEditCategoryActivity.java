package com.example.quanlychitieu.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlychitieu.R;
import com.google.android.material.button.MaterialButton;

public class AddEditCategoryActivity extends AppCompatActivity {

    public static final String EXTRA_MODE = "extra_mode";
    public static final String EXTRA_POSITION = "extra_position";
    public static final String EXTRA_ID = "extra_id";
    public static final String EXTRA_NAME = "extra_name";
    public static final String EXTRA_TYPE = "extra_type";
    public static final String EXTRA_ICON = "extra_icon";
    public static final String EXTRA_COLOR = "extra_color";

    public static final String MODE_ADD = "MODE_ADD";
    public static final String MODE_EDIT = "MODE_EDIT";

    private ImageButton btnBack;
    private TextView tvTitle;
    private TextView edtCategoryName;
    private TextView btnSave;

    private String mode = MODE_ADD;
    private int categoryId = -1;
    private int position = -1;
    private String currentType = ManageCategoryActivity.TYPE_EXPENSE;
    private String currentIcon = "🍽️";
    private int currentColor = 0xFFFF9800;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_category);

        initViews();
        readIntentData();
        setupViewByMode();
        setupEvents();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        tvTitle = findViewById(R.id.tvTitle);
        edtCategoryName = findViewById(R.id.edtCategoryName);
        btnSave = findViewById(R.id.btnSave);
    }

    private void readIntentData() {
        if (getIntent() == null) return;

        mode = getIntent().getStringExtra(EXTRA_MODE);
        if (mode == null || mode.trim().isEmpty()) {
            mode = MODE_ADD;
        }

        position = getIntent().getIntExtra(EXTRA_POSITION, -1);
        categoryId = getIntent().getIntExtra(EXTRA_ID, -1);

        String name = getIntent().getStringExtra(EXTRA_NAME);
        String type = getIntent().getStringExtra(EXTRA_TYPE);
        String icon = getIntent().getStringExtra(EXTRA_ICON);
        int color = getIntent().getIntExtra(EXTRA_COLOR, 0xFFFF9800);

        if (!TextUtils.isEmpty(type)) {
            currentType = type;
        }
        if (!TextUtils.isEmpty(icon)) {
            currentIcon = icon;
        }
        currentColor = color;

        if (!TextUtils.isEmpty(name)) {
            edtCategoryName.setText(name);
        }
    }

    private void setupViewByMode() {
        if (MODE_EDIT.equals(mode)) {
            tvTitle.setText("Chỉnh sửa danh mục");
            btnSave.setText("Lưu");
        } else {
            tvTitle.setText("Thêm danh mục");
            btnSave.setText("Thêm");
        }
    }

    private void setupEvents() {
        btnBack.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> submitCategory());
    }

    private void submitCategory() {
        String categoryName = edtCategoryName.getText().toString().trim();

        if (TextUtils.isEmpty(categoryName)) {
            Toast.makeText(this, "Vui lòng nhập tên danh mục", Toast.LENGTH_SHORT).show();
            return;
        }

        getIntent().putExtra(EXTRA_MODE, mode);
        getIntent().putExtra(EXTRA_POSITION, position);
        getIntent().putExtra(EXTRA_ID, categoryId);
        getIntent().putExtra(EXTRA_NAME, categoryName);
        getIntent().putExtra(EXTRA_TYPE, currentType);
        getIntent().putExtra(EXTRA_ICON, currentIcon);
        getIntent().putExtra(EXTRA_COLOR, currentColor);

        setResult(RESULT_OK, getIntent());
        finish();
    }
}