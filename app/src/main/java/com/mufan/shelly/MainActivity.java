package com.mufan.shelly;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private static FloodlightProvider floodlightProvider = FloodlightProvider.getSingleton();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle.get("from").equals("LoginActivity")) {
                String ip = bundle.getString("ip");
                floodlightProvider.getController().setIP(ip);
                String port = bundle.getString("port");
                floodlightProvider.getController().setOpenFlowPort(Integer.parseInt(port));
            }
        }

        ImageButton mControllerButton = (ImageButton) findViewById(R.id.conbt);
        mControllerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cIntent = new Intent(MainActivity.this, ControllerActivity.class);
                startActivity(cIntent);
            }
        });

        ImageButton mSwitchButton = (ImageButton) findViewById(R.id.swbt);
        mSwitchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sIntent = new Intent(MainActivity.this, SwitchActivity.class);
                startActivity(sIntent);
            }
        });
        ImageButton mHostButton = (ImageButton) findViewById(R.id.hostbt);
        mHostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ImageButton mStatisticsButton = (ImageButton) findViewById(R.id.statistics);

        ImageButton mTopologyButton = (ImageButton) findViewById(R.id.topology);
        mTopologyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tIntent = new Intent(MainActivity.this, TopologyActivity.class);
                startActivity(tIntent);
            }
        });

        ImageButton mAboutButton = (ImageButton) findViewById(R.id.about);
        mAboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent aIntent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(aIntent);
            }
        });
    }
}
