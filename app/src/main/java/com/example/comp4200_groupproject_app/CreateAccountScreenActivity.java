package com.example.comp4200_groupproject_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CreateAccountScreenActivity extends AppCompatActivity {
    EditText firstnameview, lastnameview, emailview, passwordview;
    ImageView imagelogoview;
    Button createbutton;
    TextView haveanaccount;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account_screen);

        firstnameview = findViewById(R.id.firstnameview);
        lastnameview = findViewById(R.id.lastnameview);
        emailview = findViewById(R.id.emailview);
        passwordview = findViewById(R.id.passwordview);
        imagelogoview = findViewById(R.id.imagelogoview);
        createbutton = findViewById(R.id.createbutton);
        haveanaccount = findViewById(R.id.haveanaccount);
        dbHelper = new DBHelper(this, "PocketPalUsers_database", null, 1);
        dbHelper.getReadableDatabase();

        createbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstname = firstnameview.getText().toString();
                String lastname = lastnameview.getText().toString();
                String email = emailview.getText().toString();
                String password = passwordview.getText().toString();

                if(firstname.isEmpty() || lastname.isEmpty() || email.isEmpty() || password.isEmpty()){
                    Toast.makeText(CreateAccountScreenActivity.this, "Please fill all required fields", Toast.LENGTH_LONG).show();
                }

                else {
                    long result = dbHelper.adduser(firstname, lastname, email, password);

                    if(result != -1){
                        Toast.makeText(CreateAccountScreenActivity.this, "Account has been created", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CreateAccountScreenActivity.this, LoginScreenActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(CreateAccountScreenActivity.this, "Account already exists!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        haveanaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAccountScreenActivity.this, LoginScreenActivity.class);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}