package com.example.just.applicationactivitiflag;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by just on 05.10.2016.
 */

public class Activity5 extends AppCompatActivity {

    private String savedPrefix = "savedPrefix";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_5);
    }

    public void startMainActivity(View view) {
        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences(savedPrefix, MODE_PRIVATE).edit();
        editor.putBoolean("isRoundFinished", true).apply();

        Intent intent = new Intent(this, Activity6.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
