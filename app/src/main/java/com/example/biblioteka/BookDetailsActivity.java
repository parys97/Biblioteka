package com.example.biblioteka;


import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;

import static com.example.biblioteka.BooksDatabase.KEY_BOOK_AUTHOR;
import static com.example.biblioteka.BooksDatabase.KEY_BOOK_DESCRIPTION;
import static com.example.biblioteka.BooksDatabase.KEY_BOOK_IMAGE;
import static com.example.biblioteka.BooksDatabase.KEY_BOOK_ISRENT;
import static com.example.biblioteka.BooksDatabase.KEY_BOOK_TITLE;

public class BookDetailsActivity extends AppCompatActivity {
    private TextView authorDetails, titledDetails, descriptionDetails;
    private ImageView coverDetails;
    private Button rentButton;
    private Bitmap selectedImage;

    private static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        coverDetails = findViewById(R.id.coverDetails);
        rentButton = findViewById(R.id.rentButton);
        authorDetails = findViewById(R.id.authorDetails);
        titledDetails = findViewById(R.id.titleDetails);
        descriptionDetails = findViewById(R.id.descriptionDetails);
        final String author = getIntent().getStringExtra("author");
        final String description = getIntent().getStringExtra("description");
        final String title = getIntent().getStringExtra("title");
        String isRent = getIntent().getStringExtra("isRent");
        byte[] imageByte = getIntent().getByteArrayExtra("blobImage");
        selectedImage = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
        coverDetails.setImageBitmap(selectedImage);
        final String book_id = getIntent().getStringExtra("book_id");
        if (Integer.valueOf(book_id) != -1 && Integer.valueOf(isRent) != -1) {
            authorDetails.setText(author);
            descriptionDetails.setText(description);
            titledDetails.setText(title);
            if (Integer.valueOf(isRent) == 0) {
                rentButton.setVisibility(View.VISIBLE);
                rentButton.setEnabled(true);
            } else {
                rentButton.setVisibility(View.INVISIBLE);
                rentButton.setEnabled(false);
            }
        }
        rentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(KEY_BOOK_AUTHOR, author);
                contentValues.put(KEY_BOOK_DESCRIPTION, description);
                contentValues.put(KEY_BOOK_TITLE, title);
                contentValues.put(KEY_BOOK_IMAGE, getBitmapAsByteArray(Bitmap.createScaledBitmap(selectedImage, 250, 250, false)));
                contentValues.put(KEY_BOOK_ISRENT, 1);
                Uri uri = Uri.parse(BooksContentProvider.BOOKS_CONTENT_URI + "/" + book_id);
                getContentResolver().update(uri, contentValues, null, null);
                Toast.makeText(getApplicationContext(), "Wypozyczono", Toast.LENGTH_SHORT).show();
                rentButton.setVisibility(View.INVISIBLE);
                rentButton.setEnabled(false);
            }
        });
    }
}
