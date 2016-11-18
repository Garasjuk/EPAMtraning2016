package com.example.just.businesinfo;

import android.content.SyncStatusObserver;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.example.just.businesinfo.adapter.BoxAdapter;
import com.example.just.businesinfo.adapter.TabsAdapter;
import com.example.just.businesinfo.connect.DatabaseHandler;
import com.example.just.businesinfo.connect.ParsedDataSet;
import com.example.just.businesinfo.fragments.Currency;
import com.example.just.businesinfo.fragments.Metal;
import com.example.just.businesinfo.fragments.SettingCurrency;
import com.example.just.businesinfo.fragments.SettingMetal;
import com.example.just.businesinfo.fragments.Weather;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class SettingActivity extends AppCompatActivity {

    public static final String LOG_TAG = "SettingActivity.java";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        TabHost mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();
        ViewPager mViewPager = (ViewPager) findViewById(R.id.pager);
        TabsAdapter mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);
        mTabsAdapter.addTab(mTabHost.newTabSpec("simple").setIndicator("Currency"), SettingCurrency.class, null);
        mTabsAdapter.addTab(mTabHost.newTabSpec("contacts").setIndicator("Metal"), SettingMetal.class, null);

        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }

    }

}
