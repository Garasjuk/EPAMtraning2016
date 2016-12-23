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
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
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

    private ListView lv;
    DatabaseHandler db;
    ProgressDialog dialog;
    SwipeRefreshLayout mySwipeRefreshLayout;

    ParseXmlAsync parseXmlAsyn;
    LoadDBCurrency loadDBCurrency;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_currency, container, false);

        lv = (ListView) view.findViewById(R.id.list);
        mySwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);

        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        try {
                            if (!hasConnection(getActivity())) {
                                Toast.makeText(getActivity(), R.string.Network_is_not_conn, Toast.LENGTH_LONG).show();
                                mySwipeRefreshLayout.setRefreshing(false);
                            } else {
                                parseXmlAsyn = new ParseXmlAsync();
                                parseXmlAsyn.execute();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

        db = new DatabaseHandler(getActivity());

        obtainCurrency(new Handler(Looper.getMainLooper()), new ISuccess<Integer>() {
            @Override
            public void onSuccess(Integer createTableCurrency) {
                try {
                    if (createTableCurrency < 1) {
                        if (createTableCurrency <= 1) {
                            dialog = ProgressDialog.show(getActivity(), "", getString(R.string.Loading), true);
                            parseXmlAsyn = new ParseXmlAsync();
                            parseXmlAsyn.execute();
                        }
                    }
                    dialog = ProgressDialog.show(getActivity(), "", getString(R.string.Loading), true);
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
        loadDBCurrency = new LoadDBCurrency();
        loadDBCurrency.execute();
    }

    public static boolean hasConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getActiveNetworkInfo();
        return wifiInfo != null && wifiInfo.isConnectedOrConnecting();
    }

    @Override
    public void onClick(View v) {
    }

    public class ParseXmlAsync extends AsyncTask<Void, Void, List<HashMap<String, String>>> {

        @Override
        protected List<HashMap<String, String>> doInBackground(Void... argo0) {
            List<HashMap<String, String>> currencyDataSetResult = new ArrayList<>();

            try {
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH) + 1;
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                InputSource inputSource;

                URL url = new URL(getString(R.string.CurrencyURL) + mMonth + "/" + mDay + "/" + mYear);
                inputSource = new InputSource(url.openStream());

                SAXParserFactory saxParserFactory = SAXParserFactory
                        .newInstance();
                SAXParser saxParser = saxParserFactory.newSAXParser();

                XMLReader xmlReader = saxParser.getXMLReader();

                XmlContentHandler xmlContentHandler = new XmlContentHandler();
                xmlReader.setContentHandler(xmlContentHandler);

                xmlReader.parse(inputSource);

                List<ParsedDataSet> parsedDataSet = xmlContentHandler
                        .getParsedData();

                Iterator<ParsedDataSet> i = parsedDataSet.iterator();
                ParsedDataSet dataItem;

                while (i.hasNext()) {

                    dataItem = i.next();

                    ParsedDataSet parsedDataSets = db.getParsedDataSetByName(dataItem.getNumCode());
                    if (parsedDataSets != null) {
                        db.updateDataSet(dataItem.getRate(), dataItem.getNumCode());
                    } else {
                        db.addContact(new ParsedDataSet(dataItem.getCurrency(), dataItem.getNumCode(), dataItem.getCharCode(), dataItem.getScale(), dataItem.getName(), dataItem.getRate(), "true"));
                    }
                }
                currencyDataSetResult = db.getAllContactsHash();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return currencyDataSetResult;
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
            mySwipeRefreshLayout.setRefreshing(false);
        }
    }

    public class LoadDBCurrency extends AsyncTask<Void, Void, List<HashMap<String, String>>> {

        @Override
        protected List<HashMap<String, String>> doInBackground(Void... argo0) {
            List<HashMap<String, String>> parsedDataSets = new ArrayList<>();

            try {
                parsedDataSets = db.getAllContactsHash();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return parsedDataSets;
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
            mySwipeRefreshLayout.setRefreshing(false);
        }
    }
}
