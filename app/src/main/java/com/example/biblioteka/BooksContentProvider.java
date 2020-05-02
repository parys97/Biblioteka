package com.example.biblioteka;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;


public class BooksContentProvider extends ContentProvider {
    private static final int ALL_BOOKS = 1;
    private static final int SINGLE_BOOK = 2;
    private static final String BOOKS_AUTHORITY = "com.example.biblioteka";
    public static final Uri BOOKS_CONTENT_URI =
            Uri.parse("content://" + BOOKS_AUTHORITY + "/books");
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(BOOKS_AUTHORITY, "books", ALL_BOOKS);
        uriMatcher.addURI(BOOKS_AUTHORITY, "books/#", SINGLE_BOOK);
    }

    private BooksDatabaseHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new BooksDatabaseHelper(getContext());
        return false;
    }

    @Override
    public String getType(Uri uri) {

        switch (uriMatcher.match(uri)) {
            case ALL_BOOKS:
                return "vnd.android.cursor.dir/vnd.com.example.biblioteka.books";
            case SINGLE_BOOK:
                return "vnd.android.cursor.item/vnd.com.example.biblioteka.books";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id;
        switch (uriMatcher.match(uri)) {
            case ALL_BOOKS:
                id = db.insert(BooksDatabase.BOOK_TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BOOKS_CONTENT_URI + "/" + id);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(BooksDatabase.BOOK_TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case ALL_BOOKS:
                break;
            case SINGLE_BOOK:
                String id = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(BooksDatabase.KEY_BOOK_ID + "=" + id);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        return cursor;

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deleteCount = 0;
        switch (uriMatcher.match(uri)) {
            case ALL_BOOKS:
                break;
            case SINGLE_BOOK:
                String id = uri.getPathSegments().get(1);
                selection = BooksDatabase.KEY_BOOK_ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ?
                        " AND (" + selection + ')' : "");
                deleteCount = db.delete(BooksDatabase.BOOK_TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return deleteCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int updateCount = 0;
        switch (uriMatcher.match(uri)) {
            case ALL_BOOKS:
                break;
            case SINGLE_BOOK:
                String id = uri.getPathSegments().get(1);
                selection = BooksDatabase.KEY_BOOK_ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ?
                        " AND (" + selection + ')' : "");
                updateCount = db.update(BooksDatabase.BOOK_TABLE_NAME, values, selection, selectionArgs);

                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return updateCount;
    }
}
