package com.example.quanlychitieu.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlychitieu.model.ReportItem;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

public class ReportDao {
    private DBHelper dbHelper;

    public ReportDao(Context context) {
        dbHelper = new DBHelper(context);
    }

    // --- DÀNH CHO TRANG REPORT (PieChart & ListView) ---
    public List<ReportItem> getReportData(int userId, int month, int year, String type) {
        List<ReportItem> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String monthStr = String.format("%02d", month);
        String datePattern = year + "-" + monthStr + "-%";

        double grandTotal = getTotalAmountByMonth(userId, month, year, type);

        String sql = "SELECT c.id, c.name, c.color_value, SUM(t.amount) as total, c.icon " +
                "FROM transactions t " +
                "JOIN categories c ON t.category_id = c.id " +
                "WHERE t.user_id = ? " +
                "AND c.user_id = ? " +
                "AND t.transaction_date LIKE ? " +
                "AND c.type = ? " +
                "GROUP BY c.id, c.name, c.color_value, c.icon " +
                "ORDER BY total DESC";

        Cursor cursor = db.rawQuery(sql, new String[]{
                String.valueOf(userId),
                String.valueOf(userId),
                datePattern,
                type
        });

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                int colorValue = cursor.getInt(2);
                double amount = cursor.getDouble(3);
                double percentage = (grandTotal > 0) ? (amount / grandTotal * 100) : 0;
                String icon = cursor.getString(4);

                ReportItem item = new ReportItem(id, name, amount, percentage, colorValue);
                item.setIcon(icon);

                list.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }

    public double getTotalAmountByMonth(int userId, int month, int year, String type) {
        double total = 0;
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String monthStr = String.format("%02d", month);
        String datePattern = year + "-" + monthStr + "-%";

        Cursor cursor = db.rawQuery(
                "SELECT SUM(amount) FROM transactions " +
                        "WHERE user_id = ? AND transaction_date LIKE ? AND type = ?",
                new String[]{
                        String.valueOf(userId),
                        datePattern,
                        type
                }
        );

        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }

        cursor.close();
        return total;
    }

    // --- DÀNH CHO TRANG CHI TIẾT DANH MỤC (BarChart & Lịch sử) ---

    public List<BarEntry> getBarChartData(int userId, int categoryId, int year) {
        List<BarEntry> entries = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT STRFTIME('%m', transaction_date) as month, SUM(amount) " +
                "FROM transactions " +
                "WHERE user_id = ? " +
                "AND category_id = ? " +
                "AND transaction_date LIKE ? " +
                "GROUP BY month";

        String yearPattern = year + "-%";

        Cursor cursor = db.rawQuery(sql, new String[]{
                String.valueOf(userId),
                String.valueOf(categoryId),
                yearPattern
        });

        float[] monthlyData = new float[13];

        if (cursor.moveToFirst()) {
            do {
                String mStr = cursor.getString(0);

                if (mStr != null) {
                    int m = Integer.parseInt(mStr);
                    monthlyData[m] = cursor.getFloat(1);
                }
            } while (cursor.moveToNext());
        }

        cursor.close();

        for (int i = 1; i <= 12; i++) {
            entries.add(new BarEntry(i, monthlyData[i]));
        }

        return entries;
    }

    public List<ReportItem> getTransactionsByCategoryDetail(int userId, int categoryId, int month, int year) {
        List<ReportItem> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String datePattern = year + "-" + String.format("%02d", month) + "-%";

        String sql = "SELECT t.amount, t.note, t.transaction_date, c.name, c.color_value, c.icon " +
                "FROM transactions t " +
                "JOIN categories c ON t.category_id = c.id " +
                "WHERE t.user_id = ? " +
                "AND c.user_id = ? " +
                "AND t.category_id = ? " +
                "AND t.transaction_date LIKE ? " +
                "ORDER BY t.transaction_date DESC";

        Cursor cursor = db.rawQuery(sql, new String[]{
                String.valueOf(userId),
                String.valueOf(userId),
                String.valueOf(categoryId),
                datePattern
        });

        if (cursor.moveToFirst()) {
            do {
                ReportItem item = new ReportItem(
                        cursor.getString(3), // categoryName
                        cursor.getDouble(0), // amount
                        0,                   // percentage không dùng ở đây
                        cursor.getInt(4)     // colorValue
                );

                item.setNote(cursor.getString(1)); // Ghi chú
                item.setDate(cursor.getString(2)); // Ngày giao dịch
                item.setIcon(cursor.getString(5)); // Icon

                list.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }
}