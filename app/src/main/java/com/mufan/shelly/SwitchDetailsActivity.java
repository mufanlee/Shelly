package com.mufan.shelly;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.mufan.models.Switch;

import java.util.List;

public class SwitchDetailsActivity extends AppCompatActivity {

    private static FloodlightProvider floodlightProvider = FloodlightProvider.getSingleton();
    private Switch sw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_details);

        TextView dpidview = (TextView) findViewById(R.id.dpidtext);
        TextView manuview = (TextView) findViewById(R.id.manutext);
        TextView hardview = (TextView) findViewById(R.id.hardtext);
        TextView softview = (TextView) findViewById(R.id.softtext);
        TextView serialview = (TextView) findViewById(R.id.serialtext);
        TextView packetview = (TextView) findViewById(R.id.packettext);
        TextView flowview = (TextView) findViewById(R.id.flowtext);
        TextView tableview = (TextView) findViewById(R.id.tabletext);
        TextView portview = (TextView) findViewById(R.id.porttext);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if ("SwitchActivity".equals(bundle.getString("from"))) {
                String dpid = bundle.getString("dpid");
                List<Switch> switchs = floodlightProvider.getSwitches(false);
                if (switchs != null) {
                    for (int i = 0; i < switchs.size(); i++) {
                        if (switchs.get(i).getDpid().equals(dpid)) {
                            sw = switchs.get(i);
                            break;
                        }
                    }

                    assert dpidview != null;
                    dpidview.setText(sw.getDpid());
                    assert manuview != null;
                    manuview.setText(sw.getMfr_desc());
                    assert hardview != null;
                    hardview.setText(sw.getHw_desc());
                    assert softview != null;
                    softview.setText(sw.getSw_desc());
                    assert serialview != null;
                    serialview.setText(sw.getSerial_num());
                    assert packetview != null;
                    packetview.setText(String.valueOf(sw.getPacket_count()));
                    assert flowview != null;
                    flowview.setText(String.valueOf(sw.getFlow_count()));
                    assert tableview != null;
                    tableview.setText(String.valueOf(sw.getTables()));
                    assert portview != null;
                    portview.setText(String.valueOf(sw.getPorts().size()));
                }
            }
        }
    }
}
