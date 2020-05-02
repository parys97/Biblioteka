package com.example.biblioteka;

import android.database.sqlite.SQLiteDatabase;

public class UsersDatabase {
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_LOGIN = "login";
    public static final String KEY_PASSWORD = "password";

    public static final String USER_TABLE_NAME = "users";

    public static final String USER_TABLE_CREATE = "CREATE TABLE if not exists " + USER_TABLE_NAME + " (" +
            KEY_USER_ID + " integer PRIMARY KEY autoincrement, " +
            KEY_LOGIN + ", " +
            KEY_PASSWORD + " )";

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(USER_TABLE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        onCreate(db);
    }
}

