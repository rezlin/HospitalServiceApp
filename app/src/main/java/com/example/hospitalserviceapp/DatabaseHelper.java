package com.example.hospitalserviceapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, "hospital.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS users(id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, email TEXT, password TEXT, role TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS services(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS requests(id INTEGER PRIMARY KEY AUTOINCREMENT, service TEXT, ward TEXT, bed TEXT, notes TEXT)");

        // ADMIN USER (only insert if not exists)
        db.execSQL("INSERT OR IGNORE INTO users(id, username, email, password, role) VALUES(1,'admin','admin@test.com','1234','admin')");

        // DEFAULT SERVICES
        db.execSQL("INSERT INTO services(name) VALUES('Cleaning')");
        db.execSQL("INSERT INTO services(name) VALUES('Equipment Assistance')");
        db.execSQL("INSERT INTO services(name) VALUES('Linen Change')");
        db.execSQL("INSERT INTO services(name) VALUES('Porter Service')");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}