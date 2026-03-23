package com.example.comp4200_groupproject_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddExpenseActivity extends AppCompatActivity {
    DBHelper dbHelper;

    EditText editName, editAmount, editDate;
    Spinner spinner;

    Button buttonDash, buttonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        dbHelper = new DBHelper(this, "PocketPalUsers_database", null, 1);

        editName = findViewById(R.id.edittextname);
        editAmount = findViewById(R.id.edittextamount);
        editDate = findViewById(R.id.edittextdate);
        spinner = findViewById(R.id.spinner);

        buttonDash = findViewById(R.id.button);
        buttonList = findViewById(R.id.button2);

        String[] categories = {"Groceries", "Transport", "Entertainment", "Bills", "Gas", "Other"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                categories
        );

        spinner.setAdapter(adapter);

        buttonList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editName.getText().toString();
                String amountStr = editAmount.getText().toString();
                String date = editDate.getText().toString();
                String category = spinner.getSelectedItem().toString();

                if (name.isEmpty() || amountStr.isEmpty() || date.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                double amount = Double.parseDouble(amountStr);

                long result = dbHelper.addexpense(name, category, amount, date);

                if (result != -1) {
                    Toast.makeText(getApplicationContext(), "Expense Added!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(AddExpenseActivity.this, ExpenseListActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Error adding expense", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonDash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddExpenseActivity.this, Dashboard.class);
                startActivity(intent);
            }
        });
    }
}