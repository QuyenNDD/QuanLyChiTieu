package com.example.quanlychitieu.fragment;

import android.content.Intent;
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

import com.example.quanlychitieu.R;
import com.example.quanlychitieu.activity.LoginActivity;
import com.example.quanlychitieu.preference.SessionManager;

public class MoreFragment extends Fragment {

    private TextView tvCurrentUser;
    private Button btnLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_more, container, false);

        tvCurrentUser = view.findViewById(R.id.tvCurrentUser);
        btnLogout = view.findViewById(R.id.btnLogout);

        String username = SessionManager.getCurrentUsername(requireContext());

        if (username != null && !username.isEmpty()) {
            tvCurrentUser.setText("Đang đăng nhập: " + username);
        } else {
            tvCurrentUser.setText("Đang đăng nhập");
        }

        btnLogout.setOnClickListener(v -> logout());

        return view;
    }

    private void logout() {
        SessionManager.clearCurrentUser(requireContext());

        Toast.makeText(requireContext(), "Đã đăng xuất", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(requireActivity(), LoginActivity.class);

        // Xóa toàn bộ màn hình cũ, không cho bấm Back quay lại MainActivity
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
    }
}