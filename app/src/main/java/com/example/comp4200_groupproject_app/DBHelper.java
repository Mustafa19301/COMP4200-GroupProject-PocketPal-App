package com.example.comp4200_groupproject_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper{
    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        String query = "CREATE TABLE users(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "firstname TEXT, " +
                "lastname TEXT, " +
                "email TEXT UNIQUE, " +
                "password TEXT)";
        sqLiteDatabase.execSQL(query);

        String expensetable = "CREATE TABLE expenses(" + "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "category TEXT, " +
                "amount REAL, " +
                "date TEXT)";
        sqLiteDatabase.execSQL(expensetable);

        String balancetable = "CREATE TABLE balance("+
                "_id INTEGER PRIMARY KEY, "+
                "balance REAL)";
        sqLiteDatabase.execSQL(balancetable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS users");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS expenses");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS balance");
        onCreate(sqLiteDatabase);
    }

    //for registering user
    public long adduser(String firstname, String lastname, String email, String password){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("firstname", firstname);
        values.put("lastname", lastname);
        values.put("email", email);
        values.put("password", password);
        return sqLiteDatabase.insert("users", null, values);
    }

    //for verifying user
    public Cursor checkuser(String email, String password){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM users WHERE email=? AND password=?",
                new String[]{email, password});
    }

    public long addexpense(String title, String category, double amount, String date){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("title", title);
        values.put("category", category);
        values.put("amount", amount);
        values.put("date", date);
        return sqLiteDatabase.insert("expenses", null, values);
    }

    public void deleteexpense(int id){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete("expenses", "_id=?", new String[]{String.valueOf(id)});
    }

    public Cursor getallexpenses(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM expenses ORDER by _id DESC", null);
    }

    public boolean hasBalance(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM balance WHERE _id=1", null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }
    public long setInitialBalance(double balance){
        if(balance<0 || hasBalance()){
            return -1;
        }
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("_id",1);
        values.put("balance",balance);
        return db.insert("balance",null, values);
    }

    public double getBalance(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT balance FROM balance WHERE _id=1", null);
        cursor.moveToFirst();
        double balance = cursor.getDouble(cursor.getColumnIndexOrThrow("balance"));
        cursor.close();
        return balance;
    }

    public boolean deductBalance(double amount){
        SQLiteDatabase db = getWritableDatabase();
        double currentBalance = getBalance();
        ContentValues values = new ContentValues();
        values.put("balance", currentBalance-amount);
        int rows = db.update("balance", values, "_id=?", new String[]{"1"});
        return rows>0;
    }

    public boolean addBalance(double amount){
        SQLiteDatabase db = getWritableDatabase();
        double currentBalance = getBalance();
        ContentValues values = new ContentValues();
        values.put("balance", currentBalance+amount);
        int rows = db.update("balance", values, "_id=?", new String[]{"1"});
        return rows>0;
    }
}
