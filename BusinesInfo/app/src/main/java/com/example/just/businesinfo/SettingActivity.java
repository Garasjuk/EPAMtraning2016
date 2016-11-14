package com.example.just.businesinfo;

import android.content.SyncStatusObserver;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.example.just.businesinfo.adapter.BoxAdapter;
import com.example.just.businesinfo.connect.DatabaseHandler;
import com.example.just.businesinfo.connect.ParsedDataSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class SettingActivity extends AppCompatActivity {


    public static final String LOG_TAG = "SettingActivity.java";

    DatabaseHandler db;
    List<ParsedDataSet> parsedDataSet = new ArrayList<ParsedDataSet>();
    private ListView lv;
    //    private CheckBox cb;
    GetDBSetting getDBSetting;
    BoxAdapter boxAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        lv = (ListView) findViewById(R.id.listSetting);
        db = new DatabaseHandler(this);
//        lv.setOnItemClickListener(itemClickListener);

        CheckBox cb = (CheckBox) findViewById(R.id.checkBox);


//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(SettingActivity.this, position, Toast.LENGTH_SHORT).show();
//            }
//        });        // присвоим обработчик кнопке OK (btnOk)


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                Toast.makeText(getApplicationContext(), "Clicked on Row: ", Toast.LENGTH_LONG).show();
            }
        });

        try {
            getDBSetting = new GetDBSetting();
            getDBSetting.execute();


        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public class GetDBSetting extends AsyncTask<Void, Void, ArrayList<ParsedDataSet>> {


        public static final String LOG_TAG = "SettingActivity.java";

        @Override
        protected ArrayList<ParsedDataSet> doInBackground(Void... argo0) {
            ArrayList<ParsedDataSet> parsedDataSets = new ArrayList<ParsedDataSet>();

            try {

                Log.v(LOG_TAG, "Load AsyncTask ");

                parsedDataSets = db.getAllCurrencySetting();


            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return parsedDataSets;
//            return contactList;
        }

        @Override
        protected void onPostExecute(ArrayList<ParsedDataSet> result) {
            super.onPostExecute(result);

            Log.v(LOG_TAG, "get DB ");

            ListAdapter adapter = new BoxAdapter(
                    SettingActivity.this, result);
            lv.setAdapter(adapter);


//
//            ListAdapter adapter = new SimpleAdapter(
//                    SettingActivity.this, result,
//                    R.layout.list_setting, new String[]{ "Status","CharCode"},
//                    new int[]{R.id.checkBox, R.id.CharCode});
//            lv.setAdapter(adapter);

//            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
