package com.example.quanlychitieu.model;

public class ReportItem {
    private String categoryName;
    private double amount;
    private double percentage;
    private int color; // Màu sắc tương ứng với miếng bánh trên PieChart

    public ReportItem(String categoryName, double amount, double percentage, int color) {
        this.categoryName = categoryName;
        this.amount = amount;
        this.percentage = percentage;
        this.color = color;
    }

    // Getter cho các trường
    public String getCategoryName() { return categoryName; }
    public double getAmount() { return amount; }
    public double getPercentage() { return percentage; }
    public int getColor() { return color; }
}