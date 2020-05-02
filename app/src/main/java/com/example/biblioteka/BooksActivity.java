package com.example.biblioteka;


import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

import static android.content.Intent.FLAG_ACTIVITY_NO_HISTORY;
import static com.example.biblioteka.BooksDatabase.KEY_BOOK_AUTHOR;
import static com.example.biblioteka.BooksDatabase.KEY_BOOK_DESCRIPTION;
import static com.example.biblioteka.BooksDatabase.KEY_BOOK_ID;
import static com.example.biblioteka.BooksDatabase.KEY_BOOK_IMAGE;
import static com.example.biblioteka.BooksDatabase.KEY_BOOK_ISRENT;
import static com.example.biblioteka.BooksDatabase.KEY_BOOK_TITLE;

public class BooksActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private SimpleCursorAdapter dataAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);
        displayListView();
    }

    private void displayListView() {
        String[] columns = new String[]{
                KEY_BOOK_AUTHOR, KEY_BOOK_TITLE
        };
        int[] to = new int[]{
                R.id.authorTextView,
                R.id.titleTextView,
        };
        dataAdapter = new SimpleCursorAdapter(this, R.layout.books_listview_item, null, columns, to, 0);
        listView = findViewById(R.id.listView);
        listView.setAdapter(dataAdapter);
        getLoaderManager().initLoader(0, null, this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);
                String book_id =
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_BOOK_ID));
                String author =
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_BOOK_AUTHOR));
                String title =
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_BOOK_TITLE));
                String description =
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_BOOK_DESCRIPTION));
                String isRent =
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_BOOK_ISRENT));
                byte[] blobImage =
                        cursor.getBlob(cursor.getColumnIndexOrThrow(KEY_BOOK_IMAGE));


                Intent intentEdit = new Intent(getBaseContext(), BookDetailsActivity.class);
                intentEdit.putExtra("book_id", book_id);
                intentEdit.putExtra("author", author);
                intentEdit.putExtra("title", title);
                intentEdit.putExtra("description", description);
                intentEdit.putExtra("isRent", isRent);
                intentEdit.putExtra("blobImage", blobImage);
                startActivity(intentEdit);
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projections = new String[]{
                KEY_BOOK_ID, KEY_BOOK_AUTHOR, KEY_BOOK_TITLE, KEY_BOOK_DESCRIPTION, KEY_BOOK_ISRENT,KEY_BOOK_IMAGE
        };
        CursorLoader cursorLoader = new CursorLoader(this, BooksContentProvider.BOOKS_CONTENT_URI, projections, null, null, null);

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        dataAdapter.swapCursor(data);

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        dataAdapter.swapCursor(null);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.books_user_menu, menu);
        MenuItem logOutItem = menu.findItem(R.id.log_uot);
        logOutItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                return true;
            }
        });
        return true;
    }

}
