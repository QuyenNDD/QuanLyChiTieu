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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.quanlychitieu.ProfileDetailActivity;
import com.example.quanlychitieu.R;
import com.example.quanlychitieu.activity.LoginActivity;
import com.example.quanlychitieu.database.UserDao;
import com.example.quanlychitieu.model.User;
import com.example.quanlychitieu.preference.SessionManager;

public class MoreFragment extends Fragment {

    private TextView tvUsername, tvUserID, menuProfile, menuTheme, menuFontSize, menuLogout;
    private UserDao userDao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);

        userDao = new UserDao(getActivity());

        initViews(view);

        menuProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProfileDetailActivity.class);
            startActivity(intent);
        });

        menuTheme.setOnClickListener(v -> showThemeDialog());

        menuFontSize.setOnClickListener(v -> showFontSizeDialog());

        menuLogout.setOnClickListener(v -> performLogout());

        return view;
    }

    private void initViews(View view) {
        tvUsername = view.findViewById(R.id.tvMainUsername);
        tvUserID = view.findViewById(R.id.tvMainID);
        menuProfile = view.findViewById(R.id.menuProfile);
        menuTheme = view.findViewById(R.id.menuTheme);
        menuFontSize = view.findViewById(R.id.menuFontSize);
        menuLogout = view.findViewById(R.id.menuLogout);
    }

    /**
     * Đồng bộ cài đặt màu nền và làm tươi dữ liệu Header theo vòng đời onResume()
     */
    @Override
    public void onResume() {
        super.onResume();

        if (getView() != null && getActivity() != null) {
            SharedPreferences settings = getActivity().getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
            int themeColor = settings.getInt("theme_color", Color.WHITE);
            getView().setBackgroundColor(themeColor);
        }

        loadHeaderData();
    }

    private void loadHeaderData() {
        if (getActivity() == null) return;

        // Lấy ID người dùng hiện tại an toàn qua SessionManager
        int userId = SessionManager.getCurrentUserId(getActivity());
        User user = userDao.getUserById(userId);
        if (user != null) {
            tvUsername.setText(user.getFullName());
            tvUserID.setText(String.format("ID: %03d", user.getId()));
        }
    }

    private void showThemeDialog() {
        if (getActivity() == null) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_color_picker, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        Button btnWhite = dialogView.findViewById(R.id.btnColorWhite);
        Button btnBlue = dialogView.findViewById(R.id.btnColorBlue);
        Button btnGray = dialogView.findViewById(R.id.btnColorGray);

        btnWhite.setOnClickListener(v -> updateTheme(Color.WHITE, dialog));
        btnBlue.setOnClickListener(v -> updateTheme(Color.parseColor("#E3F2FD"), dialog));
        btnGray.setOnClickListener(v -> updateTheme(Color.parseColor("#F5F5F5"), dialog));

        dialog.show();
    }

    private void updateTheme(int color, AlertDialog dialog) {
        if (getActivity() == null) return;

        SharedPreferences.Editor editor = getActivity().getSharedPreferences("AppSettings", Context.MODE_PRIVATE).edit();
        editor.putInt("theme_color", color);
        editor.apply();

        dialog.dismiss();
        getActivity().recreate();
    }

    private void showFontSizeDialog() {
        if (getActivity() == null) return;

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

    private void performLogout() {
        if (getActivity() == null) return;

        SessionManager.clearCurrentUser(getActivity());

        Toast.makeText(getActivity(), "Đã đăng xuất thành công", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        getActivity().finish();
    }
}