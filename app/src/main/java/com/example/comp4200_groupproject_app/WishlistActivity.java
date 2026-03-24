package com.example.comp4200_groupproject_app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.text.InputType;
import android.widget.LinearLayout.LayoutParams;
import android.database.Cursor;

public class WishlistActivity extends AppCompatActivity {

    DBHelper db;
    EditText nameInput, targetInput;
    Button addBtn;
    Button btn_dashboard;
    LinearLayout listContainer;

    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        userId = prefs.getInt("userId", -1);
        db = new DBHelper(this, "PocketPalUsers_database", null, 1);

        nameInput = findViewById(R.id.nameInput);
        targetInput = findViewById(R.id.targetInput);
        addBtn = findViewById(R.id.addBtn);
        listContainer = findViewById(R.id.listContainer);
        btn_dashboard = findViewById(R.id.btn_dashboard);
        btn_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });

        displayItems();
    }

    private void addItem(){
        String name = nameInput.getText().toString().trim();
        String targetStr = targetInput.getText().toString().trim();

        if(name.isEmpty() || targetStr.isEmpty()){
            Toast.makeText(this, "Enter name and target", Toast.LENGTH_SHORT).show();
            return;
        }

        double target = Double.parseDouble(targetStr);

        db.addwishlistItem(userId, name, target);

        nameInput.setText("");
        targetInput.setText("");

        displayItems();
    }

    private void displayItems(){
        listContainer.removeAllViews();

        Cursor cursor = db.getWishlist(userId);
        if(cursor.moveToFirst()){
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                double target = cursor.getDouble(cursor.getColumnIndexOrThrow("target"));
                double saved = cursor.getDouble(cursor.getColumnIndexOrThrow("saved"));

                LinearLayout card = new LinearLayout(this);
                card.setOrientation(LinearLayout.VERTICAL);
                card.setPadding(16,16,16,16);
                card.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                card.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);

                TextView title = new TextView(this);
                title.setText(name + " ($" + saved + " / $" + target + ")");
                title.setTextSize(16);
                card.addView(title);

                ProgressBar bar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
                bar.setMax((int)target);
                bar.setProgress((int)saved);
                card.addView(bar);

                LinearLayout buttons = new LinearLayout(this);
                buttons.setOrientation(LinearLayout.HORIZONTAL);

                Button addMoneyBtn = new Button(this);
                addMoneyBtn.setText("Add $");
                addMoneyBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showAddMoneyDialog(id);
                    }
                });

                Button editBtn = new Button(this);
                editBtn.setText("Edit");
                editBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showEditDialog(id, name, target);
                    }
                });

                Button delBtn = new Button(this);
                delBtn.setText("Delete");
                delBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        db.deleteWishlistItem(id);
                        displayItems();
                    }
                });

                buttons.addView(addMoneyBtn);
                buttons.addView(editBtn);
                buttons.addView(delBtn);
                card.addView(buttons);

                listContainer.addView(card);

            } while(cursor.moveToNext());
        }
        cursor.close();
    }

    private void showAddMoneyDialog(int id){
        EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setHint("Amount to add");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Savings");
        builder.setView(input);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String val = input.getText().toString().trim();
                if(!val.isEmpty()){
                    double amt = Double.parseDouble(val);
                    db.addmoney(id,amt);
                    displayItems();
                }
            }
        });
        builder.setNegativeButton("Cancel",null);
        builder.show();

    }

    private void showEditDialog(int id, String oldName, double oldTarget) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        EditText nameField = new EditText(this);
        nameField.setText(oldName);
        layout.addView(nameField);

        EditText targetField = new EditText(this);
        targetField.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        targetField.setText(String.valueOf(oldTarget));
        layout.addView(targetField);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Item");
        builder.setView(layout);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newName = nameField.getText().toString().trim();
                String targetStr = targetField.getText().toString().trim();
                if (!newName.isEmpty() && !targetStr.isEmpty()) {
                    double newTarget = Double.parseDouble(targetStr);
                    db.updatewishlist(id, newName, newTarget);
                    displayItems();
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}