package com.example.comp4200_groupproject_app;

public class Expense {
    int id;
    String title, category, date;
    double amount;

    public Expense(int id, String title, String category, double amount, String date) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.amount = amount;
        this.date = date;
    }
}
