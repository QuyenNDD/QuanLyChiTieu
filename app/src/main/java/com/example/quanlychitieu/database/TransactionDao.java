package com.example.quanlychitieu.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlychitieu.model.Transaction;
import com.example.quanlychitieu.model.TransactionWithCategory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TransactionDao {

    private final DBHelper dbHelper;

    public TransactionDao(Context context) {
        dbHelper = new DBHelper(context);
    }

    public long insertTransaction(Transaction transaction) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("user_id", transaction.getUserId());
        values.put("category_id", transaction.getCategoryId());
        values.put("amount", transaction.getAmount());
        values.put("note", transaction.getNote());
        values.put("transaction_date", transaction.getTransactionDate());
        values.put("type", transaction.getType());

        long result = db.insert("transactions", null, values);
        db.close();
        return result;
    }

    public List<Transaction> getTransactionsByUser(int userId) {
        List<Transaction> transactionList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id, user_id, category_id, amount, note, transaction_date, type, created_at " +
                        "FROM transactions " +
                        "WHERE user_id = ? " +
                        "ORDER BY transaction_date DESC, id DESC",
                new String[]{String.valueOf(userId)}
        );

        if (cursor.moveToFirst()) {
            do {
                Transaction transaction = new Transaction(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("user_id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("category_id")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("amount")),
                        cursor.getString(cursor.getColumnIndexOrThrow("note")),
                        cursor.getString(cursor.getColumnIndexOrThrow("transaction_date")),
                        cursor.getString(cursor.getColumnIndexOrThrow("type")),
                        cursor.getString(cursor.getColumnIndexOrThrow("created_at"))
                );
                transactionList.add(transaction);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return transactionList;
    }

    public int deleteTransaction(int transactionId, int userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int result = db.delete(
                "transactions",
                "id = ? AND user_id = ?",
                new String[]{String.valueOf(transactionId), String.valueOf(userId)}
        );

        db.close();
        return result;
    }

    public double getMonthTotalByType(int userId, int month, int year, String type) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        double total = 0;

        String monthStr = String.format("%02d", month);
        String pattern = year + "-" + monthStr + "-%";

        Cursor cursor = db.rawQuery(
                "SELECT COALESCE(SUM(amount), 0) " +
                        "FROM transactions " +
                        "WHERE user_id = ? AND type = ? AND transaction_date LIKE ?",
                new String[]{String.valueOf(userId), type, pattern}
        );

        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }

        cursor.close();
        db.close();
        return total;
    }

    public Map<String, Double> getDailyExpenseMap(int userId, int month, int year) {
        Map<String, Double> map = new LinkedHashMap<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String monthStr = String.format("%02d", month);
        String pattern = year + "-" + monthStr + "-%";

        Cursor cursor = db.rawQuery(
                "SELECT transaction_date, SUM(amount) " +
                        "FROM transactions " +
                        "WHERE user_id = ? AND type = 'EXPENSE' AND transaction_date LIKE ? " +
                        "GROUP BY transaction_date " +
                        "ORDER BY transaction_date ASC",
                new String[]{String.valueOf(userId), pattern}
        );

        if (cursor.moveToFirst()) {
            do {
                map.put(cursor.getString(0), cursor.getDouble(1));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return map;
    }

    public Map<String, Double> getDailyIncomeMap(int userId, int month, int year) {
        Map<String, Double> map = new LinkedHashMap<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String monthStr = String.format("%02d", month);
        String pattern = year + "-" + monthStr + "-%";

        Cursor cursor = db.rawQuery(
                "SELECT transaction_date, SUM(amount) " +
                        "FROM transactions " +
                        "WHERE user_id = ? AND type = 'INCOME' AND transaction_date LIKE ? " +
                        "GROUP BY transaction_date " +
                        "ORDER BY transaction_date ASC",
                new String[]{String.valueOf(userId), pattern}
        );

        if (cursor.moveToFirst()) {
            do {
                map.put(cursor.getString(0), cursor.getDouble(1));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return map;
    }

    public Map<String, List<TransactionWithCategory>> getTransactionsGroupedByDate(int userId, int month, int year) {
        Map<String, List<TransactionWithCategory>> groupedMap = new LinkedHashMap<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String monthStr = String.format("%02d", month);
        String pattern = year + "-" + monthStr + "-%";

        String sql = "SELECT t.id, t.transaction_date, t.type, t.amount, t.note, " +
                "COALESCE(c.name, 'Không có') AS category_name, " +
                "COALESCE(c.icon, 'ic_category_deleted') AS category_icon, " +
                "COALESCE(c.color_value, -7829368) AS category_color " +
                "FROM transactions t " +
                "LEFT JOIN categories c ON t.category_id = c.id " +
                "WHERE t.user_id = ? AND t.transaction_date LIKE ? " +
                "ORDER BY t.transaction_date DESC, t.id DESC";

        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(userId), pattern});

        if (cursor.moveToFirst()) {
            do {
                TransactionWithCategory item = new TransactionWithCategory(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getDouble(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getInt(7)
                );

                if (!groupedMap.containsKey(item.getDate())) {
                    groupedMap.put(item.getDate(), new ArrayList<>());
                }
                groupedMap.get(item.getDate()).add(item);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return groupedMap;
    }
    public TransactionWithCategory getTransactionWithCategoryById(int transactionId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT t.id, t.user_id, t.category_id, t.amount, t.note, t.transaction_date, t.type, " +
                "COALESCE(c.name, 'Không có') AS category_name, " +
                "COALESCE(c.icon, 'ic_category_deleted') AS category_icon, " +
                "COALESCE(c.color_value, -7829368) AS category_color " +
                "FROM transactions t " +
                "LEFT JOIN categories c ON t.category_id = c.id " +
                "WHERE t.id = ?";

        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(transactionId)});
        TransactionWithCategory transaction = null;

        if (cursor.moveToFirst()) {
            transaction = new TransactionWithCategory();
            transaction.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            transaction.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow("user_id")));
            transaction.setCategoryId(cursor.getInt(cursor.getColumnIndexOrThrow("category_id")));
            transaction.setAmount(cursor.getDouble(cursor.getColumnIndexOrThrow("amount")));
            transaction.setNote(cursor.getString(cursor.getColumnIndexOrThrow("note")));
            transaction.setTransactionDate(cursor.getString(cursor.getColumnIndexOrThrow("transaction_date")));
            transaction.setType(cursor.getString(cursor.getColumnIndexOrThrow("type")));
            transaction.setCategoryName(cursor.getString(cursor.getColumnIndexOrThrow("category_name")));
            transaction.setCategoryIcon(cursor.getString(cursor.getColumnIndexOrThrow("category_icon")));
            transaction.setCategoryColor(cursor.getInt(cursor.getColumnIndexOrThrow("category_color")));
        }

        cursor.close();
        db.close();
        return transaction;
    }
    public int updateTransaction(Transaction transaction) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("user_id", transaction.getUserId());
        values.put("category_id", transaction.getCategoryId());
        values.put("amount", transaction.getAmount());
        values.put("note", transaction.getNote());
        values.put("transaction_date", transaction.getTransactionDate());
        values.put("type", transaction.getType());

        int result = db.update(
                "transactions",
                values,
                "id = ?",
                new String[]{String.valueOf(transaction.getId())}
        );

        db.close();
        return result;
    }
    public int deleteTransaction(int transactionId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int result = db.delete(
                "transactions",
                "id = ?",
                new String[]{String.valueOf(transactionId)}
        );
        db.close();
        return result;
    }
}