package com.example.quanlychitieu.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;

import com.example.quanlychitieu.model.ReportItem;

import java.util.ArrayList;
import java.util.List;

public class TransactionDao {
    private DBHelper dbHelper;

    public TransactionDao(Context context) {
        dbHelper = new DBHelper(context);
    }

    // Hàm lấy dữ liệu báo cáo theo tháng và năm
    public List<ReportItem> getReportData(int month, int year, String type) {
        List<ReportItem> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // 1. Tạo mẫu tìm kiếm: ví dụ "2026-04-%"
        String monthStr = String.format("%02d", month);
        String datePattern = year + "-" + monthStr + "-%";

        // 2. Tính TỔNG CỘNG của cả tháng trước để chia tỉ lệ %
        double grandTotal = 0;
        Cursor cursorTotal = db.rawQuery(
                "SELECT SUM(amount) FROM transactions WHERE transaction_date LIKE ? AND type = ?",
                new String[]{datePattern, type});
        if (cursorTotal.moveToFirst()) {
            grandTotal = cursorTotal.getDouble(0);
        }
        cursorTotal.close();

        // 3. Truy vấn gom nhóm theo Danh mục
        String sql = "SELECT c.name, SUM(t.amount) as total " +
                "FROM transactions t " +
                "JOIN categories c ON t.category_id = c.id " +
                "WHERE t.transaction_date LIKE ? AND t.type = ? " +
                "GROUP BY c.name " +
                "ORDER BY total DESC";

        Cursor cursor = db.rawQuery(sql, new String[]{datePattern, type});

        // 4. Mảng màu sắc cho biểu đồ (Bạn có thể tùy chỉnh thêm)
        int[] colorPalette = {
                Color.parseColor("#FF5722"), Color.parseColor("#2196F3"),
                Color.parseColor("#4CAF50"), Color.parseColor("#FFEB3B"),
                Color.parseColor("#9C27B0"), Color.parseColor("#00BCD4")
        };
        int colorIndex = 0;

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                double amount = cursor.getDouble(1);

                // Tính phần trăm
                double percentage = (grandTotal > 0) ? (amount / grandTotal * 100) : 0;

                // Lấy màu từ mảng (xoay vòng nếu quá nhiều danh mục)
                int color = colorPalette[colorIndex % colorPalette.length];

                list.add(new ReportItem(name, amount, percentage, color));
                colorIndex++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    public double getTotalAmountByMonth(int month, int year, String type) {
        double total = 0;
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String monthStr = String.format("%02d", month);
        String datePattern = year + "-" + monthStr + "-%";

        Cursor cursor = db.rawQuery(
                "SELECT SUM(amount) FROM transactions WHERE transaction_date LIKE ? AND type = ?",
                new String[]{datePattern, type});

        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        cursor.close();
        return total;
    }
}
