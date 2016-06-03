package com.mufan.shelly;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mufan.shelly.listItem.HostContent;

public class HostsActivity extends AppCompatActivity implements HostItemFragment.OnListFragmentInteractionListener{

    private HostItemFragment hostItemFragment;
    private static final String TAG = "HostsActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hosts);

        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            if (hostItemFragment == null) {
                hostItemFragment = HostItemFragment.newInstance(1);
            }
            ft.replace(R.id.fragment_host_main, hostItemFragment).commit();
        }
    }

    @Override
    public void onListFragmentInteraction(HostContent.HostItem item) {

    }
}
