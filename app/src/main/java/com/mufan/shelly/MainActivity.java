package com.mufan.shelly;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            String ip = bundle.getString("ip");
            String port = bundle.getString("port");
            TextView txt = (TextView) findViewById(R.id.txt);
            txt.setText(ip+":"+port);
        }
    }
}
