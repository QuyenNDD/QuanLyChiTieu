package com.example.quanlychitieu.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

public class DBHelper extends SQLiteOpenHelper {

    // Tên và phiên bản Database
    private static final String DATABASE_NAME = "QuanLyChiTieu.db";
    private static final int DATABASE_VERSION = 1;

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

        // 2. Bảng Categories
        String createTableCategories = "CREATE TABLE categories (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "type TEXT NOT NULL, " +
                "icon TEXT, " +
                "color_value INTEGER, " +
                "created_at INTEGER, " +
                "updated_at INTEGER" +
                ")";
        db.execSQL(createTableCategories);

        // 3. Bảng Transactions (Quan trọng cho Report)
        String createTableTransactions = "CREATE TABLE transactions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "category_id INTEGER, " +
                "amount REAL, " +
                "note TEXT, " +
                "transaction_date TEXT, " + // Định dạng YYYY-MM-DD
                "type TEXT, " +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY(user_id) REFERENCES users(id), " +
                "FOREIGN KEY(category_id) REFERENCES categories(id))";
        db.execSQL(createTableTransactions);

        // 4. Bảng Password Reset OTPs (Bản đầy đủ bạn vừa chốt)
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
//        insertSampleData(db);
    }
    // Hàm thêm dữ liệu mẫu để kiểm tra báo cáo
//    public void insertSampleData(@NonNull SQLiteDatabase db) {
//        // 1. Thêm User mẫu (ID sẽ là 1)
//        db.execSQL("INSERT OR IGNORE INTO users (id, username, password, email) " +
//                "VALUES (1, 'user_test', '123', 'test@gmail.com')");
//
//        // 2. Thêm các Danh mục mẫu (Categories)
//        // Chi tiêu
//        db.execSQL("INSERT OR IGNORE INTO categories (id, user_id, name, type) VALUES (1, 1, 'Ăn uống', 'EXPENSE')");
//        db.execSQL("INSERT OR IGNORE INTO categories (id, user_id, name, type) VALUES (2, 1, 'Di chuyển', 'EXPENSE')");
//        db.execSQL("INSERT OR IGNORE INTO categories (id, user_id, name, type) VALUES (3, 1, 'Mua sắm', 'EXPENSE')");
//        // Thu nhập
//        db.execSQL("INSERT OR IGNORE INTO categories (id, user_id, name, type) VALUES (4, 1, 'Lương', 'INCOME')");
//        db.execSQL("INSERT OR IGNORE INTO categories (id, user_id, name, type) VALUES (5, 1, 'Thưởng', 'INCOME')");
//
//        // 3. Thêm các Giao dịch mẫu (Transactions) cho tháng 04/2026
//        // (Lưu ý: transaction_date phải đúng định dạng YYYY-MM-DD)
//
//        // Chi tiêu tháng 4
//        db.execSQL("INSERT INTO transactions (user_id, category_id, amount, note, transaction_date, type) " +
//                "VALUES (1, 1, 450000, 'Ăn trưa', '2026-04-10', 'EXPENSE')");
//        db.execSQL("INSERT INTO transactions (user_id, category_id, amount, note, transaction_date, type) " +
//                "VALUES (1, 1, 1200000, 'Đi ăn tiệc', '2026-04-12', 'EXPENSE')");
//        db.execSQL("INSERT INTO transactions (user_id, category_id, amount, note, transaction_date, type) " +
//                "VALUES (1, 2, 300000, 'Đổ xăng', '2026-04-13', 'EXPENSE')");
//        db.execSQL("INSERT INTO transactions (user_id, category_id, amount, note, transaction_date, type) " +
//                "VALUES (1, 3, 2500000, 'Mua điện thoại mới', '2026-04-14', 'EXPENSE')");
//
//        // Thu nhập tháng 4
//        db.execSQL("INSERT INTO transactions (user_id, category_id, amount, note, transaction_date, type) " +
//                "VALUES (1, 4, 15000000, 'Lương tháng 4', '2026-04-05', 'INCOME')");
//        db.execSQL("INSERT INTO transactions (user_id, category_id, amount, note, transaction_date, type) " +
//                "VALUES (1, 5, 2000000, 'Thưởng dự án', '2026-04-15', 'INCOME')");
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