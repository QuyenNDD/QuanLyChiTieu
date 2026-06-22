package com.example.quanlychitieu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlychitieu.activity.LoginActivity;
import com.example.quanlychitieu.database.UserDao;
import com.example.quanlychitieu.model.User;
import com.example.quanlychitieu.preference.SessionManager;

public class ProfileDetailActivity extends BaseActivity {

    private TextView tvDetailID, tvDetailUsername;
    private ImageButton btnBack;
    private Button btnLogout;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);

        // 1. Ánh xạ các View khớp 100% với file XML chạy đúng của bạn
        initViews();

        // 2. Khởi tạo Database
        userDao = new UserDao(this);

        // 3. Hiển thị thông tin người dùng từ SQLite dựa trên Session thật
        loadUserProfile();

        // 4. Thiết lập sự kiện click cho các nút
        setEvents();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnLogout = findViewById(R.id.btnLogout);
        tvDetailID = findViewById(R.id.tvDetailID);
        tvDetailUsername = findViewById(R.id.tvDetailUsername);
    }

    private void loadUserProfile() {
        // Kiểm tra phiên đăng nhập bằng SessionManager để tránh lỗi lệch file lưu trữ
        if (SessionManager.isLoggedIn(this)) {
            int userId = SessionManager.getCurrentUserId(this);
            User user = userDao.getUserById(userId);
            if (user != null) {
                // Đổ dữ liệu thật lên giao diện định dạng 001, 002...
                tvDetailID.setText(String.format("%03d", user.getId()));
                tvDetailUsername.setText(user.getFullName());
            }
        } else {
            Toast.makeText(this, "Lỗi: Không tìm thấy phiên đăng nhập!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    private void setEvents() {
        // Sự kiện click nút quay lại dạng ImageButton
        btnBack.setOnClickListener(v -> finish());

        // Sự kiện click nút Đăng xuất dạng Button màu đỏ lớn
        btnLogout.setOnClickListener(v -> performLogout());
    }

    private void performLogout() {
        // 1. Gọi SessionManager xóa sạch file cấu hình "app_session"
        SessionManager.clearCurrentUser(this);

        Toast.makeText(this, "Đã đăng xuất thành công", Toast.LENGTH_SHORT).show();

        // 2. Khởi hành chuyến xe Intent quay về LoginActivity và xóa sạch lịch sử
        Intent intent = new Intent(ProfileDetailActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}