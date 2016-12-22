package com.example.just.businesinfo.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.just.businesinfo.Entity.IngotDataSet;
import com.example.just.businesinfo.Entity.MetalDataSet;
import com.example.just.businesinfo.ISuccess;
import com.example.just.businesinfo.R;
import com.example.just.businesinfo.SettingActivity;
import com.example.just.businesinfo.adapter.MetalAdapter;
import com.example.just.businesinfo.connect.DatabaseHandler;
import com.example.just.businesinfo.connect.XmlContentHandlerIngot;
import com.example.just.businesinfo.connect.XmlContentHandlerMetal;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MetalFragment extends Fragment {

    private ListView lv;
    DatabaseHandler db;

    ParseXmlMetal parseXmlMetal;
    LoadDBMetal loadDBMetal;
    SwipeRefreshLayout mySwipeRefreshLayout;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_metal, container, false);

        lv = (ListView) view.findViewById(R.id.list_metal);
        mySwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);

        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        try {
                            if (!hasConnection(getActivity())) {
                                Toast.makeText(getActivity(), "Network is not connected", Toast.LENGTH_LONG).show();
                                mySwipeRefreshLayout.setRefreshing(false);
                            } else {
                                parseXmlMetal = new MetalFragment.ParseXmlMetal();
                                parseXmlMetal.execute();
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

        db = new DatabaseHandler(getActivity());

        obtainMetal(new Handler(Looper.getMainLooper()), new ISuccess<Integer>() {
            @Override
            public void onSuccess(Integer createTableMetal) {
                try {
                    if (createTableMetal >= 1) {
                    } else if (createTableMetal <= 1) {
                        parseXmlMetal = new MetalFragment.ParseXmlMetal();
                        parseXmlMetal.execute();
                    }
                    loadDBMetal = new MetalFragment.LoadDBMetal();
                    loadDBMetal.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    private void obtainMetal(final Handler handler, final ISuccess<Integer> success) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final int createTableMetal = db.getCreateTableMetal();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        success.onSuccess(createTableMetal);
                    }
                });
            }
        }).start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getActivity(), SettingActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onResume() {
        super.onResume();
        loadDBMetal = new MetalFragment.LoadDBMetal();
        loadDBMetal.execute();
    }

    public static boolean hasConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }


    public class ParseXmlMetal extends AsyncTask<Void, Void, ArrayList<MetalDataSet>> {

        public static final String LOG_TAG = "MetalFragment.java";

        @Override
        protected ArrayList<MetalDataSet> doInBackground(Void... argo0) {
            ArrayList<MetalDataSet> metalDataSetResult = new ArrayList<MetalDataSet>();
            try {
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH) + 1;
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                InputSource inputSourceMetal = null;

                InputSource inputSourceIngot = null;

                URL urlMetal = new URL("http://www.nbrb.by/Services/XmlMetalsRef.aspx");
                URL urlIngot = new URL("http://www.nbrb.by/Services/XmlIngots.aspx?onDate=" + mMonth + "/" + mDay + "/" + mYear);

                inputSourceMetal = new InputSource(urlMetal.openStream());

                inputSourceIngot = new InputSource(urlIngot.openStream());

                SAXParserFactory saxParserFactoryMetal = SAXParserFactory
                        .newInstance();
                SAXParser saxParserMetal = saxParserFactoryMetal.newSAXParser();

                SAXParserFactory saxParserFactoryIngot = SAXParserFactory
                        .newInstance();
                SAXParser saxParserIngot = saxParserFactoryIngot.newSAXParser();

                XMLReader xmlReaderMetal = saxParserMetal.getXMLReader();
                XMLReader xmlReaderIngot = saxParserIngot.getXMLReader();

                XmlContentHandlerMetal xmlContentHandlerMetal = new XmlContentHandlerMetal();
                xmlReaderMetal.setContentHandler(xmlContentHandlerMetal);

                XmlContentHandlerIngot xmlContentHandlerIngot = new XmlContentHandlerIngot();
                xmlReaderIngot.setContentHandler(xmlContentHandlerIngot);

                xmlReaderMetal.parse(inputSourceMetal);

                xmlReaderIngot.parse(inputSourceIngot);

                List<MetalDataSet> metalDataSets = new ArrayList<MetalDataSet>();
                metalDataSets = xmlContentHandlerMetal.getMetalData();
                int metalSize = metalDataSets.size();

                List<IngotDataSet> ingotDataSets = new ArrayList<IngotDataSet>();
                ingotDataSets = xmlContentHandlerIngot.getIngotData();
                int ingotSize = ingotDataSets.size();

                Iterator<MetalDataSet> i = metalDataSets.iterator();
                MetalDataSet dataItemMetal;

                Iterator<IngotDataSet> j = ingotDataSets.iterator();
                IngotDataSet dataItemIngot;

                for (int a = 0; a < metalSize; a++)
                    for (int b = 0; b < ingotSize; b++) {
                        if (metalDataSets.get(a).getMetal().equals(ingotDataSets.get(b).getMetalId())) {
                            MetalDataSet metalDataSet = db.getMetalDataSetByName(ingotDataSets.get(b).getMetalId(), ingotDataSets.get(b).getNominal());
                            if (metalDataSet != null) {
                                db.updateMetalDataSet(ingotDataSets.get(b).getMetalId(), ingotDataSets.get(b).getNominal(), ingotDataSets.get(b).getBanksDollars(), ingotDataSets.get(b).getCertificateRubles());
                            } else {
                                db.addMetal(new MetalDataSet(metalDataSets.get(a).getMetal(), metalDataSets.get(a).getName(), metalDataSets.get(a).getNameEng(), "true"), new IngotDataSet(ingotDataSets.get(b).getNominal(), ingotDataSets.get(b).getBanksDollars(), ingotDataSets.get(b).getCertificateRubles()));
                            }
                        }
                    }
                metalDataSetResult = db.getAllMetal();
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return metalDataSetResult;
        }

        @Override
        protected void onPostExecute(ArrayList<MetalDataSet> result) {
            super.onPostExecute(result);
            ListAdapter adapter = new MetalAdapter(
                    getActivity(), result);
            lv.setAdapter(adapter);
            mySwipeRefreshLayout.setRefreshing(false);
        }
    }

    public class LoadDBMetal extends AsyncTask<Void, Void, ArrayList<MetalDataSet>> {

        public static final String LOG_TAG = "MetalFragment.java";

        @Override
        protected ArrayList<MetalDataSet> doInBackground(Void... argo0) {
            ArrayList<MetalDataSet> metalDataSets = new ArrayList<>();
            try {
                metalDataSets = db.getAllMetal();
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return metalDataSets;
        }

        @Override
        protected void onPostExecute(ArrayList<MetalDataSet> result) {
            super.onPostExecute(result);
            ListAdapter adapter = new MetalAdapter(
                    getActivity(), result);
            lv.setAdapter(adapter);
            mySwipeRefreshLayout.setRefreshing(false);
        }
    }
}
