package com.example.hospitalserviceapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {

    EditText serviceInput;
    Button addBtn, removeServiceBtn, viewRequestsBtn, viewUsersBtn;
    ListView listView;

    DatabaseHelper db;

    ArrayList<String> servicesList;
    ArrayList<String> usersList;
    ArrayList<String> requestsList;

    String mode = "services";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // LINK XML IDS
        serviceInput = findViewById(R.id.serviceInput);
        addBtn = findViewById(R.id.addBtn);
        removeServiceBtn = findViewById(R.id.removeServiceBtn);
        viewRequestsBtn = findViewById(R.id.viewRequestsBtn);
        viewUsersBtn = findViewById(R.id.viewUsersBtn);
        listView = findViewById(R.id.listView);

        db = new DatabaseHelper(this);

        // DEFAULT LOAD
        loadServices();

        // ================= ADD SERVICE =================
        addBtn.setOnClickListener(v -> {
            String service = serviceInput.getText().toString().trim();

            if(service.isEmpty()){
                Toast.makeText(this,"Enter service",Toast.LENGTH_SHORT).show();
                return;
            }

            db.getWritableDatabase().execSQL(
                    "INSERT INTO services(name) VALUES(?)",
                    new Object[]{service}
            );

            serviceInput.setText("");
            loadServices();
        });

        // ================= REMOVE SERVICE =================
        removeServiceBtn.setOnClickListener(v -> {
            mode = "services";
            loadServices();
            Toast.makeText(this,"Long press service to delete",Toast.LENGTH_SHORT).show();
        });

        // ================= VIEW REQUESTS =================
        viewRequestsBtn.setOnClickListener(v -> {
            mode = "requests";
            loadRequests();
        });

        // ================= VIEW USERS =================
        viewUsersBtn.setOnClickListener(v -> {
            mode = "users";
            loadUsers();
            Toast.makeText(this,"Long press user to delete",Toast.LENGTH_SHORT).show();
        });

        // ================= LONG CLICK ACTION =================
        listView.setOnItemLongClickListener((parent, view, position, id) -> {

            if(mode.equals("services")){
                // DELETE SERVICE
                db.getWritableDatabase().execSQL(
                        "DELETE FROM services WHERE name=?",
                        new Object[]{servicesList.get(position)}
                );
                loadServices();
            }

            else if(mode.equals("users")){
                // DELETE USER
                db.getWritableDatabase().execSQL(
                        "DELETE FROM users WHERE username=?",
                        new Object[]{usersList.get(position)}
                );
                loadUsers();
            }

            else if(mode.equals("requests")){
                // DELETE REQUEST
                deleteRequest(position);
            }

            return true;
        });
    }

    // ================= LOAD SERVICES =================
    void loadServices(){
        servicesList = new ArrayList<>();
        mode = "services";

        Cursor c = db.getReadableDatabase().rawQuery("SELECT * FROM services", null);

        while(c.moveToNext()){
            servicesList.add(c.getString(1));
        }
        c.close();

        listView.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                servicesList
        ));
    }

    // ================= LOAD USERS =================
    void loadUsers(){
        usersList = new ArrayList<>();

        Cursor c = db.getReadableDatabase().rawQuery("SELECT * FROM users", null);

        while(c.moveToNext()){
            usersList.add(c.getString(1)); // username
        }
        c.close();

        listView.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                usersList
        ));
    }

    // ================= LOAD REQUESTS =================
    void loadRequests(){
        requestsList = new ArrayList<>();

        Cursor c = db.getReadableDatabase().rawQuery("SELECT * FROM requests", null);

        while(c.moveToNext()){
            String r = "Service: " + c.getString(1) +
                    "\nWard: " + c.getString(2) +
                    "\nBed: " + c.getString(3);

            requestsList.add(r);
        }
        c.close();

        listView.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                requestsList
        ));
    }

    // ================= DELETE REQUEST =================
    void deleteRequest(int position){
        Cursor c = db.getReadableDatabase().rawQuery("SELECT id FROM requests", null);

        if(c.moveToPosition(position)){
            int id = c.getInt(0);

            db.getWritableDatabase().execSQL(
                    "DELETE FROM requests WHERE id=?",
                    new Object[]{id}
            );
        }
        c.close();

        loadRequests();
    }
}