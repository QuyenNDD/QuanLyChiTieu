package com.example.quanlychitieu.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlychitieu.model.User;

public class UserDao {
    private DBHelper dbHelper;
    public UserDao(Context context) {
        dbHelper = new DBHelper(context);
    }
    public User getUserById(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        User user = null;
        Cursor cursor = db.rawQuery("SELECT id, username, email FROM users WHERE id = ?",
                new String[]{String.valueOf(userId)});
        if (cursor != null && cursor.moveToFirst()) {
            user = new User();
            user.setUserID(cursor.getInt(0));
            user.setUserName(cursor.getString(1));
            user.setEmail(cursor.getString(2));
            cursor.close();
        }
        return user;
    }
}
