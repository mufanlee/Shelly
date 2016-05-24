package com.mufan.shelly;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.mufan.shelly.listItem.SwitchContent;

public class SwitchActivity extends AppCompatActivity implements SwitchItemFragment.OnListFragmentInteractionListener{

    private SwitchItemFragment switchItemFragment;
    private static final String TAG = "SwitchActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swtich);

        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            if (switchItemFragment == null) {
                switchItemFragment = SwitchItemFragment.newInstance(1);
            }
            ft.replace(R.id.fragment_switch_main, switchItemFragment).commit();
        }
    }

    @Override
    public void onListFragmentInteraction(SwitchContent.SwitchItem item) {
        Log.d(TAG, "onListFragmentInteraction: " + item.dpid);
        Intent intent = new Intent(SwitchActivity.this, SwitchDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("from","SwitchActivity");
        bundle.putString("dpid",item.dpid);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
