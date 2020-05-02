package com.example.biblioteka;

import android.database.sqlite.SQLiteDatabase;

public class BooksDatabase {
    public static final String KEY_BOOK_ID = "_id";
    public static final String KEY_BOOK_AUTHOR = "author";
    public static final String KEY_BOOK_TITLE = "title";
    public static final String KEY_BOOK_DESCRIPTION = "description";
    public static final String KEY_BOOK_IMAGE = "image";
    public static final String KEY_BOOK_ISRENT = "isrent";

    public static final String BOOK_TABLE_NAME = "books";

    public static final String BOOK_TABLE_CREATE = "CREATE TABLE if not exists " + BOOK_TABLE_NAME + " (" +
            KEY_BOOK_ID + " integer PRIMARY KEY autoincrement, " +
            KEY_BOOK_AUTHOR + ", " +
            KEY_BOOK_TITLE + ", " +
            KEY_BOOK_DESCRIPTION + ", " +
            KEY_BOOK_ISRENT + " integer , " +
            KEY_BOOK_IMAGE + " BLOB )";

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(BOOK_TABLE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + BOOK_TABLE_NAME);
        onCreate(db);
    }
}

