package com.example.comp4200_groupproject_app;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity {

    Button addExpense, expenseList;
    TextView tv_expenses, tv_income, tv_balance;
    DBHelper dbHelper;
    ArrayList<Expense> expenses;
    ExpenseAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        addExpense = findViewById(R.id.button_addexpenses);
        expenseList = findViewById(R.id.button_viewexpenses);
        tv_expenses = findViewById(R.id.tv_totalexpenses);
        tv_income = findViewById(R.id.tv_totalincome);
        tv_balance = findViewById(R.id.tv_totalbalance);
        recyclerView = findViewById(R.id.recyclerviewexpense);
        dbHelper = new DBHelper(this, "PocketPalUsers_database", null, 1);

        addExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, AddExpenseActivity.class);
                startActivity(intent);
            }
        });
        expenseList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, ExpenseListActivity.class);
                startActivity(intent);
            }
        });

        expenses = new ArrayList<>();
        adapter = new ExpenseAdapter(this, expenses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        getExpenses();

    }

    @Override
    protected void onResume(){
        super.onResume();
        getExpenses();
    }

    public void getExpenses(){
        expenses.clear();
        Cursor cursor = dbHelper.getallexpenses();
        double total =0.0;

        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String category = cursor.getString(cursor.getColumnIndexOrThrow("category"));
                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow("amount"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));

                total+=amount;

                expenses.add(new Expense(id, title, category, amount, date));
            }
            cursor.close();
        }

        adapter.notifyDataSetChanged();
        tv_expenses.setText(Double.toString(total));
    }
}