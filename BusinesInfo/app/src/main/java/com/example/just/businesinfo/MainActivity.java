package com.example.just.businesinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

import com.example.just.businesinfo.adapter.TabsAdapter;
import com.example.just.businesinfo.fragments.CurrencyFragment;
import com.example.just.businesinfo.fragments.MetalFragment;
import com.example.just.businesinfo.fragments.WeatherFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabHost mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();
        ViewPager mViewPager = (ViewPager) findViewById(R.id.pager);
        TabsAdapter mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);
        mTabsAdapter.addTab(mTabHost.newTabSpec("simple").setIndicator(getString(R.string.currencyTab)), CurrencyFragment.class, null);
        mTabsAdapter.addTab(mTabHost.newTabSpec("contacts").setIndicator(getString(R.string.metalTab)), MetalFragment.class, null);
        mTabsAdapter.addTab(mTabHost.newTabSpec("weather").setIndicator(getString(R.string.weatherTab)), WeatherFragment.class, null);

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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}