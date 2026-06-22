package com.example.quanlychitieu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlychitieu.R;
import com.example.quanlychitieu.adapter.ColorOptionAdapter;
import com.example.quanlychitieu.adapter.IconOptionAdapter;
import com.example.quanlychitieu.model.IconOption;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

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
    private EditText edtCategoryName;
    private MaterialButton btnSave;
    private RecyclerView rvIcons;
    private RecyclerView rvColors;

    private String mode = MODE_ADD;
    private int categoryId = -1;
    private int position = -1;
    private String currentType = ManageCategoryActivity.TYPE_EXPENSE;
    private String selectedIcon = "ic_cat_food";
    private int selectedColor = 0xFFF7E46B;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_category);

        initViews();
        readIntentData();
        setupViewByMode();
        setupIconRecyclerView();
        setupColorRecyclerView();
        setupEvents();
        updateSaveButtonState();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        tvTitle = findViewById(R.id.tvTitle);
        edtCategoryName = findViewById(R.id.edtCategoryName);
        btnSave = findViewById(R.id.btnSave);
        rvIcons = findViewById(R.id.rvIcons);
        rvColors = findViewById(R.id.rvColors);
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
        int color = getIntent().getIntExtra(EXTRA_COLOR, 0xFFF7E46B);

        if (!TextUtils.isEmpty(type)) {
            currentType = type;
        }
        if (!TextUtils.isEmpty(icon)) {
            selectedIcon = icon;
        }
        selectedColor = color;

        if (!TextUtils.isEmpty(name)) {
            edtCategoryName.setText(name);
        }
    }

    private void setupViewByMode() {
        if (MODE_EDIT.equals(mode)) {
            tvTitle.setText("Chỉnh sửa");
        } else {
            tvTitle.setText("Tạo mới");
        }
    }

    private void setupIconRecyclerView() {
        List<IconOption> iconList = new ArrayList<>();

        iconList.add(new IconOption("ic_cat_food", R.drawable.ic_cat_food));
        iconList.add(new IconOption("ic_cat_transport", R.drawable.ic_cat_transport));
        iconList.add(new IconOption("ic_cat_shopping", R.drawable.ic_cat_shopping));
        iconList.add(new IconOption("ic_cat_salary", R.drawable.ic_cat_salary));
        iconList.add(new IconOption("ic_cat_bonus", R.drawable.ic_cat_bonus));
        iconList.add(new IconOption("ic_cat_health", R.drawable.ic_cat_health));
        iconList.add(new IconOption("ic_cat_education", R.drawable.ic_cat_education));
        iconList.add(new IconOption("ic_cat_house", R.drawable.ic_cat_house));
        iconList.add(new IconOption("ic_cat_phone", R.drawable.ic_cat_phone));
        iconList.add(new IconOption("ic_cat_coffee", R.drawable.ic_cat_coffee));
        iconList.add(new IconOption("ic_cat_cake", R.drawable.ic_cat_cake));
        iconList.add(new IconOption("ic_cat_camera", R.drawable.ic_cat_camera));
        iconList.add(new IconOption("ic_cat_dress", R.drawable.ic_cat_dress));
        iconList.add(new IconOption("ic_cat_haircut", R.drawable.ic_cat_haircut));
        iconList.add(new IconOption("ic_cat_handbag", R.drawable.ic_cat_handbag));
        iconList.add(new IconOption("ic_cat_game", R.drawable.ic_cat_game));
        iconList.add(new IconOption("ic_cat_film", R.drawable.ic_cat_film));
        iconList.add(new IconOption("ic_cat_laptop", R.drawable.ic_cat_laptop));
        iconList.add(new IconOption("ic_cat_watch", R.drawable.ic_cat_watch));
        iconList.add(new IconOption("ic_cat_ring", R.drawable.ic_cat_ring));

        IconOptionAdapter adapter = new IconOptionAdapter(iconList, selectedIcon, iconOption -> {
            selectedIcon = iconOption.getIconName();
        });

        rvIcons.setLayoutManager(new GridLayoutManager(this, 4));
        rvIcons.setAdapter(adapter);
    }

    private void setupColorRecyclerView() {
        List<Integer> colorList = new ArrayList<>();

        colorList.add(0xFFF7E46B);
        colorList.add(0xFFF2C4B7);
        colorList.add(0xFFF88C92);
        colorList.add(0xFFE7B1DC);
        colorList.add(0xFFE3B7F1);

        colorList.add(0xFFFF870A);
        colorList.add(0xFFFF120C);
        colorList.add(0xFFF64B8C);
        colorList.add(0xFFD94CB3);
        colorList.add(0xFFD252E5);

        colorList.add(0xFFC87900);
        colorList.add(0xFFD10012);
        colorList.add(0xFFB93E56);
        colorList.add(0xFFC70686);
        colorList.add(0xFFA81ABC);

        colorList.add(0xFFF1F45A);
        colorList.add(0xFFD2F05D);
        colorList.add(0xFFCDEAA0);
        colorList.add(0xFF8FDEC7);
        colorList.add(0xFF61DBE5);

        ColorOptionAdapter adapter = new ColorOptionAdapter(colorList, selectedColor, colorValue -> {
            selectedColor = colorValue;
        });

        rvColors.setLayoutManager(new GridLayoutManager(this, 5));
        rvColors.setAdapter(adapter);
    }

    private void setupEvents() {
        btnBack.setOnClickListener(v -> finish());

        edtCategoryName.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateSaveButtonState();
            }

            @Override
            public void afterTextChanged(android.text.Editable s) { }
        });

        btnSave.setOnClickListener(v -> submitCategory());
    }

    private void updateSaveButtonState() {
        boolean enabled = !TextUtils.isEmpty(edtCategoryName.getText().toString().trim());
        btnSave.setEnabled(enabled);

        if (enabled) {
            btnSave.setTextColor(0xFFFFFFFF);
            btnSave.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFF5E4BB7));
        } else {
            btnSave.setTextColor(0xFFBDBDBD);
            btnSave.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFF3E3E3E));
        }
    }

    private void submitCategory() {
        String categoryName = edtCategoryName.getText().toString().trim();

        if (TextUtils.isEmpty(categoryName)) {
            Toast.makeText(this, "Vui lòng nhập tên danh mục", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_MODE, mode);
        resultIntent.putExtra(EXTRA_POSITION, position);
        resultIntent.putExtra(EXTRA_ID, categoryId);
        resultIntent.putExtra(EXTRA_NAME, categoryName);
        resultIntent.putExtra(EXTRA_TYPE, currentType);
        resultIntent.putExtra(EXTRA_ICON, selectedIcon);
        resultIntent.putExtra(EXTRA_COLOR, selectedColor);

        setResult(RESULT_OK, resultIntent);
        finish();
    }
}