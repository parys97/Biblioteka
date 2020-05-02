package com.example.biblioteka;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;

import in.mayanknagwanshi.imagepicker.ImageSelectActivity;

import static com.example.biblioteka.BooksDatabase.KEY_BOOK_AUTHOR;
import static com.example.biblioteka.BooksDatabase.KEY_BOOK_DESCRIPTION;
import static com.example.biblioteka.BooksDatabase.KEY_BOOK_IMAGE;
import static com.example.biblioteka.BooksDatabase.KEY_BOOK_ISRENT;
import static com.example.biblioteka.BooksDatabase.KEY_BOOK_TITLE;

public class EditBooksActivity extends AppCompatActivity {
    private EditText authorEt, titleEt, descriptionEt;
    private ImageView coverImageView;
    private Button chaneCoverButton, saveButton;
    private Bitmap selectedImage;
    private RadioButton rb1, rb2;

    private static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_books);
        rb1 = findViewById(R.id.rb1);
        rb2 = findViewById(R.id.rb2);
        authorEt = findViewById(R.id.authorTv);
        titleEt = findViewById(R.id.titleTv);
        descriptionEt = findViewById(R.id.descriptionTv);
        coverImageView = findViewById(R.id.coverImageView);
        chaneCoverButton = findViewById(R.id.changeCover);
        saveButton = findViewById(R.id.saveButton);
        String author = getIntent().getStringExtra("author");
        String description = getIntent().getStringExtra("description");
        String title = getIntent().getStringExtra("title");
        String isRent = getIntent().getStringExtra("isRent");
        if (Integer.valueOf(isRent) == 0) {
            rb2.setChecked(true);
        } else {
            rb1.setChecked(true);
        }

        byte[] imageByte = getIntent().getByteArrayExtra("blobImage");
        selectedImage = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
        final String book_id = getIntent().getStringExtra("book_id");
        if (Integer.valueOf(book_id) != -1 && Integer.valueOf(isRent) != -1) {
            authorEt.setText(author);
            titleEt.setText(title);
            descriptionEt.setText(description);
            coverImageView.setImageBitmap(selectedImage);
        }
        chaneCoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditBooksActivity.this, ImageSelectActivity.class);
                intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true);//default is true
                intent.putExtra(ImageSelectActivity.FLAG_GALLERY, true);//default is true
                startActivityForResult(intent, 1213);
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(KEY_BOOK_AUTHOR, authorEt.getText().toString());
                contentValues.put(KEY_BOOK_DESCRIPTION, descriptionEt.getText().toString());
                contentValues.put(KEY_BOOK_TITLE, titleEt.getText().toString());
                contentValues.put(KEY_BOOK_IMAGE, getBitmapAsByteArray(Bitmap.createScaledBitmap(selectedImage, 250, 250, false)));
                int isRent = 0;
                if (rb1.isChecked()) {
                    isRent = 1;
                }
                if (rb2.isChecked()) {
                    isRent = 0;
                }
                contentValues.put(KEY_BOOK_ISRENT, isRent);
                Uri uri = Uri.parse(BooksContentProvider.BOOKS_CONTENT_URI + "/" + book_id);
                getContentResolver().update(uri, contentValues, null, null);
                Toast.makeText(getApplicationContext(), "uaktualniono", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1213 && resultCode == Activity.RESULT_OK) {
            String filePath = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
            selectedImage=BitmapFactory.decodeFile(filePath);
            coverImageView.setImageBitmap(selectedImage);
        }
    }
}

