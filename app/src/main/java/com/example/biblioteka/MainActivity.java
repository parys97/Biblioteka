package com.example.biblioteka;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText loginEditText, password;
    private ReadFile readFile;
    private Admin admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        readFile = new ReadFile(getApplicationContext());
        admin = readFile.getAdmin();
        loginEditText = findViewById(R.id.emailEditText);
        password = findViewById(R.id.password);
        Button signUpButton = findViewById(R.id.signupButton);
        final Button loginButton = findViewById(R.id.loginButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loginEditText.getText().toString().equals(admin.getLogin()) &&
                        password.getText().toString().equals(admin.getPassword())) {
                    //admin
                    startActivity(new Intent(MainActivity.this, AdminBooksActivity.class));
                } else {
                    if (!loginEditText.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
                        Cursor cursor = getContentResolver().query(UsersContentProvider.USERS_CONTENT_URI, null,
                                UsersDatabase.KEY_LOGIN + "=?" + " AND " +
                                        UsersDatabase.KEY_PASSWORD + "=?", new String[]{loginEditText.getText().toString(), password.getText().toString()}, null, null);
                        if (cursor == null || cursor.getCount() <= 0) {
                            Toast.makeText(getApplicationContext(), "Niepoprawny login lub haslo", Toast.LENGTH_LONG).show();
                        } else {
                            while (cursor.moveToNext()) {
                                String login =
                                        cursor.getString(cursor.getColumnIndexOrThrow(UsersDatabase.KEY_LOGIN));
                                String passwordFromDb =
                                        cursor.getString(cursor.getColumnIndexOrThrow(UsersDatabase.KEY_PASSWORD));
                                if (login.equals(loginEditText.getText().toString()) && passwordFromDb.equals(
                                        password.getText().toString()
                                )) {
                                    //succes
                                    Toast.makeText(getApplicationContext(), "Zalogowano", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(MainActivity.this, BooksActivity.class));

                                } else {
                                    Toast.makeText(getApplicationContext(), "Niepoprawny login lub haslo", Toast.LENGTH_LONG).show();

                                }
                            }
                            cursor.close();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Uzupełnij login i hasło", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }
}
