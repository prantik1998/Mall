package com.autonise.myapplication;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends FragmentActivity {

    DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("HI", "Hello");

        mDemoCollectionPagerAdapter =
                new DemoCollectionPagerAdapter(getSupportFragmentManager());
        Log.d("HI", "Hello1");
        mViewPager = findViewById(R.id.pager);
        Log.d("HI", "Hello2");
        mViewPager.setAdapter(mDemoCollectionPagerAdapter);
        Log.d("HI", "Hello3");
    }
}

