package com.example.quanlychitieu.preference;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "app_session";
    private static final String KEY_CURRENT_USER_ID = "current_user_id";

    private SessionManager() {
    }

    // TẠM THỜI: dùng user_id = 1 nếu chưa có login thật
    public static int getCurrentUserId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_CURRENT_USER_ID, 1);
    }

    public static void saveCurrentUserId(Context context, int userId) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putInt(KEY_CURRENT_USER_ID, userId).apply();
    }

    public static void clearCurrentUser(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().remove(KEY_CURRENT_USER_ID).apply();
    }
}