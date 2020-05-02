package com.example.biblioteka;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import static android.content.Intent.FLAG_ACTIVITY_NO_HISTORY;
import static com.example.biblioteka.UsersDatabase.KEY_LOGIN;
import static com.example.biblioteka.UsersDatabase.KEY_PASSWORD;

public class SignUpActivity extends AppCompatActivity {
    EditText email, password, passwordRepeat;
    Button createButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        email = findViewById(R.id.emailEditText);
        password = findViewById(R.id.password);
        passwordRepeat = findViewById(R.id.passwordRepeat);

        createButton = findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!email.getText().toString().isEmpty() && !password.getText().toString().isEmpty()
                        && !passwordRepeat.getText().toString().isEmpty() && password.getText().toString().equals(passwordRepeat.getText().toString())) {
                    //success
                    saveUserInDatabase();

                } else {
                    Toast.makeText(SignUpActivity.this, "Dane niepoprawne", Toast.LENGTH_LONG).show();
                }
            }
        });


    }
    private void saveUserInDatabase() {
        Cursor cursor = getContentResolver().query(UsersContentProvider.USERS_CONTENT_URI, null,
                UsersDatabase.KEY_LOGIN + "=?" + " AND " +
                        UsersDatabase.KEY_PASSWORD + "=?", new String[]{email.getText().toString(), password.getText().toString()}, null, null);
        if (cursor == null || cursor.getCount() <= 0) {
            //ok sign up
            save();
        } else {
            while (cursor.moveToNext()) {
                String login =
                        cursor.getString(cursor.getColumnIndexOrThrow(UsersDatabase.KEY_LOGIN));
                String passwordFromDb =
                        cursor.getString(cursor.getColumnIndexOrThrow(UsersDatabase.KEY_PASSWORD));
                if (login.equals(email.getText().toString()) && passwordFromDb.equals(
                        password.getText().toString()
                )) {
                    // already in database
                    Toast.makeText(getApplicationContext(), "Konto już utworzone", Toast.LENGTH_LONG).show();

                } else {
                    save();
                }
            }
            cursor.close();
        }
    }

    private void save() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_LOGIN, email.getText().toString());
        contentValues.put(KEY_PASSWORD, password.getText().toString());
        getContentResolver().insert(UsersContentProvider.USERS_CONTENT_URI, contentValues);
        Toast.makeText(getApplicationContext(), "Konto utworzone", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }
}
