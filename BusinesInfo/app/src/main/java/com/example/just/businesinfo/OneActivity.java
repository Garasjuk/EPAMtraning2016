package com.example.just.businesinfo;

import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.just.businesinfo.connect.ParsedDataSet;
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

public class OneActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {


    private ListView lv;
    ArrayList<HashMap<String, String>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        contactList = new ArrayList<>();

        lv = (ListView) findViewById(R.id.list);

        try {
            // parse our XML
            new parseXmlAsync().execute();

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    protected void onDestroy() {
//        super.onDestroy();
//        // закрываем подключение при выходе
//        bd.close();
//    }

    @Override
    public void onClick(View v) {

    }

    public void onClickBnt1(View v) {
        try {
            // parse our XML
            new parseXmlAsync().execute();
//           bd =  new BD(this);
//            bd.open();
//            bd.addData();
//            Intent intent = new Intent(this, SecondActivity.class);
//            startActivityForResult(intent,1);

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    public class parseXmlAsync extends AsyncTask<Void, Void, Void> {

        public static final String LOG_TAG = "MainActivity.java";

        @Override
        protected Void doInBackground(Void... argo0) {

            try {

                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
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

                    HashMap<String, String> contact = new HashMap<>();

                    contact.put("currency", dataItem.getCurrency());
                    contact.put("NumCode", dataItem.getNumCode());
                    contact.put("CharCode", dataItem.getCharCode());
                    contact.put("Scale", dataItem.getScale());
                    contact.put("Name", dataItem.getName());
                    contact.put("Rate", dataItem.getRate());

                    contactList.add(contact);

                }

            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(
                    OneActivity.this, contactList,
                    R.layout.list_item, new String[]{  "CharCode", "Scale", "Name", "Rate"},
                    new int[]{  R.id.nameEng, R.id.Scale, R.id.nominal, R.id.Rate});
            lv.setAdapter(adapter);


        }

    }
}
