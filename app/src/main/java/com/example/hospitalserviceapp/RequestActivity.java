package com.example.hospitalserviceapp;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class RequestActivity extends AppCompatActivity {

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        TextView service = findViewById(R.id.serviceText);
        EditText ward = findViewById(R.id.ward);
        EditText bed = findViewById(R.id.bed);

        db = new DatabaseHelper(this);

        service.setText(getIntent().getStringExtra("service"));

        findViewById(R.id.submitBtn).setOnClickListener(v -> {

            if(ward.getText().toString().isEmpty() ||
                    bed.getText().toString().isEmpty()){
                Toast.makeText(this,"Fill fields",Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteDatabase database = db.getWritableDatabase();

            database.execSQL("INSERT INTO requests(service,ward,bed,notes) VALUES(?,?,?,?)",
                    new Object[]{
                            service.getText().toString(),
                            ward.getText().toString(),
                            bed.getText().toString(),
                            ""
                    });

            Toast.makeText(this,"Submitted",Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}