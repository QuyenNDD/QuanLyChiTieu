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

    public Category(int id, int userId, String name, String type,
                    String icon, int colorValue, long createdAt, long updatedAt) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.icon = icon;
        this.colorValue = colorValue;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Category(int userId, String name, String type, String icon, int colorValue) {
        this(0, userId, name, type, icon, colorValue, 0, 0);
    }

    public Category(int id, int userId, String name, String type, String icon, int colorValue) {
        this(id, userId, name, type, icon, colorValue, 0, 0);
    }

    public Category(String name, String type) {
        this(0, 0, name, type, "", 0, 0, 0);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getColorValue() {
        return colorValue;
    }

    public void setColorValue(int colorValue) {
        this.colorValue = colorValue;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }
}