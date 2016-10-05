package com.example.just.applicationactivitiflag;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by just on 05.10.2016.
 */

public class Activity1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1);
    }



    public void startSecondActivity(View view) {
        startActivity(new Intent(this, Activity2.class));
    }
}
