package com.example.quanlychitieu.model;

public class Transaction {
    private int id;
    private int userId;
    private int categoryId;
    private double amount;
    private String note;
    private String transactionDate;
    private String type;
    private String createdAt;

    public Transaction() {
    }

    public Transaction(int id, int userId, int categoryId, double amount,
                       String note, String transactionDate, String type, String createdAt) {
        this.id = id;
        this.userId = userId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.note = note;
        this.transactionDate = transactionDate;
        this.type = type;
        this.createdAt = createdAt;
    }

    public Transaction(int userId, int categoryId, double amount,
                       String note, String transactionDate, String type) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.note = note;
        this.transactionDate = transactionDate;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public double getAmount() {
        return amount;
    }

    public String getNote() {
        return note;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public String getType() {
        return type;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}