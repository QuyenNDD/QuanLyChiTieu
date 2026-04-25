package com.example.quanlychitieu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlychitieu.database.UserDao;
import com.example.quanlychitieu.model.User;

public class ProfileDetailActivity extends BaseActivity {

    private TextView tvDetailID, tvDetailUsername;
    private ImageButton btnBack;
    private Button btnLogout;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);

        // 1. Ánh xạ các View từ XML
        initViews();

        // 2. Khởi tạo Database
        userDao = new UserDao(this);

        // 3. Hiển thị thông tin người dùng
        loadUserProfile();

        // 4. Thiết lập sự kiện click
        setEvents();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnLogout = findViewById(R.id.btnLogout);
        tvDetailID = findViewById(R.id.tvDetailID);
        tvDetailUsername = findViewById(R.id.tvDetailUsername);
    }

    private void loadUserProfile() {
        // Lấy userId từ SharedPreferences (đã lưu lúc đăng nhập)
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);

        if (userId != -1) {
            User user = userDao.getUserById(userId);
            if (user != null) {
                // Hiển thị ID định dạng 001, 002... theo yêu cầu
                tvDetailID.setText(String.format("%03d", user.getUserID()));
                // Hiển thị username (Biệt danh)
                tvDetailUsername.setText(user.getUserName());
            }
        } else {
            Toast.makeText(this, "Lỗi: Không tìm thấy phiên đăng nhập!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setEvents() {
        // Nút quay lại (Phân khu 1)
        btnBack.setOnClickListener(v -> finish());

        // Nút đăng xuất (Phân khu 3)
        btnLogout.setOnClickListener(v -> {
            performLogout();
        });
    }

    private void performLogout() {
        // 1. Xóa sạch dữ liệu userId trong SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Xóa toàn bộ session
        editor.apply();

        // 2. Thông báo và quay về màn hình Đăng nhập (LoginActivity)
        Toast.makeText(this, "Đã đăng xuất thành công", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(ProfileDetailActivity.this, LoginActivity.class);
        // Quan trọng: Xóa sạch các Activity cũ để không thể nhấn Back quay lại trang Profile
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}