package com.example.just.businesinfo.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.just.businesinfo.Entity.MetalDataSet;
import com.example.just.businesinfo.R;
import com.example.just.businesinfo.adapter.MetalAdapterSetting;
import com.example.just.businesinfo.connect.DatabaseHandler;

import java.util.ArrayList;

public class SettingMetalFragment extends Fragment {

    DatabaseHandler db;
    private ListView lv;
    SettingMetalFragment.GetDBSettingMetal getDBSettingMetal;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting_metal, container, false);

        lv = (ListView) view.findViewById(R.id.list_metal_setting);
        db = new DatabaseHandler(getActivity());

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getActivity(), "Clicked on Row: ", Toast.LENGTH_LONG).show();
            }
        });

        try {
            getDBSettingMetal = new SettingMetalFragment.GetDBSettingMetal();
            getDBSettingMetal.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    public class GetDBSettingMetal extends AsyncTask<Void, Void, ArrayList<MetalDataSet>> {

        @Override
        protected ArrayList<MetalDataSet> doInBackground(Void... argo0) {
            ArrayList<MetalDataSet> metalDataSet = new ArrayList<>();
            try {
                metalDataSet = db.getAllMetalSetting();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return metalDataSet;
        }

        @Override
        protected void onPostExecute(ArrayList<MetalDataSet> result) {
            super.onPostExecute(result);
            ListAdapter adapter = new MetalAdapterSetting(
                    getActivity(), result);
            lv.setAdapter(adapter);
        }
    }
}
