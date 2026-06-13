package com.example.quanlychitieu.database;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.example.quanlychitieu.model.Budget;

import java.util.ArrayList;
import java.util.List;

public class BudgetDao {
    private SQLiteDatabase db;

    public BudgetDao(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    // 1. Thêm ngân sách mới
    public boolean insertBudget(int userId, int categoryId, int month, int year, double limitAmount) {
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("category_id", categoryId);
        values.put("month", month);
        values.put("year", year);
        values.put("limit_amount", limitAmount);

        long result = db.insert("budgets", null, values);
        return result != -1;
    }

    // 2. Lấy danh sách ngân sách kèm số tiền ĐÃ TIÊU THỰC TẾ
    public List<Budget> getBudgetsByMonth(int userId, int month, int year) {
        List<Budget> list = new ArrayList<>();

        // Định dạng tháng có 2 chữ số (vd: "04") để khớp với định dạng ngày YYYY-MM-DD
        String monthStr = String.format("%02d", month);
        String yearStr = String.valueOf(year);

        // Truy vấn lồng (Subquery) để tự động tính tổng tiền đã tiêu
        String query = "SELECT b.id, b.category_id, c.name AS category_name, c.icon, b.limit_amount, " +
                "(SELECT COALESCE(SUM(amount), 0) FROM transactions t " +
                " WHERE t.category_id = b.category_id " +
                " AND t.user_id = b.user_id " +
                " AND t.type = 'EXPENSE' " +
                " AND strftime('%m', t.transaction_date) = ? " +
                " AND strftime('%Y', t.transaction_date) = ?) AS spent_amount " +
                "FROM budgets b " +
                "INNER JOIN categories c ON b.category_id = c.id " +
                "WHERE b.user_id = ? AND b.month = ? AND b.year = ?";

        Cursor cursor = db.rawQuery(query, new String[]{monthStr, yearStr, String.valueOf(userId), String.valueOf(month), String.valueOf(year)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                int catId = cursor.getInt(cursor.getColumnIndexOrThrow("category_id"));
                String catName = cursor.getString(cursor.getColumnIndexOrThrow("category_name"));
                String icon = cursor.getString(cursor.getColumnIndexOrThrow("icon"));
                double limit = cursor.getDouble(cursor.getColumnIndexOrThrow("limit_amount"));
                double spent = cursor.getDouble(cursor.getColumnIndexOrThrow("spent_amount"));

                list.add(new Budget(id, catId, catName, icon, spent, limit));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    // 3. Helper: Lấy danh sách Category loại EXPENSE để đưa vào Spinner
    public Cursor getExpenseCategories(int userId) {
        return db.rawQuery("SELECT id as _id, name FROM categories WHERE user_id = ? AND type = 'EXPENSE'",
                new String[]{String.valueOf(userId)});
    }
}