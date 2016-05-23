package com.mufan.shelly;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.mufan.jsons.JsonToController;
import com.mufan.shelly.listItem.ModulesContent;
import com.mufan.shelly.listItem.TablesContent;

import java.io.IOException;

public class ControllerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TablesFragment.OnListFragmentInteractionListener,
        StatusFragment.OnFragmentInteractionListener, ModulesFragment.OnListFragmentInteractionListener {

    private static FloodlightProvider floodlightProvider = FloodlightProvider.getSingleton();
    private static final String TAG = "ControllerActivity";

    private StatusFragment statusFragment;
    private TablesFragment tableFragment;
    private ModulesFragment modulesFragment;

    private Fragment currentFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        try {
            JsonToController.getControllerInfo();
        } catch (IOException e) {
            Log.e(TAG, "onCreate: failed to get controller infomation: " + e.getMessage());
            //e.printStackTrace();
        }

        initFragment(savedInstanceState);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.controller, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        setTitle(item.getTitle());
        if (id == R.id.nav_info) {
            if (statusFragment == null) {
                String hostname = floodlightProvider.getController().getIP()+":"+floodlightProvider.getController().getOpenFlowPort();
                String status = floodlightProvider.getController().getHealth();
                String role = floodlightProvider.getController().getRole();
                String uptime = String.valueOf(floodlightProvider.getController().getUptime());
                statusFragment = StatusFragment.newInstance(hostname, status, role, uptime);
            }
            switchFragment(currentFragment, statusFragment);
        } else if (id == R.id.nav_tables) {
            if (tableFragment == null) {
                tableFragment = TablesFragment.newInstance(1, floodlightProvider.getController().getTables());
            }
            switchFragment(currentFragment, tableFragment);
        } else if (id == R.id.nav_loadedModules) {
            if (modulesFragment == null) {
                modulesFragment = ModulesFragment.newInstance(1, floodlightProvider.getController().getLoadedModules());
            }
            switchFragment(currentFragment, modulesFragment);
        } else if (id == R.id.nav_memory) {

        } else if (id == R.id.nav_allModules) {

        } else if (id == R.id.nav_back) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void initFragment(Bundle savedInstanceState)
    {
        //判断activity是否重建，如果不是，则不需要重新建立fragment.
        if(savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            if(statusFragment == null) {
                if (floodlightProvider.getController().getIP() != null && floodlightProvider.getController().getRole() != null) {
                    String hostname = floodlightProvider.getController().getIP()+":"+floodlightProvider.getController().getOpenFlowPort();
                    String status = floodlightProvider.getController().getHealth();
                    String role = floodlightProvider.getController().getRole();
                    String uptime = String.valueOf(floodlightProvider.getController().getUptime());
                    statusFragment = StatusFragment.newInstance(hostname, status, role, uptime);
                } else {
                    statusFragment = StatusFragment.newInstance("", "", "", "");
                }
            }
            currentFragment = statusFragment;
            ft.replace(R.id.main_frame, statusFragment).commit();
        }
    }

    public void switchFragment(Fragment from, Fragment to) {

        if (currentFragment != to) {
            currentFragment = to;
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            if (!to.isAdded()) {
                ft.hide(from).add(R.id.main_frame, to).commit();
            } else {
                ft.hide(from).show(to).commit();
            }
        }
    }

    @Override
    public void onListFragmentInteraction(TablesContent.DBTable item) {


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(ModulesContent.Module item) {

    }
}
