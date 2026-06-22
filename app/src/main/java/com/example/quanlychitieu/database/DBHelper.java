package com.example.quanlychitieu.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

public class DBHelper extends SQLiteOpenHelper {

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

        //Ngân sách
        // 5. Bảng Budgets (Ngân sách)
        String createTableBudgets = "CREATE TABLE budgets (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER NOT NULL, " +
                "category_id INTEGER NOT NULL, " +
                "month INTEGER NOT NULL, " +
                "year INTEGER NOT NULL, " +
                "limit_amount REAL NOT NULL, " +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY(user_id) REFERENCES users(id), " +
                "FOREIGN KEY(category_id) REFERENCES categories(id))";
        db.execSQL(createTableBudgets);

//         Kích hoạt nạp dữ liệu mẫu
//        insertSampleData(db);
    }
//        db.execSQL("INSERT OR IGNORE INTO users (id, username, password, email) " +
//
//
//
//        db.execSQL("INSERT INTO transactions (user_id, category_id, amount, note, transaction_date, type) " +
//        db.execSQL("INSERT INTO transactions (user_id, category_id, amount, note, transaction_date, type) " +
//        db.execSQL("INSERT INTO transactions (user_id, category_id, amount, note, transaction_date, type) " +
//        db.execSQL("INSERT INTO transactions (user_id, category_id, amount, note, transaction_date, type) " +
//        db.execSQL("INSERT INTO transactions (user_id, category_id, amount, note, transaction_date, type) " +
//        db.execSQL("INSERT INTO transactions (user_id, category_id, amount, note, transaction_date, type) " +
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