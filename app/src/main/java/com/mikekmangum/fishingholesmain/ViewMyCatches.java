package com.mikekmangum.fishingholesmain;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class ViewMyCatches extends AppCompatActivity {

    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private ViewPagerAdapter mAdapter;
    private TabLayout mTabLayout;
    private static Context sContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_catches);
        sContext= getApplicationContext();

        mToolbar = findViewById(R.id.toolBar);
        setSupportActionBar(mToolbar);

        mViewPager = findViewById(R.id.pager);

        Cursor res = DatabaseHelper.getInstance(this).getAllData();
        int rows = res.getCount();
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), rows);
        mViewPager.setAdapter(mAdapter);

        //mTabLayout = findViewById(R.id.tabs);

        //mTabLayout.setupWithViewPager(mViewPager);

    }

    public static Context getContext() {
        return sContext;
    }

}
