package com.example.quanlychitieu.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlychitieu.model.Transaction;

import java.util.ArrayList;
import java.util.List;

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
                        "FROM transactions WHERE user_id = ? " +
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
}