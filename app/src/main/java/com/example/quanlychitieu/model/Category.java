package com.example.quanlychitieu.model;

public class Category {
    private int id;
    private int userId;
    private String name;
    private String type;
    private String icon;
    private int colorValue;
    private long createdAt;
    private long updatedAt;

    public Category() {
    }

    public Category(int userId, String name, String type, String icon, int colorValue) {
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.icon = icon;
        this.colorValue = colorValue;
    }

    public Category(int id, int userId, String name, String type, String icon, int colorValue) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.icon = icon;
        this.colorValue = colorValue;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getIcon() {
        return icon;
    }

    public int getColorValue() {
        return colorValue;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setColorValue(int colorValue) {
        this.colorValue = colorValue;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }
}