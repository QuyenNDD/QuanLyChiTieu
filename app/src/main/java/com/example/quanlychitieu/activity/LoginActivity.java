package com.example.quanlychitieu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlychitieu.MainActivity;
import com.example.quanlychitieu.R;
import com.example.quanlychitieu.database.UserDao;
import com.example.quanlychitieu.preference.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_REGISTER = 100;

    private EditText edtUsername, edtPassword;
    private Button btnLogin;
    private TextView tvGoToRegister;

    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (SessionManager.isLoggedIn(this)) {
            goToMain();
            return;
        }

        setContentView(R.layout.activity_login);

        userDao = new UserDao(this);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvGoToRegister = findViewById(R.id.tvGoToRegister);

        btnLogin.setOnClickListener(v -> login());

        tvGoToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivityForResult(intent, REQUEST_REGISTER);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_REGISTER && resultCode == RESULT_OK && data != null) {
            String username = data.getStringExtra("registered_username");

            if (username != null && !username.isEmpty()) {
                edtUsername.setText(username);
                edtPassword.setText("");

                Toast.makeText(this, "Đăng ký thành công, vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void login() {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (username.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập username", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập password", Toast.LENGTH_SHORT).show();
            return;
        }

        int userId = userDao.login(username, password);

        if (userId > 0) {
            SessionManager.saveCurrentUser(this, userId, username);

            Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

            goToMain();
        } else {
            Toast.makeText(this, "Sai username hoặc password", Toast.LENGTH_SHORT).show();
        }
    }

    private void goToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}