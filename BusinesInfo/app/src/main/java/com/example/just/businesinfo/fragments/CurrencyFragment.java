package com.example.just.businesinfo.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.just.businesinfo.ISuccess;
import com.example.just.businesinfo.R;
import com.example.just.businesinfo.connect.DatabaseHandler;
import com.example.just.businesinfo.Entity.ParsedDataSet;
import com.example.just.businesinfo.connect.XmlContentHandler;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class CurrencyFragment extends Fragment implements View.OnClickListener {

    private ProgressBar progressBar;
    private Button btn1;
    private ListView lv;
    DatabaseHandler db;
    ProgressDialog dialog;


    ParseXmlAsync parseXmlAsyn;
    LoadDBCurrency loadDBCurrency;
    //   SharedPreferences charCode, scale, name, rate;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_currency, container, false);


        lv = (ListView) view.findViewById(R.id.list);

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
                        parseXmlAsyn = new ParseXmlAsync();
                        parseXmlAsyn.execute();
                    }

                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


//

        db = new DatabaseHandler(getActivity());

        obtainCurrency(new Handler(Looper.getMainLooper()), new ISuccess<Integer>() {
            @Override
            public void onSuccess(Integer createTableCurrency) {
                try {
                    if (createTableCurrency >= 1) {
                        Log.v("CurrencyFragment", "Table is true");
                    } else if (createTableCurrency <= 1) {

                        showProgressBar();
                        dialog = ProgressDialog.show(getActivity(), "", "Загрузка. Пожалуйста подождите...", true);
                        parseXmlAsyn = new ParseXmlAsync();
                        parseXmlAsyn.execute();
                        Log.v("CurrencyFragment", "Table is false");
                    }

                    // parse our XML
                    showProgressBar();
                    dialog = ProgressDialog.show(getActivity(), "", "Загрузка. Пожалуйста подождите...", true);
                    loadDBCurrency = new LoadDBCurrency();
                    loadDBCurrency.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    private void obtainCurrency(final Handler handler, final ISuccess<Integer> success) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final int createTableCurrency = db.getCreateTableCurrency();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        success.onSuccess(createTableCurrency);
                    }
                });
            }
        }).start();
    }

    public void onResume() {
        super.onResume();
        showProgressBar();
        loadDBCurrency = new LoadDBCurrency();
        loadDBCurrency.execute();

//        Log.d(LOG_TAG, "Fragment1 onResume");
    }

//    protected void onDestroy() {
//        super.onDestroy();
//        // закрываем подключение при выходе
//        bd.close();
//    }


//    public void onClickBnt1(View v) {
//        try {
//
//            if (!hasConnection(getActivity())) {
//                Toast.makeText(getActivity(), "Network is not connected", Toast.LENGTH_LONG).show();
//            } else {
//                Toast.makeText(getActivity(), "Network is connecting", Toast.LENGTH_SHORT).show();
//                progressBar.setVisibility(View.VISIBLE);
//                parseXmlAsyn = new ParseXmlAsync();
//                parseXmlAsyn.execute();
//            }
//
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }


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


    @Override
    public void onClick(View v) {


    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (data == null){
//            return;
//        }
//        String name = data.getStringExtra("name");
//
//    }

    public class ParseXmlAsync extends AsyncTask<Void, Void, List<HashMap<String, String>>> {

        public static final String LOG_TAG = "MainActivity.java";

        @Override
        protected List<HashMap<String, String>> doInBackground(Void... argo0) {
            List<HashMap<String, String>> currencyDataSetResult = new ArrayList<>();

            try {

//                db.onDrop();
                // db.onCreate(db);
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH) + 1;
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                // initialize our input source variable
                InputSource inputSource = null;

                // XML from URL

                // specify a URL
                // make sure you are connected to the internet
                URL url = new URL("http://www.nbrb.by/Services/XmlExRates.aspx?ondate=" + mMonth + "/" + mDay + "/" + mYear);
                inputSource = new InputSource(url.openStream());
                Log.v(LOG_TAG, "Url: " + url);

                // instantiate SAX parser
                SAXParserFactory saxParserFactory = SAXParserFactory
                        .newInstance();
                SAXParser saxParser = saxParserFactory.newSAXParser();

                // get the XML reader
                XMLReader xmlReader = saxParser.getXMLReader();

                // prepare and set the XML content or data handler before
                // parsing
                XmlContentHandler xmlContentHandler = new XmlContentHandler();
                xmlReader.setContentHandler(xmlContentHandler);

                // parse the XML input source
                xmlReader.parse(inputSource);

                // put the parsed data to a List
                List<ParsedDataSet> parsedDataSet = xmlContentHandler
                        .getParsedData();

                // we'll use an iterator so we can loop through the data
                Iterator<ParsedDataSet> i = parsedDataSet.iterator();
                ParsedDataSet dataItem;

                while (i.hasNext()) {

                    dataItem = (ParsedDataSet) i.next();

                    String currency = dataItem.getCurrency();
                    Log.v(LOG_TAG, "currency: " + currency);

                    Log.v(LOG_TAG, "NumCode: " + dataItem.getNumCode());
                    Log.v(LOG_TAG, "CharCode: " + dataItem.getCharCode());
                    Log.v(LOG_TAG, "Scale: " + dataItem.getScale());
                    Log.v(LOG_TAG, "Name: " + dataItem.getName());
                    Log.v(LOG_TAG, "Rate: " + dataItem.getRate());

                    ParsedDataSet parsedDataSets = db.getParsedDataSetByName(dataItem.getNumCode());
                    if (parsedDataSets != null) {
                        db.updateDataSet(dataItem.getRate(), dataItem.getNumCode());
                    } else {
                        db.addContact(new ParsedDataSet(dataItem.getCurrency(), dataItem.getNumCode(), dataItem.getCharCode(), dataItem.getScale(), dataItem.getName(), dataItem.getRate(), "true"));
                    }
                }

                currencyDataSetResult = db.getAllContactsHash();
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return currencyDataSetResult;
//            return contactList;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {
            super.onPostExecute(result);
//            List<ParsedDataSet> parsedDataSets = db.getAllContacts();
//            List<HashMap<String, String>> parsedDataSets = db.getAllContactsHash();

//            R.layout.list_item = db.getContactsCount();

            ListAdapter adapter = new SimpleAdapter(
                    getActivity(), result,
                    R.layout.list_item, new String[]{"CharCode", "Scale", "Name", "Rate"},
                    new int[]{R.id.nameEng, R.id.Scale, R.id.nominal, R.id.Rate});
            lv.setAdapter(adapter);
/*
            for (ParsedDataSet cn : parsedDataSets) {
                String log = "CharCode: " + cn.getCharCode() + " ,SCale: " + cn.getScale() + " ,Name: " + cn.getName() + " Rate: " + cn.getRate();
                // Writing Contacts to log

                Log.d("Name: ", log);

                }
//
            }*/
//            db.onDrop();
            dialog.dismiss();
            hideProgressBar();
        }
    }


    public class LoadDBCurrency extends AsyncTask<Void, Void, List<HashMap<String, String>>> {

        public static final String LOG_TAG = "CurrencyFragment.java";

        @Override
        protected List<HashMap<String, String>> doInBackground(Void... argo0) {
            List<HashMap<String, String>> parsedDataSets = new ArrayList<>();

            try {

//                Log.v(LOG_TAG, "Load AsyncTask " );

                parsedDataSets = db.getAllContactsHash();


            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return parsedDataSets;
//            return contactList;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {
            super.onPostExecute(result);

            ListAdapter adapter = new SimpleAdapter(
                    getActivity(), result,
                    R.layout.list_item, new String[]{"CharCode", "Scale", "Name", "Rate"},
                    new int[]{R.id.nameEng, R.id.Scale, R.id.nominal, R.id.Rate});
            lv.setAdapter(adapter);
            dialog.dismiss();
                    hideProgressBar();
//            db.onDrop();
//            progressBar.setVisibility(View.INVISIBLE);
        }
    }

}
