package com.example.comp4200_groupproject_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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

public class LoginScreenActivity extends AppCompatActivity {
    EditText usernameview, passwordview;
    TextView donthaveaccount;
    ImageView imagelogoview;
    Button loginbutton;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_screen);

        usernameview = findViewById(R.id.usernameview);
        passwordview = findViewById(R.id.passwordview);
        donthaveaccount = findViewById(R.id.donthaveaccount);
        imagelogoview = findViewById(R.id.imagelogoview);
        loginbutton = findViewById(R.id.loginbutton);
        dbHelper = new DBHelper(this, "PocketPalUsers_database", null, 1);
        dbHelper.getReadableDatabase();

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = usernameview.getText().toString();
                String password = passwordview.getText().toString();

                Cursor cursor = dbHelper.checkuser(email, password);
                if(cursor.getCount() > 0){
                    cursor.moveToFirst();
                    int userId = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                    Toast.makeText(LoginScreenActivity.this, "Login was successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginScreenActivity.this, Dashboard.class);
                    SharedPreferences preferences = getSharedPreferences("user_session",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("userId",userId);
                    editor.apply();

                    startActivity(intent);
                }
                else {
                    Toast.makeText(LoginScreenActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

        donthaveaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginScreenActivity.this, CreateAccountScreenActivity.class);
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