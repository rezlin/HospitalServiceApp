package com.example.hospitalserviceapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText username, email, password, confirmPassword;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);

        db = new DatabaseHelper(this);

        findViewById(R.id.registerBtn).setOnClickListener(v -> {

            String user = username.getText().toString().trim();
            String mail = email.getText().toString().trim();
            String pass = password.getText().toString().trim();
            String confirm = confirmPassword.getText().toString().trim();

            // ================= VALIDATION =================

            // Empty fields
            if(user.isEmpty() || mail.isEmpty() || pass.isEmpty() || confirm.isEmpty()){
                Toast.makeText(this,"All fields are required",Toast.LENGTH_SHORT).show();
                return;
            }

            // Email format
            if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
                Toast.makeText(this,"Invalid email format",Toast.LENGTH_SHORT).show();
                return;
            }

            // Password length
            if(pass.length() < 4){
                Toast.makeText(this,"Password must be at least 4 characters",Toast.LENGTH_SHORT).show();
                return;
            }

            // Password match
            if(!pass.equals(confirm)){
                Toast.makeText(this,"Passwords do not match",Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteDatabase database = db.getWritableDatabase();

            // Check if username exists
            Cursor c = database.rawQuery(
                    "SELECT * FROM users WHERE username=?",
                    new String[]{user}
            );

            if(c.moveToFirst()){
                Toast.makeText(this,"Username already exists",Toast.LENGTH_SHORT).show();
                c.close();
                return;
            }
            c.close();

            // ================= INSERT USER =================
            database.execSQL(
                    "INSERT INTO users(username,email,password,role) VALUES(?,?,?,?)",
                    new Object[]{user, mail, pass, "user"}
            );

            Toast.makeText(this,"Registered Successfully",Toast.LENGTH_SHORT).show();

            finish();
        });
    }
}