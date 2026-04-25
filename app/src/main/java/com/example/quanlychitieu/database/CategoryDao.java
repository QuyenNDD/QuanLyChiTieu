package com.example.quanlychitieu.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlychitieu.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryDao {

    private final DBHelper dbHelper;

    public CategoryDao(Context context) {
        dbHelper = new DBHelper(context);
    }

    public long insertCategory(Category category) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        long now = System.currentTimeMillis();

        values.put("user_id", category.getUserId());
        values.put("name", category.getName());
        values.put("type", category.getType());
        values.put("icon", category.getIcon());
        values.put("color_value", category.getColorValue());
        values.put("created_at", now);
        values.put("updated_at", now);

        long result = db.insert("categories", null, values);
        db.close();
        return result;
    }

    public int updateCategory(Category category) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("name", category.getName());
        values.put("type", category.getType());
        values.put("icon", category.getIcon());
        values.put("color_value", category.getColorValue());
        values.put("updated_at", System.currentTimeMillis());

        int result = db.update(
                "categories",
                values,
                "id = ? AND user_id = ?",
                new String[]{
                        String.valueOf(category.getId()),
                        String.valueOf(category.getUserId())
                }
        );

        db.close();
        return result;
    }

    public int deleteCategory(int categoryId, int userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int result = db.delete(
                "categories",
                "id = ? AND user_id = ?",
                new String[]{
                        String.valueOf(categoryId),
                        String.valueOf(userId)
                }
        );

        db.close();
        return result;
    }

    public List<Category> getCategoriesByType(int userId, String type) {
        List<Category> categoryList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id, user_id, name, type, icon, color_value, created_at, updated_at " +
                        "FROM categories WHERE user_id = ? AND type = ? ORDER BY id ASC",
                new String[]{String.valueOf(userId), type}
        );

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                int ownerUserId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String categoryType = cursor.getString(cursor.getColumnIndexOrThrow("type"));
                String icon = cursor.getString(cursor.getColumnIndexOrThrow("icon"));
                int colorValue = cursor.getInt(cursor.getColumnIndexOrThrow("color_value"));
                long createdAt = cursor.getLong(cursor.getColumnIndexOrThrow("created_at"));
                long updatedAt = cursor.getLong(cursor.getColumnIndexOrThrow("updated_at"));

                Category category = new Category(
                        id,
                        ownerUserId,
                        name,
                        categoryType,
                        icon,
                        colorValue,
                        createdAt,
                        updatedAt
                );

                categoryList.add(category);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return categoryList;
    }
}