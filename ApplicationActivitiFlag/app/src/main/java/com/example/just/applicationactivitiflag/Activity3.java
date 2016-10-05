package com.example.just.applicationactivitiflag;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by just on 05.10.2016.
 */

public class Activity3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);
    }

    public void startFourthActivity(View view) {
        startActivity(new Intent(this, Activity4.class));
    }
}
