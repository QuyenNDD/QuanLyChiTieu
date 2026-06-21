package com.example.quanlychitieu.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlychitieu.utils.PasswordUtils;

public class UserDao {

    private final DBHelper dbHelper;

    public UserDao(Context context) {
        dbHelper = new DBHelper(context);
    }

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
}