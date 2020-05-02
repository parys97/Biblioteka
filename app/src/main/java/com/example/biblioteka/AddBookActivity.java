package com.example.biblioteka;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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


public class AddBookActivity extends AppCompatActivity {
    private EditText authorEt, titleEt, descitpionEt;
    private Button addImageButton, saveButton;
    private ImageView imageView;
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
        setContentView(R.layout.activity_add_book);
        rb1 = findViewById(R.id.rb1);
        rb2 = findViewById(R.id.rb2);
        authorEt = findViewById(R.id.author);
        titleEt = findViewById(R.id.title);
        descitpionEt = findViewById(R.id.description);
        imageView = findViewById(R.id.imaageView);
        addImageButton = findViewById(R.id.addImage);
        saveButton = findViewById(R.id.saveButton);
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddBookActivity.this, ImageSelectActivity.class);
                intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true);//default is true
                intent.putExtra(ImageSelectActivity.FLAG_GALLERY, true);//default is true
                startActivityForResult(intent, 1213);
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!authorEt.getText().toString().isEmpty() && !titleEt.getText().toString().isEmpty()
                        && !descitpionEt.getText().toString().isEmpty() && selectedImage != null) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(KEY_BOOK_AUTHOR, authorEt.getText().toString());
                    contentValues.put(KEY_BOOK_DESCRIPTION, descitpionEt.getText().toString());
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

                    getContentResolver().insert(BooksContentProvider.BOOKS_CONTENT_URI, contentValues);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Uzupełnij wszystkie pola", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1213 && resultCode == Activity.RESULT_OK) {
            String filePath = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
            selectedImage = BitmapFactory.decodeFile(filePath);
            imageView.setImageBitmap(selectedImage);
        }
    }
}

