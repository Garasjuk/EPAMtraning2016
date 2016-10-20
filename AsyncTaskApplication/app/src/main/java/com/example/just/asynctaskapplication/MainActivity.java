package com.example.just.asynctaskapplication;

import android.content.Intent;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<String> {

    static final int LOADER_TIME_ID = 1;
    TextView tvTime;
    Button btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvTime = (TextView) findViewById(R.id.tvTime);
        btn2 = (Button) findViewById(R.id.btn2);
        Bundle bndl = new Bundle();
        bndl.putString(TimeLoader.ARGS_TIME_FORMAT, getTimeFormat());
        getLoaderManager().initLoader(LOADER_TIME_ID, bndl, this);
    }

    public void onClickSecondActivity(View v){
        Intent intent = new Intent("com.example.just.asynctaskapplication.secondActivity");
        startActivity(intent);
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        Loader<String> loader = null;
        if (id == LOADER_TIME_ID) {
            loader = new TimeLoader(this, args);
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String result) {
        tvTime.setText(result);
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
    }

    public void getTimeClick(View v) {
        Loader<String> loader;
        loader = getLoaderManager().getLoader(LOADER_TIME_ID);
        loader.forceLoad();
    }

    String getTimeFormat() {
        String result = TimeLoader.TIME_FORMAT;
        return result;
    }


}
