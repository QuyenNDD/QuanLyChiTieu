package com.example.quanlychitieu.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlychitieu.utils.PasswordUtils;
import com.example.quanlychitieu.model.User;

public class UserDao {

    private final DBHelper dbHelper;

    public UserDao(Context context) {
        dbHelper = new DBHelper(context);
    }

    // Hàm đăng ký tài khoản
    public long register(String username, String password, String email) {
        if (isUsernameExists(username)) {
            return -1;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String hashedPassword = PasswordUtils.hashPassword(password);

        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", hashedPassword);
        values.put("email", email);

        long userId = db.insert("users", null, values);
        db.close();
        return userId;
    }

    // Hàm đăng nhập
    public int login(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id, password FROM users WHERE username = ?",
                new String[]{username}
        );

        int userId = -1;

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            String storedPassword = cursor.getString(1);

            boolean isValidPassword = PasswordUtils.verifyPassword(password, storedPassword);

            if (isValidPassword) {
                userId = id;
            }
        }

        cursor.close();
        db.close();

        return userId;
    }

    // Hàm kiểm tra Username đã tồn tại chưa (Đã tách riêng)
    public boolean isUsernameExists(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id FROM users WHERE username = ?",
                new String[]{username}
        );

        boolean exists = cursor.moveToFirst();

        cursor.close();
        db.close();

        return exists;
    }

    // Hàm lấy thông tin User bằng ID (Đã sửa lại hoàn chỉnh)
    public User getUserById(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        User user = null;

        Cursor cursor = db.rawQuery(
                "SELECT id, username, email FROM users WHERE id = ?",
                new String[]{String.valueOf(userId)}
        );

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                user = new User();
                user.setId(cursor.getInt(0));
                user.setFullName(cursor.getString(1)); // Lưu ý: Lấy 'username' từ DB gán vào FullName theo mẫu cũ của bạn
                user.setEmail(cursor.getString(2));
            }
            cursor.close();
        }

        db.close(); // Đảm bảo đóng kết nối database sau khi dùng xong
        return user;
    }
}