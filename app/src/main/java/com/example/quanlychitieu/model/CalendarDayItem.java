package com.example.quanlychitieu.model;

public class CalendarDayItem {
    private String date;
    private int dayOfMonth;
    private boolean isCurrentMonth;
    private double expenseTotal;
    private double incomeTotal;

    public CalendarDayItem(String date, int dayOfMonth, boolean isCurrentMonth,
                           double expenseTotal, double incomeTotal) {
        this.date = date;
        this.dayOfMonth = dayOfMonth;
        this.isCurrentMonth = isCurrentMonth;
        this.expenseTotal = expenseTotal;
        this.incomeTotal = incomeTotal;
    }

    public String getDate() {
        return date;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public boolean isCurrentMonth() {
        return isCurrentMonth;
    }

    public double getExpenseTotal() {
        return expenseTotal;
    }

    public double getIncomeTotal() {
        return incomeTotal;
    }
}