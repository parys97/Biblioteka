package com.example.biblioteka;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UsersDatabaseHelper extends SQLiteOpenHelper {
    public static final String USERS_DATABASE_NAME = "users";
    public static final int USERS_DATABASE_VERSION = 3;

    public UsersDatabaseHelper(Context context) {
        super(context, USERS_DATABASE_NAME, null, USERS_DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        UsersDatabase.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        UsersDatabase.onUpgrade(db, oldVersion, newVersion);
    }
}

