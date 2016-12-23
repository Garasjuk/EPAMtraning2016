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

import com.example.just.businesinfo.R;
import com.example.just.businesinfo.adapter.BoxAdapter;
import com.example.just.businesinfo.connect.DatabaseHandler;
import com.example.just.businesinfo.Entity.ParsedDataSet;

import java.util.ArrayList;
import java.util.List;

public class SettingCurrencyFragment extends Fragment {


    DatabaseHandler db;
    List<ParsedDataSet> parsedDataSet;
    private ListView lv;
    SettingCurrencyFragment.GetDBSettingCurrency getDBSettingCurrency;

    public SettingCurrencyFragment() {
        parsedDataSet = new ArrayList<>();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting_currency, container, false);
        lv = (ListView) view.findViewById(R.id.list_currency_setting);
        db = new DatabaseHandler(getActivity());

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
            }
        });

        try {
            getDBSettingCurrency = new SettingCurrencyFragment.GetDBSettingCurrency();
            getDBSettingCurrency.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    public class GetDBSettingCurrency extends AsyncTask<Void, Void, ArrayList<ParsedDataSet>> {

        @Override
        protected ArrayList<ParsedDataSet> doInBackground(Void... argo0) {
            ArrayList<ParsedDataSet> parsedDataSets = new ArrayList<>();
            try {
                parsedDataSets = db.getAllCurrencySetting();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return parsedDataSets;
        }

        @Override
        protected void onPostExecute(ArrayList<ParsedDataSet> result) {
            super.onPostExecute(result);
            ListAdapter adapter = new BoxAdapter(
                    getActivity(), result);
            lv.setAdapter(adapter);
        }
    }
}
