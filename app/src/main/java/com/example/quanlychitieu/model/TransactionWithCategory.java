package com.example.quanlychitieu.model;

public class TransactionWithCategory {
    private int id;
    private int userId;
    private int categoryId;
    private double amount;
    private String note;
    private String transactionDate;
    private String type;
    private String categoryName;
    private String categoryIcon;
    private int categoryColor;

    public TransactionWithCategory() {
    }

    public TransactionWithCategory(int id, String transactionDate, String type, double amount,
                                   String note, String categoryName, String categoryIcon, int categoryColor) {
        this.id = id;
        this.transactionDate = transactionDate;
        this.type = type;
        this.amount = amount;
        this.note = note;
        this.categoryName = categoryName;
        this.categoryIcon = categoryIcon;
        this.categoryColor = categoryColor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryIcon() {
        return categoryIcon;
    }

    public void setCategoryIcon(String categoryIcon) {
        this.categoryIcon = categoryIcon;
    }

    public int getCategoryColor() {
        return categoryColor;
    }

    public void setCategoryColor(int categoryColor) {
        this.categoryColor = categoryColor;
    }

    public String getDate() {
        return transactionDate;
    }
}