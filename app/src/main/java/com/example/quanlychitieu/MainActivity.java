package com.example.quanlychitieu;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.quanlychitieu.fragment.TransactionFormFragment;
import com.example.quanlychitieu.utils.SimpleTextFragment;

public class MainActivity extends AppCompatActivity {

    private LinearLayout tabInput, tabCalendar, tabReport, tabBudget, tabMore;
    private ImageView iconInput, iconCalendar, iconReport, iconBudget, iconMore;
    private TextView textInput, textCalendar, textReport, textBudget, textMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        initEvents();

        if (savedInstanceState == null) {
            setSelectedTab(0);
            replaceFragment(new TransactionFormFragment());
        }
    }

    private void initViews() {
        tabInput = findViewById(R.id.tabInput);
        tabCalendar = findViewById(R.id.tabCalendar);
        tabReport = findViewById(R.id.tabReport);
        tabBudget = findViewById(R.id.tabBudget);
        tabMore = findViewById(R.id.tabMore);

        iconInput = findViewById(R.id.iconInput);
        iconCalendar = findViewById(R.id.iconCalendar);
        iconReport = findViewById(R.id.iconReport);
        iconBudget = findViewById(R.id.iconBudget);
        iconMore = findViewById(R.id.iconMore);

        textInput = findViewById(R.id.textInput);
        textCalendar = findViewById(R.id.textCalendar);
        textReport = findViewById(R.id.textReport);
        textBudget = findViewById(R.id.textBudget);
        textMore = findViewById(R.id.textMore);
    }

    private void initEvents() {
        tabInput.setOnClickListener(v -> {
            setSelectedTab(0);
            replaceFragment(new TransactionFormFragment());
        });

        tabCalendar.setOnClickListener(v -> {
            setSelectedTab(1);
            replaceFragment(SimpleTextFragment.newInstance("Màn hình Lịch"));
        });

        tabReport.setOnClickListener(v -> {
            setSelectedTab(2);
            replaceFragment(SimpleTextFragment.newInstance("Màn hình Báo cáo"));
        });

        tabBudget.setOnClickListener(v -> {
            setSelectedTab(3);
            replaceFragment(SimpleTextFragment.newInstance("Màn hình Ngân sách"));
        });

        tabMore.setOnClickListener(v -> {
            setSelectedTab(4);
            replaceFragment(SimpleTextFragment.newInstance("Màn hình Khác"));
        });
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    private void setSelectedTab(int selectedIndex) {
        int activeColor = Color.WHITE;
        int inactiveColor = Color.parseColor("#8E8E93");

        iconInput.setColorFilter(selectedIndex == 0 ? activeColor : inactiveColor);
        textInput.setTextColor(selectedIndex == 0 ? activeColor : inactiveColor);

        iconCalendar.setColorFilter(selectedIndex == 1 ? activeColor : inactiveColor);
        textCalendar.setTextColor(selectedIndex == 1 ? activeColor : inactiveColor);

        iconReport.setColorFilter(selectedIndex == 2 ? activeColor : inactiveColor);
        textReport.setTextColor(selectedIndex == 2 ? activeColor : inactiveColor);

        iconBudget.setColorFilter(selectedIndex == 3 ? activeColor : inactiveColor);
        textBudget.setTextColor(selectedIndex == 3 ? activeColor : inactiveColor);

        iconMore.setColorFilter(selectedIndex == 4 ? activeColor : inactiveColor);
        textMore.setTextColor(selectedIndex == 4 ? activeColor : inactiveColor);
    }
}