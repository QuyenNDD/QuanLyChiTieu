package com.example.quanlychitieu.model;

public class ReportItem {
    private int categoryId;
    private String categoryName;
    private double amount;
    private double percentage;
    private int color;
    private String date;
    private String note;
    private String icon;

    // Constructor đầy đủ nhất (Dùng cho mọi trường hợp)
    public ReportItem(int categoryId, String categoryName, double amount, double percentage, int color) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.amount = amount;
        this.percentage = percentage;
        this.color = color;
    }

    // Constructor rút gọn (Dùng cho PieChart trang Report)
    public ReportItem(String categoryName, double amount, double percentage, int color) {
        this.categoryName = categoryName;
        this.amount = amount;
        this.percentage = percentage;
        this.color = color;
    }

    // Getter và Setter cho CategoryId
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    // Getter và Setter cho CategoryName
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    // Getter và Setter cho Amount
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    // Getter và Setter cho Percentage
    public double getPercentage() { return percentage; }
    public void setPercentage(double percentage) { this.percentage = percentage; }

    // Getter và Setter cho Color
    public int getColor() { return color; }
    public void setColor(int color) { this.color = color; }

    // Getter và Setter cho Date (Dùng cho hiển thị Header trong Adapter)
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    // Getter và Setter cho Note (Dùng cho hiển thị mô tả giao dịch)
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    // Getter và Setter cho Icon (Dùng để lấy hình ảnh từ drawable)
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
}