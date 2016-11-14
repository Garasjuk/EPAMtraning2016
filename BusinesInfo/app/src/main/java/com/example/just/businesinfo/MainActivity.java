package com.example.just.businesinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

import com.example.just.businesinfo.adapter.TabsAdapter;
import com.example.just.businesinfo.fragments.Currency;
import com.example.just.businesinfo.fragments.Metal;
import com.example.just.businesinfo.fragments.Weather;

public class MainActivity extends AppCompatActivity {

    private TabHost mTabHost;
    private ViewPager mViewPager;
    private TabsAdapter mTabsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);
        mTabsAdapter.addTab(mTabHost.newTabSpec("simple").setIndicator("Currency"), Currency.class, null);
        mTabsAdapter.addTab(mTabHost.newTabSpec("contacts").setIndicator("Metal"), Metal.class, null);
        mTabsAdapter.addTab(mTabHost.newTabSpec("weather").setIndicator("Weather"), Weather.class, null);

        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();

//        Log.d(TAG, "MainActivity: onRestart()");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);

            Log.v("Setings", "Setings: ");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}