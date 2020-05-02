package com.example.biblioteka;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BooksDatabaseHelper extends SQLiteOpenHelper {
    public static final String BOOKS_DATABASE_NAME = "books";
    public static final int BOOKS_DATABASE_VERSION = 1;

    public BooksDatabaseHelper(Context context) {
        super(context, BOOKS_DATABASE_NAME, null, BOOKS_DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        BooksDatabase.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        BooksDatabase.onUpgrade(db, oldVersion, newVersion);
    }
}


