package com.example.just.businesinfo.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.just.businesinfo.Entity.MetalDataSet;
import com.example.just.businesinfo.R;
import com.example.just.businesinfo.adapter.MetalAdapterSetting;
import com.example.just.businesinfo.connect.DatabaseHandler;

import java.util.ArrayList;

public class SettingMetalFragment extends Fragment {

    public static final String LOG_TAG = "SettingActivity.java";

    DatabaseHandler db;
//    List<MetalDataSet> metalDataSet = new ArrayList<MetalDataSet>();
    private ListView lv;
    //    private CheckBox cb;
    SettingMetalFragment.GetDBSettingMetal getDBSettingMetal;
//    BoxAdapter boxAdapter;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting_metal, container, false);

        lv = (ListView) view.findViewById(R.id.list_metal_setting);
        db = new DatabaseHandler(getActivity());
//        lv.setOnItemClickListener(itemClickListener);

        CheckBox cb = (CheckBox) view.findViewById(R.id.checkBox);


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
                Toast.makeText(getActivity(), "Clicked on Row: ", Toast.LENGTH_LONG).show();
            }
        });

        try {
            getDBSettingMetal = new SettingMetalFragment.GetDBSettingMetal();
            getDBSettingMetal.execute();


        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }


    public class GetDBSettingMetal extends AsyncTask<Void, Void, ArrayList<MetalDataSet>> {


        public static final String LOG_TAG = "SettingMetalFragment.java";

        @Override
        protected ArrayList<MetalDataSet> doInBackground(Void... argo0) {
            ArrayList<MetalDataSet> metalDataSet = new ArrayList<MetalDataSet>();

            try {

                Log.v(LOG_TAG, "Load AsyncTask ");

                metalDataSet = db.getAllMetalSetting();


            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return metalDataSet;
//            return contactList;
        }

        @Override
        protected void onPostExecute(ArrayList<MetalDataSet> result) {
            super.onPostExecute(result);

            Log.v(LOG_TAG, "get DB ");

            ListAdapter adapter = new MetalAdapterSetting(
                    getActivity(), result);
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
