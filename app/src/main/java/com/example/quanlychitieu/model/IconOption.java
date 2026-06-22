package com.example.quanlychitieu.model;

public class IconOption {
    private final String iconName;
    private final int iconResId;

    public IconOption(String iconName, int iconResId) {
        this.iconName = iconName;
        this.iconResId = iconResId;
    }

    public String getIconName() {
        return iconName;
    }

    public int getIconResId() {
        return iconResId;
    }
}