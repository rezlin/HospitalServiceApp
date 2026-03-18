package com.example.hospitalserviceapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class ServicesActivity extends AppCompatActivity {

    ListView listView;
    DatabaseHelper db;
    ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        listView = findViewById(R.id.listView);
        db = new DatabaseHelper(this);

        loadServices();

        // Click service → go to request screen
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent i = new Intent(ServicesActivity.this, RequestActivity.class);
            i.putExtra("service", list.get(position));
            startActivity(i);
        });

        // Bottom navigation
        BottomNavigationView nav = findViewById(R.id.bottomNav);
        nav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_logout) {
                startActivity(new Intent(ServicesActivity.this, LoginActivity.class));
                finish();
            }
            return true;
        });
    }

    void loadServices() {
        SQLiteDatabase database = db.getReadableDatabase();
        Cursor c = database.rawQuery("SELECT * FROM services", null);

        list = new ArrayList<>();

        while (c.moveToNext()) {
            list.add(c.getString(1));
        }

        c.close();

        // ✅ SAFE ADAPTER (NO CRASH)
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.service_item,   // uses your fixed XML
                list
        );

        listView.setAdapter(adapter);
    }
}