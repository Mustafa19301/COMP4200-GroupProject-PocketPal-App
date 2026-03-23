package com.example.comp4200_groupproject_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity {

    Button addExpense, expenseList, viewWishlist;
    TextView tv_balance;
    DBHelper dbHelper;
    int userId;


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
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        userId = prefs.getInt("userId", -1);
        addExpense = findViewById(R.id.button_addexpenses);
        expenseList = findViewById(R.id.button_viewexpenses);
        viewWishlist = findViewById(R.id.button_viewexpenses);
        tv_balance = findViewById(R.id.tv_totalbalance);
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



        //tv_balance.setText(String.valueOf(dbHelper.getBalance()));
        //getExpenses();

    }

    @Override
    protected void onResume(){
        super.onResume();
        if(!dbHelper.hasBalance(userId)){
            showBalanceDialog();
        }else{
            double balance = dbHelper.getBalance(userId);
            tv_balance.setText(String.format("$%.2f",balance));
        }
        //getExpenses();
    }


    private void showBalanceDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Initial Balance");

        EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setHint("Enter balance");
        builder.setView(input);
        builder.setCancelable(false);
        builder.setPositiveButton("Save",null);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v->{
            String value = input.getText().toString().trim();
            if(value.isEmpty()){
                input.setError("Balance is required");
            }
            double balance = Double.parseDouble(value);
            if(balance<0){
                input.setError("Balance cannot be negative");
                return;
            }
            boolean result = dbHelper.setInitialBalance(userId, balance);
            if (result) {
                tv_balance.setText(String.format("$%.2f",balance));
                dialog.dismiss();
            } else {
                Toast.makeText(Dashboard.this, "Balance already set or invalid", Toast.LENGTH_SHORT).show();
            }
        });

    }
}