package com.example.quanlychitieu.model;

public class Category {
    private int id;
    private String name;
    private String type; // EXPENSE hoặc INCOME
    private final boolean isEditItem;

    public Category(String name, String type) {
        this(name, type, false);
    }

    public Category(int id, String name, String type, boolean isEditItem) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.isEditItem = isEditItem;
    }

    public Category(int id, String name, String type) {
        this(id, name, type, false);
    }

    public Category(String name, String type, boolean isEditItem) {
        this.name = name;
        this.type = type;
        this.isEditItem = isEditItem;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean isEditItem() {
        return isEditItem;
    }
}
