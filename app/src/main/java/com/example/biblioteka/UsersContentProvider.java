package com.example.biblioteka;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;


public class UsersContentProvider extends ContentProvider {
    private static final int ALL_USERS = 1;
    private static final int SINGLE_USERS = 2;
    private static final String USERS_AUTHORITY = "com.example.biblioteka.users";
    public static final Uri USERS_CONTENT_URI =
            Uri.parse("content://" + USERS_AUTHORITY + "/users");
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(USERS_AUTHORITY, "users", ALL_USERS);
        uriMatcher.addURI(USERS_AUTHORITY, "users/#", SINGLE_USERS);
    }

    private UsersDatabaseHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new UsersDatabaseHelper(getContext());
        return false;
    }

    @Override
    public String getType(Uri uri) {

        switch (uriMatcher.match(uri)) {
            case ALL_USERS:
                return "vnd.android.cursor.dir/vnd.com.example.biblioteka.users.users";
            case SINGLE_USERS:
                return "vnd.android.cursor.item/vnd.com.example.biblioteka.users.users";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id;
        switch (uriMatcher.match(uri)) {
            case ALL_USERS:
                id = db.insert(UsersDatabase.USER_TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(USERS_CONTENT_URI + "/" + id);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(UsersDatabase.USER_TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case ALL_USERS:
                break;
            case SINGLE_USERS:
                String id = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(UsersDatabase.KEY_USER_ID + "=" + id);
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
            case ALL_USERS:
                break;
            case SINGLE_USERS:
                String id = uri.getPathSegments().get(1);
                selection = UsersDatabase.KEY_USER_ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ?
                        " AND (" + selection + ')' : "");
                deleteCount = db.delete(UsersDatabase.USER_TABLE_NAME, selection, selectionArgs);
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
            case ALL_USERS:
                break;
            case SINGLE_USERS:
                String id = uri.getPathSegments().get(1);
                selection = UsersDatabase.KEY_USER_ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ?
                        " AND (" + selection + ')' : "");
                updateCount = db.update(UsersDatabase.USER_TABLE_NAME, values, selection, selectionArgs);

                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return updateCount;
    }
}

