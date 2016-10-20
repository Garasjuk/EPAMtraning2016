package com.example.just.asynctaskapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.just.asynctaskapplication.ui.IProgressCallback;
import com.example.just.asynctaskapplication.ui.IResultCallback;
import com.example.just.asynctaskapplication.ui.ITask;
import com.example.just.asynctaskapplication.ui.MyAsyncTask;

public class SecondActivity extends AppCompatActivity {

    private static final String TAG = "MY_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        final TextView view = (TextView) findViewById(R.id.textView2);
        MyAsyncTask myTask = new MyAsyncTask();
        myTask.execute(new ITask<String, Integer, String>() {

            @Override
            public String doInBackground(String s, IProgressCallback<Integer> progressCallback) {
                int count = 0;
                for (char c : s.toCharArray()) {
                    Log.d(TAG, "doInBackground: " + c);
                    progressCallback.onProgressUpdate(count);
                    count++;
                    try {
                        Thread.currentThread().sleep(3000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return "Work is done!";
            }
        }, "World", new IProgressCallback<Integer>() {
            @Override
            public void onProgressUpdate(Integer integer) {
                Toast.makeText(SecondActivity.this, "Downloading " + integer + " file", Toast.LENGTH_SHORT).show();
            }
        }, new IResultCallback<String>() {

            @Override
            public void onSuccess(String s) {
                view.setText(s);
            }

            @Override
            public void onError(Exception exception) {
                view.setText("Error");
            }
        });
    }
}