package com.example.comp4200_groupproject_app;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExpenseListActivity extends AppCompatActivity {
    TextView textviewbottom, textviewtop, textviewtitle;
    Button button;
    ImageView imageviewlogo;
    RecyclerView recyclerView;
    ArrayList<Expense> list;
    ExpenseAdapter adapter;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_list);

        recyclerView = findViewById(R.id.recyclerviewexpense);
        textviewbottom = findViewById(R.id.textviewbottom);
        textviewtop = findViewById(R.id.textviewtop);
        textviewtitle = findViewById(R.id.textviewtitle);
        button = findViewById(R.id.button);
        imageviewlogo = findViewById(R.id.imageviewlogo);
        dbHelper = new DBHelper(this, "PocketPalUsers_database", null, 1);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExpenseListActivity.this, Dashboard.class);
                startActivity(intent);
            }
        });

        list = new ArrayList<>();
        adapter = new ExpenseAdapter(this, list);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        loadExpenses();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadExpenses();
    }

    public void loadExpenses(){
        list.clear();

        Cursor cursor = dbHelper.getallexpenses();

        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String category = cursor.getString(cursor.getColumnIndexOrThrow("category"));
                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow("amount"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));

                list.add(new Expense(id, title, category, amount, date));
            }
            cursor.close();
        }

        adapter.notifyDataSetChanged();
    }
}