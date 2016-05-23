package com.mufan.shelly;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static FloodlightProvider floodlightProvider = FloodlightProvider.getSingleton();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            String ip = bundle.getString("ip");
            floodlightProvider.getController().setIP(ip);
            String port = bundle.getString("port");
            floodlightProvider.getController().setOpenFlowPort(Integer.parseInt(port));

            TextView txt = (TextView) findViewById(R.id.txt);
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
        ImageButton mHostButton = (ImageButton) findViewById(R.id.hostbt);
    }
}
