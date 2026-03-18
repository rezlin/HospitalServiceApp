package com.example.hospitalserviceapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    TextView forgotPassword;

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        forgotPassword = findViewById(R.id.forgotPassword);

        db = new DatabaseHelper(this);

        // =========================
        // 👤 USER LOGIN
        // =========================
        findViewById(R.id.loginBtn).setOnClickListener(v -> {

            String user = username.getText().toString().trim();
            String pass = password.getText().toString().trim();

            // ✅ VALIDATION

            if(user.isEmpty()){
                username.setError("Username required");
                return;
            }

            if(pass.isEmpty()){
                password.setError("Password required");
                return;
            }

            if(user.length() < 3){
                username.setError("Username too short");
                return;
            }

            if(pass.length() < 4){
                password.setError("Password must be at least 4 characters");
                return;
            }

            SQLiteDatabase database = db.getReadableDatabase();

            Cursor c = database.rawQuery(
                    "SELECT * FROM users WHERE username=? AND password=?",
                    new String[]{user, pass}
            );

            if(c.moveToFirst()){
                Toast.makeText(this,"Login Successful",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, ServicesActivity.class));
            } else {
                Toast.makeText(this,"Invalid Username or Password",Toast.LENGTH_SHORT).show();
            }

            c.close();
        });

        // =========================
        // 👑 ADMIN LOGIN
        // =========================
        findViewById(R.id.adminLoginBtn).setOnClickListener(v -> {

            String user = username.getText().toString().trim();
            String pass = password.getText().toString().trim();

            // ✅ VALIDATION

            if(user.isEmpty()){
                username.setError("Admin username required");
                return;
            }

            if(pass.isEmpty()){
                password.setError("Admin password required");
                return;
            }

            if(user.equals("admin") && pass.equals("1234")){
                Toast.makeText(this,"Admin Login Successful",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, AdminActivity.class));
            } else {
                Toast.makeText(this,"Wrong Admin Credentials",Toast.LENGTH_SHORT).show();
            }
        });

        // =========================
        // 📝 REGISTER
        // =========================
        findViewById(R.id.registerBtn).setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class))
        );

        // =========================
        // 🔑 FORGOT PASSWORD
        // =========================
        forgotPassword.setOnClickListener(v -> {
            Toast.makeText(this,"Password reset not implemented",Toast.LENGTH_SHORT).show();
        });
    }
}