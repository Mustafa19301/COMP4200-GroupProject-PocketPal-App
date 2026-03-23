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
                "password TEXT, "+
                "balance REAL)";
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

    public boolean hasBalance(int userId){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT balance FROM users WHERE _id=?", new String[]{String.valueOf(userId)});
        cursor.moveToFirst();
        boolean exists = !cursor.isNull(cursor.getColumnIndexOrThrow("balance"));
        cursor.close();
        return exists;
    }
    public boolean setInitialBalance(int userId, double balance){
        if(balance<0 || hasBalance(userId)){
            return false;
        }
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("balance",balance);
        int rows = db.update("users", values, "_id=?", new String[]{String.valueOf(userId)});
        return rows > 0;
    }

    public double getBalance(int userId){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT balance FROM users WHERE _id=?", new String[]{String.valueOf(userId)});
        cursor.moveToFirst();
        double balance = cursor.getDouble(cursor.getColumnIndexOrThrow("balance"));
        cursor.close();
        return balance;
    }

    public boolean deductBalance(int userId, double amount){
        SQLiteDatabase db = getWritableDatabase();
        double currentBalance = getBalance(userId);
        ContentValues values = new ContentValues();
        values.put("balance", currentBalance-amount);
        int rows = db.update("users", values, "_id=?", new String[]{String.valueOf(userId)});
        return rows>0;
    }

    public boolean addBalance(int userId,double amount){
        SQLiteDatabase db = getWritableDatabase();
        double currentBalance = getBalance(userId);
        ContentValues values = new ContentValues();
        values.put("balance", currentBalance+amount);
        int rows = db.update("users", values, "_id=?", new String[]{String.valueOf(userId)});
        return rows>0;
    }
}
