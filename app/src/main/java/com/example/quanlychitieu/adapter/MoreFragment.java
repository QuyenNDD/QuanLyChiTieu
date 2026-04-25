package com.example.quanlychitieu.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.quanlychitieu.ProfileDetailActivity;
import com.example.quanlychitieu.R;
import com.example.quanlychitieu.database.UserDao;
import com.example.quanlychitieu.model.User;

public class MoreFragment extends Fragment {
    private TextView tvUsername, tvUserID, menuProfile, menuTheme, menuFontSize;
    private UserDao userDao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);

        userDao = new UserDao(getActivity());
        initViews(view);
        loadHeaderData();

        // 2.1 Click Hồ sơ -> Mở Activity mới
        menuProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProfileDetailActivity.class);
            startActivity(intent);
        });

        // 2.2 Click Chủ đề -> Mở Color Picker (Giả lập đơn giản)
        menuTheme.setOnClickListener(v -> showThemeDialog());

        // 2.3 Click Cỡ chữ -> Mở Font Dialog
        menuFontSize.setOnClickListener(v -> showFontSizeDialog());

        SharedPreferences settings = getActivity().getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
        int themeColor = settings.getInt("theme_color", Color.WHITE);
        view.setBackgroundColor(themeColor);

        return view;
    }
    private void initViews(View view) {
        tvUsername = view.findViewById(R.id.tvMainUsername);
        tvUserID = view.findViewById(R.id.tvMainID);
        menuProfile = view.findViewById(R.id.menuProfile);
        menuTheme = view.findViewById(R.id.menuTheme);
        menuFontSize = view.findViewById(R.id.menuFontSize);
    }

    private void loadHeaderData() {
        SharedPreferences prefs = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);
        User user = userDao.getUserById(userId);
        if (user != null) {
            tvUsername.setText(user.getUserName());
            tvUserID.setText(String.format("ID: %03d", user.getUserID()));
        }
    }
    // ... initViews code ...
    private void showThemeDialog() {
        // 1. Tạo Builder và nạp Layout custom
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_color_picker, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        // 2. Ánh xạ các Button trong Dialog
        Button btnWhite = dialogView.findViewById(R.id.btnColorWhite);
        Button btnBlue = dialogView.findViewById(R.id.btnColorBlue);
        Button btnGray = dialogView.findViewById(R.id.btnColorGray);

        // 3. Thiết lập sự kiện cho từng Button
        btnWhite.setOnClickListener(v -> updateTheme(Color.WHITE, dialog));
        btnBlue.setOnClickListener(v -> updateTheme(Color.parseColor("#E3F2FD"), dialog));
        btnGray.setOnClickListener(v -> updateTheme(Color.parseColor("#F5F5F5"), dialog));

        dialog.show();
    }

    // Hàm phụ trợ để lưu màu và đóng dialog
    private void updateTheme(int color, AlertDialog dialog) {
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("AppSettings", Context.MODE_PRIVATE).edit();
        editor.putInt("theme_color", color);
        editor.apply();

        dialog.dismiss(); // Đóng dialog

        // Tải lại Activity để áp dụng màu nền mới
        if (getActivity() != null) {
            getActivity().recreate();
        }
    }
    private void showFontSizeDialog() {
        String[] sizes = {"Nhỏ", "Vừa", "Lớn"};
        float[] scales = {0.85f, 1.0f, 1.25f};

        new AlertDialog.Builder(getActivity())
                .setTitle("Cỡ chữ")
                .setItems(sizes, (dialog, which) -> {
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("AppSettings", Context.MODE_PRIVATE).edit();
                    editor.putFloat("font_scale", scales[which]);
                    editor.apply();

                    getActivity().recreate();
                }).show();
    }
}
