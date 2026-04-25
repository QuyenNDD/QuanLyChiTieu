package com.example.quanlychitieu;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // Áp dụng Cỡ chữ (Phần 2.3)
        SharedPreferences prefs = getSharedPreferences("AppSettings", MODE_PRIVATE);
        float fontScale = prefs.getFloat("font_scale", 1.0f);
        Configuration config = getResources().getConfiguration();
        config.fontScale = fontScale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Áp dụng Màu nền (Phần 2.2)
        SharedPreferences prefs = getSharedPreferences("AppSettings", MODE_PRIVATE);
        int themeColor = prefs.getInt("theme_color", Color.WHITE);
        View rootView = findViewById(android.R.id.content);
        if (rootView != null) {
            rootView.setBackgroundColor(themeColor);
        }
    }
}
