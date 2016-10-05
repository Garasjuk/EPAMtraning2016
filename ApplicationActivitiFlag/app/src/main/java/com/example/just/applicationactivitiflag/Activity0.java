package com.example.just.applicationactivitiflag;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by just on 05.10.2016.
 */

public class Activity0 extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defineActivityDirection();
    }


    private void defineActivityDirection() {
        // Retrieve a boolean value from SharedPreferences


        if(!getApplicationContext().getSharedPreferences("Prefix", MODE_PRIVATE)
                .getBoolean("isStatus", false)) {
            startActivity(new Intent(this, Activity1.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        }

        else {
            startActivity(new Intent(this, Activity6.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        }

    }
}
