package com.example.quanlychitieu.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;

import androidx.annotation.NonNull;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "QuanLyChiTieu.db";
    private static final int DATABASE_VERSION = 3;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 1. Bảng Users
        String createTableUsers = "CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT UNIQUE, " +
                "password TEXT, " +
                "email TEXT, " +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(createTableUsers);

        // 2. Bảng Categories (Đã cập nhật theo yêu cầu mới của bạn)
        String createTableCategories = "CREATE TABLE categories (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER NOT NULL, " +
                "name TEXT NOT NULL, " +
                "type TEXT NOT NULL, " +
                "icon TEXT, " +
                "color_value INTEGER, " +
                "created_at INTEGER, " +
                "updated_at INTEGER, " +
                "FOREIGN KEY(user_id) REFERENCES users(id)" +
                ")";
        db.execSQL(createTableCategories);

        // 3. Bảng Transactions
        String createTableTransactions = "CREATE TABLE transactions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "category_id INTEGER, " +
                "amount REAL, " +
                "note TEXT, " +
                "transaction_date TEXT, " + // YYYY-MM-DD
                "type TEXT, " +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY(user_id) REFERENCES users(id), " +
                "FOREIGN KEY(category_id) REFERENCES categories(id))";
        db.execSQL(createTableTransactions);

        // 4. Bảng Password Reset OTPs
        String createTableOtp = "CREATE TABLE password_reset_otps (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "contact TEXT, " +
                "otp_code_hash TEXT, " +
                "purpose TEXT, " +
                "expires_at DATETIME, " +
                "verified_at DATETIME, " +
                "used_at DATETIME, " +
                "attempt_count INTEGER DEFAULT 0, " +
                "status TEXT DEFAULT 'PENDING', " +
                "token TEXT, " +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY(user_id) REFERENCES users(id))";
        db.execSQL(createTableOtp);

//         Kích hoạt nạp dữ liệu mẫu
//        insertSampleData(db);
    }

//    public void insertSampleData(SQLiteDatabase db) {
//        // 1. Phải chèn User trước để tránh lỗi Foreign Key
//        db.execSQL("INSERT OR IGNORE INTO users (id, username, password, email) " +
//                "VALUES (1, 'user_test', '123456', 'test@gmail.com')");
//
//        // 2. Chèn Categories
//        // Dùng mã màu dạng số nguyên âm (đặc trưng của Android Color)
//        insertCategory(db, 1, 1, "Ăn uống", "EXPENSE", "ic_cat_food", 0xFFFF9800);
//        insertCategory(db, 2, 1, "Di chuyển", "EXPENSE", "ic_cat_transport", 0xFF03A9F4);
//        insertCategory(db, 3, 1, "Mua sắm", "EXPENSE", "ic_cat_shopping", 0xFFE91E63);
//        insertCategory(db, 4, 1, "Y tế", "EXPENSE", "ic_cat_health", 0xFF4CAF50);
//        insertCategory(db, 5, 1, "Giải trí", "EXPENSE", "ic_cat_entertainment", 0xFF9C27B0);
//        insertCategory(db, 6, 1, "Tiền lương", "INCOME", "ic_cat_salary", 0xFF4CAF50);
//        insertCategory(db, 7, 1, "Thưởng", "INCOME", "ic_cat_bonus", 0xFFFFC107);
//        insertCategory(db, 8, 1, "Đầu tư", "INCOME", "ic_cat_investment", 0xFF00BCD4);
//        // 3. Chèn dữ liệu Giao dịch (Transactions) mẫu
//
//        // --- THU NHẬP (INCOME) ---
//        // Ngày 05/04/2026: Nhận lương
//        db.execSQL("INSERT INTO transactions (user_id, category_id, amount, note, transaction_date, type) " +
//                "VALUES (1, 6, 15000000, 'Lương tháng 04/2026', '2026-04-05', 'INCOME')");
//
//        // Ngày 15/04/2026: Nhận thưởng
//        db.execSQL("INSERT INTO transactions (user_id, category_id, amount, note, transaction_date, type) " +
//                "VALUES (1, 7, 2000000, 'Thưởng dự án A', '2026-04-15', 'INCOME')");
//
//        // --- CHI TIÊU (EXPENSE) ---
//        // Ngày 10/04/2026: (Nhiều giao dịch cùng ngày để test nhóm theo ngày)
//        db.execSQL("INSERT INTO transactions (user_id, category_id, amount, note, transaction_date, type) " +
//                "VALUES (1, 1, 50000, 'Phở sáng', '2026-04-10', 'EXPENSE')");
//        db.execSQL("INSERT INTO transactions (user_id, category_id, amount, note, transaction_date, type) " +
//                "VALUES (1, 1, 35000, 'Cà phê Highland', '2026-04-10', 'EXPENSE')");
//        db.execSQL("INSERT INTO transactions (user_id, category_id, amount, note, transaction_date, type) " +
//                "VALUES (1, 2, 45000, 'Grab đi làm', '2026-04-10', 'EXPENSE')");
//
//        // Ngày 12/04/2026
//        db.execSQL("INSERT INTO transactions (user_id, category_id, amount, note, transaction_date, type) " +
//                "VALUES (1, 3, 500000, 'Mua giày mới', '2026-04-12', 'EXPENSE')");
//        db.execSQL("INSERT INTO transactions (user_id, category_id, amount, note, transaction_date, type) " +
//                "VALUES (1, 5, 200000, 'Xem phim rạp', '2026-04-12', 'EXPENSE')");
//
//        // Ngày 20/04/2026
//        db.execSQL("INSERT INTO transactions (user_id, category_id, amount, note, transaction_date, type) " +
//                "VALUES (1, 4, 300000, 'Mua thuốc cảm', '2026-04-20', 'EXPENSE')");
//        db.execSQL("INSERT INTO transactions (user_id, category_id, amount, note, transaction_date, type) " +
//                "VALUES (1, 1, 150000, 'Ăn tối với bạn', '2026-04-20', 'EXPENSE')");
//    }
//
//    // Hàm phụ trợ để viết code sạch hơn và tránh lỗi chuỗi SQL
//    private void insertCategory(SQLiteDatabase db, int id, int userId, String name, String type, String icon, long color) {
//        String sql = "INSERT OR IGNORE INTO categories (id, user_id, name, type, icon, color_value, created_at, updated_at) " +
//                "VALUES (?, ?, ?, ?, ?, ?, strftime('%s','now') * 1000, strftime('%s','now') * 1000)";
//
//        db.execSQL(sql, new Object[]{id, userId, name, type, icon, (int)color});
//    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS password_reset_otps");
        db.execSQL("DROP TABLE IF EXISTS transactions");
        db.execSQL("DROP TABLE IF EXISTS categories");
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }
}