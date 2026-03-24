package com.example.comp4200_groupproject_app;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
public class EditExpenseActivity extends AppCompatActivity {

    EditText editName, editAmount, editDate;
    Spinner spinner;
    Button buttonUpdate;

    DBHelper dbHelper;
    int userId, expenseId;
    double oldAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        dbHelper = new DBHelper(this, "PocketPalUsers_database", null, 1);

        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        userId = prefs.getInt("userId", -1);

        editName = findViewById(R.id.edittextname);
        editAmount = findViewById(R.id.edittextamount);
        editDate = findViewById(R.id.edittextdate);
        spinner = findViewById(R.id.spinner);
        buttonUpdate = findViewById(R.id.button2);

        String[] categories = {"Groceries", "Transport", "Entertainment", "Bills", "Gas", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinner.setAdapter(adapter);

        expenseId = getIntent().getIntExtra("id", -1);
        oldAmount = getIntent().getDoubleExtra("amount", 0);

        editName.setText(getIntent().getStringExtra("title"));
        editAmount.setText(String.valueOf(oldAmount));
        editDate.setText(getIntent().getStringExtra("date"));

        String category = getIntent().getStringExtra("category");
        int pos = adapter.getPosition(category);
        spinner.setSelection(pos);

        buttonUpdate.setText("Update Expense");

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = editName.getText().toString();
                String amountStr = editAmount.getText().toString();
                String date = editDate.getText().toString();
                String newCategory = spinner.getSelectedItem().toString();

                if (name.isEmpty() || amountStr.isEmpty() || date.isEmpty()) {
                    Toast.makeText(EditExpenseActivity.this, "Fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                double newAmount = Double.parseDouble(amountStr);
                double difference = newAmount - oldAmount;

                if (difference > 0) {
                    dbHelper.deductBalance(userId, difference);
                } else if (difference < 0) {
                    dbHelper.addBalance(userId, Math.abs(difference));
                }

                boolean success = dbHelper.updateExpense(expenseId, name, newCategory, newAmount, date);

                if (success) {
                    Toast.makeText(EditExpenseActivity.this, "Expense Updated", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditExpenseActivity.this, "Update Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();

                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        EditExpenseActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                                String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                                editDate.setText(date);
                            }
                        },
                        year, month, day
                );
                datePickerDialog.show();
            }
        });
    }
}
