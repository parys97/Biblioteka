package com.example.biblioteka;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReadFile {
    private Context context;
    private String login;
    private String password;

    public ReadFile(Context context) {
        this.context = context;
    }

    public Admin getAdmin() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(context.getAssets().open("admins.txt"), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String dane = null;
        try {
            dane = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (dane != null) {
            String[] row;
            row = dane.split(" - ");
            login = row[0];
            password = row[1];
            try {
                dane = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new Admin(login,password);
    }
}


