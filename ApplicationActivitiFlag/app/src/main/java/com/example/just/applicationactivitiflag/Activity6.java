package com.example.just.applicationactivitiflag;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Activity6 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_6);
    }

    public void onClickReturnZeroActivity(View view) {
        startActivity(new Intent(this, Activity0.class));
    }
}
