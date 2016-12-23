package com.example.just.businesinfo;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;

import com.example.just.businesinfo.adapter.TabsAdapter;
import com.example.just.businesinfo.fragments.SettingCurrencyFragment;
import com.example.just.businesinfo.fragments.SettingMetalFragment;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        TabHost mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();
        ViewPager mViewPager = (ViewPager) findViewById(R.id.pager);
        TabsAdapter mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);
        mTabsAdapter.addTab(mTabHost.newTabSpec("simple").setIndicator(getString(com.example.just.businesinfo.R.string.SettingCurrencyTab)), SettingCurrencyFragment.class, null);
        mTabsAdapter.addTab(mTabHost.newTabSpec("contacts").setIndicator(getString(com.example.just.businesinfo.R.string.SettingMetalTab)), SettingMetalFragment.class, null);

        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }
    }
}
