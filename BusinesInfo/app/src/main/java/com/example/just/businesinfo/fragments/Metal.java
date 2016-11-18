package com.example.just.businesinfo.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public class Metal extends Fragment {


    ProgressBar progressBar;
    Button btn1;
    private ListView lv;
    DatabaseHandler db;

    ParseXmlMetal parseXmlMetal;
    LoadDBMetal loadDBMetal;
    //   SharedPreferences charCode, scale, name, rate;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_metal, container, false);


        lv = (ListView) view.findViewById(R.id.list_metal);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
//        btn1 = (Button) view.findViewById(R.id.btn1);
//        btn1.hasOnClickListeners();
//

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        btn1 = (Button) view.findViewById(R.id.btn1);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!hasConnection(getActivity())) {
                        Toast.makeText(getActivity(), "Network is not connected", Toast.LENGTH_LONG).show();
                    } else {
//                        Toast.makeText(getActivity(), "Network is connecting", Toast.LENGTH_SHORT).show();
                        showProgressBar();
                        parseXmlMetal = new Metal.ParseXmlMetal();
                        parseXmlMetal.execute();
                    }

                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        db = new DatabaseHandler(getActivity());

        try {

            if (db.getCreateTableMetal() >= 1) {
                Log.v("Metal", "Table is true");
            } else if (db.getCreateTableMetal() <= 1) {

                parseXmlMetal = new Metal.ParseXmlMetal();
                parseXmlMetal.execute();
            }

            Log.v("Metal", "Loade");
            showProgressBar();
            loadDBMetal = new Metal.LoadDBMetal();
            loadDBMetal.execute();

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getActivity(), SettingActivity.class);
            startActivity(intent);

            Log.v("Setings", "Setings: ");
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void onResume() {
        super.onResume();
        showProgressBar();
        loadDBMetal = new Metal.LoadDBMetal();
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

    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }


    public class ParseXmlMetal extends AsyncTask<Void, Void, ArrayList<MetalDataSet>> {

        public static final String LOG_TAG = "Metal.java";

        @Override
        protected ArrayList<MetalDataSet> doInBackground(Void... argo0) {
            ArrayList<MetalDataSet> metalDataSetResult = new ArrayList<MetalDataSet>();
            try {

//                db.onDrop();
                // db.onCreate(db);

                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH) + 1;
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);


                // initialize our input source variable
                InputSource inputSourceMetal = null;

                InputSource inputSourceIngot = null;

                URL urlMetal = new URL("http://www.nbrb.by/Services/XmlMetalsRef.aspx");
                URL urlIngot = new URL("http://www.nbrb.by/Services/XmlIngots.aspx?onDate=" + mMonth + "/" + mDay + "/" + mYear);

                inputSourceMetal = new InputSource(urlMetal.openStream());
                Log.v(LOG_TAG, "Url: " + urlMetal);

                inputSourceIngot = new InputSource(urlIngot.openStream());
                Log.v(LOG_TAG, "Url: " + urlIngot);

                // instantiate SAX parser
                SAXParserFactory saxParserFactoryMetal = SAXParserFactory
                        .newInstance();
                SAXParser saxParserMetal = saxParserFactoryMetal.newSAXParser();

                SAXParserFactory saxParserFactoryIngot = SAXParserFactory
                        .newInstance();
                SAXParser saxParserIngot = saxParserFactoryIngot.newSAXParser();

                // get the XML reader
                XMLReader xmlReaderMetal = saxParserMetal.getXMLReader();
                XMLReader xmlReaderIngot = saxParserIngot.getXMLReader();

                // prepare and set the XML content or data handler before
                // parsing
                XmlContentHandlerMetal xmlContentHandlerMetal = new XmlContentHandlerMetal();
                xmlReaderMetal.setContentHandler(xmlContentHandlerMetal);

                XmlContentHandlerIngot xmlContentHandlerIngot = new XmlContentHandlerIngot();
                xmlReaderIngot.setContentHandler(xmlContentHandlerIngot);

                // parse the XML input source
                xmlReaderMetal.parse(inputSourceMetal);

                xmlReaderIngot.parse(inputSourceIngot);

                // put the parsed data to a List
                List<MetalDataSet> metalDataSets = new ArrayList<MetalDataSet>();
                metalDataSets = xmlContentHandlerMetal.getMetalData();
                int metalSize = metalDataSets.size();

                List<IngotDataSet> ingotDataSets = new ArrayList<IngotDataSet>();
                ingotDataSets = xmlContentHandlerIngot.getIngotData();
                int ingotSize = ingotDataSets.size();

                // we'll use an iterator so we can loop through the data
                Iterator<MetalDataSet> i = metalDataSets.iterator();
                MetalDataSet dataItemMetal;

                Iterator<IngotDataSet> j = ingotDataSets.iterator();
                IngotDataSet dataItemIngot;

                for (int a = 0; a < metalSize; a++)
                    for (int b = 0; b < ingotSize; b++) {
                        if (metalDataSets.get(a).getMetal().equals(ingotDataSets.get(b).getMetalId())) {

                            Log.v(LOG_TAG, "Id: " + metalDataSets.get(a).getMetal());
                            Log.v(LOG_TAG, "Name: " + metalDataSets.get(a).getName());
                            Log.v(LOG_TAG, "NameEng: " + metalDataSets.get(a).getNameEng());
//

                            Log.v(LOG_TAG, "MetalId: " + ingotDataSets.get(b).getMetalId());
                            Log.v(LOG_TAG, "Nominal: " + ingotDataSets.get(b).getNominal());

                            Log.v(LOG_TAG, "BanksDollars: " + ingotDataSets.get(b).getBanksDollars());
                            Log.v(LOG_TAG, "CertificateRubles: " + ingotDataSets.get(b).getCertificateRubles());

                            Log.v(LOG_TAG, "iteratorMetal: " + metalSize);
                            Log.v(LOG_TAG, "iteratorIngot: " + ingotSize);

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
            hideProgressBar();
        }
    }

    public class LoadDBMetal extends AsyncTask<Void, Void, ArrayList<MetalDataSet>> {

        public static final String LOG_TAG = "Currency.java";

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
            hideProgressBar();
        }
    }


}
