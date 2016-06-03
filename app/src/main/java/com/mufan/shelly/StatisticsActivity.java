package com.mufan.shelly;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class StatisticsActivity extends AppCompatActivity implements StatisticFragment.OnFragmentInteractionListener{

    private StatisticFragment statisticFragment;
    private static final String TAG = "StatisticsActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            if (statisticFragment == null) {
                statisticFragment = StatisticFragment.newInstance("","");
            }
            ft.replace(R.id.fragment_stattistics_main, statisticFragment).commit();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
