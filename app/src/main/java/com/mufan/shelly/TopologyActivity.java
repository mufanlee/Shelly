package com.mufan.shelly;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mufan.models.Device;
import com.mufan.models.Switch;
import com.mufan.utils.HexString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import twaver.Consts;
import twaver.layout.AutoLayouter;
import twaver.model.ElementBox;
import twaver.model.Link;
import twaver.model.Node;
import twaver.network.Network;

public class TopologyActivity extends AppCompatActivity {

    private static FloodlightProvider floodlightProvider = FloodlightProvider.getSingleton();
    private static final String TAG = "TopologyActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topology);

        Network network = new Network(this);
        ElementBox box = network.getElementBox();
        AutoLayouter layouter = new AutoLayouter(network, Consts.LAYOUT_CIRCULAR);

        List<Switch> switches = floodlightProvider.getSwitches(false);
        Map<String, Node> switchesMap = new HashMap<>();
        List<Device> devices = floodlightProvider.getDevices(false);
        List<com.mufan.models.Link> links = floodlightProvider.getLinks(false);

        if (switches == null) {
            switches = floodlightProvider.getSwitches(true);
        }
        Log.d(TAG, "onCreate: " + switches.size());
        for (int i = 0; i < switches.size(); i++) {
            Node sw = new Node();
            //sw.setName(switches.get(i).getDpid());
            sw.setName("S"+ HexString.toLong(switches.get(i).getDpid()));
            sw.setImage("/res/drawable/switcs.png");
            switchesMap.put(switches.get(i).getDpid(), sw);
            box.add(sw);
        }

        if (devices == null) {
            devices = floodlightProvider.getDevices(true);
        }
        for (int i = 0; i < devices.size(); i++) {
            Node host = new Node();
            String ipv4Addr = devices.get(i).getIpv4_addr();
            int pos = ipv4Addr.lastIndexOf('.');
            int hostNumber = Integer.valueOf(ipv4Addr.substring(pos+1));
            host.setName("H"+hostNumber);
            host.setImage("/res/drawable/host.png");
            //node.setName(devices.get(i).getIpv4_addr());
            box.add(host);
            Link link = new Link();
            link.setFrom(host);
            link.setTo(switchesMap.get(devices.get(i).getAttachmentPoint().getSwitchDPID()));
            box.add(link);
        }

        if (links == null) {
            links = floodlightProvider.getLinks(true);
        }
        for (int i = 0; i < links.size(); i++) {
            Link link = new Link();
            link.setFrom(switchesMap.get(links.get(i).getSrcSwitch()));
            link.setTo(switchesMap.get(links.get(i).getDstSwtich()));
            box.add(link);
        }
        this.setContentView(network);
        layouter.doLayout();
    }
}
