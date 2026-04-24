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
                "id = ?",
                new String[]{String.valueOf(category.getId())}
        );

        db.close();
        return result;
    }

    public int deleteCategory(int categoryId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int result = db.delete(
                "categories",
                "id = ?",
                new String[]{String.valueOf(categoryId)}
        );

        db.close();
        return result;
    }

    public List<Category> getCategoriesByType(String type) {
        List<Category> categoryList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id, name, type, icon, color_value, created_at, updated_at " +
                        "FROM categories WHERE type = ? ORDER BY id ASC",
                new String[]{type}
        );

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String categoryType = cursor.getString(cursor.getColumnIndexOrThrow("type"));
                String icon = cursor.getString(cursor.getColumnIndexOrThrow("icon"));
                int colorValue = cursor.getInt(cursor.getColumnIndexOrThrow("color_value"));
                long createdAt = cursor.getLong(cursor.getColumnIndexOrThrow("created_at"));
                long updatedAt = cursor.getLong(cursor.getColumnIndexOrThrow("updated_at"));

                Category category = new Category(
                        id,
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

    public Category getCategoryById(int categoryId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Category category = null;

        Cursor cursor = db.rawQuery(
                "SELECT id, name, type, icon, color_value, created_at, updated_at " +
                        "FROM categories WHERE id = ?",
                new String[]{String.valueOf(categoryId)}
        );

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
            String icon = cursor.getString(cursor.getColumnIndexOrThrow("icon"));
            int colorValue = cursor.getInt(cursor.getColumnIndexOrThrow("color_value"));
            long createdAt = cursor.getLong(cursor.getColumnIndexOrThrow("created_at"));
            long updatedAt = cursor.getLong(cursor.getColumnIndexOrThrow("updated_at"));

            category = new Category(id, name, type, icon, colorValue, createdAt, updatedAt);
        }

        cursor.close();
        db.close();
        return category;
    }

    public boolean isCategoryNameExists(String name, String type) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        boolean exists = false;

        Cursor cursor = db.rawQuery(
                "SELECT id FROM categories WHERE name = ? AND type = ?",
                new String[]{name, type}
        );

        if (cursor.moveToFirst()) {
            exists = true;
        }

        cursor.close();
        db.close();
        return exists;
    }
}