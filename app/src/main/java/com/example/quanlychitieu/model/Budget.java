package com.example.quanlychitieu.model;

public class Budget {
    private int id;
    private int categoryId;
    private String categoryName;
    private String iconName; // Đổi thành String để map với Database của bạn
    private double spentAmount;
    private double totalLimit;

    public Budget(int id, int categoryId, String categoryName, String iconName, double spentAmount, double totalLimit) {
        this.id = id;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.iconName = iconName;
        this.spentAmount = spentAmount;
        this.totalLimit = totalLimit;
    }

    public int getId() { return id; }
    public int getCategoryId() { return categoryId; }
    public String getCategoryName() { return categoryName; }
    public String getIconName() { return iconName; }
    public double getSpentAmount() { return spentAmount; }
    public double getTotalLimit() { return totalLimit; }

    public int getPercentage() {
        if (totalLimit <= 0) return 0;
        return (int) ((spentAmount / totalLimit) * 100);
    }
}