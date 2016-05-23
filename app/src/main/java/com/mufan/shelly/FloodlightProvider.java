package com.mufan.shelly;

import com.mufan.models.Controller;
import com.mufan.models.Device;
import com.mufan.models.Link;
import com.mufan.models.Switch;

import java.util.List;

/**
 * FloodlightProvider class
 * Created by mufan on 2016/5/22.
 */
public class FloodlightProvider {

    private Controller controller = new Controller();
    private List<Switch> switchs;
    private List<Device> devices;
    private List<Link> links;

    private static final String TAG = "FloodlightProvider";
    private static FloodlightProvider INSTANCE;
    public static FloodlightProvider getSingleton(){
        if (INSTANCE == null) {
            INSTANCE = new FloodlightProvider();
        }
        return INSTANCE;
    }
    private FloodlightProvider(){
    }

    public Controller getController(){
        //controller.setIP("127.0.0.1");
        //controller.setOpenFlowPort(8080);
        return controller;
    }

//    public List<Device> getDevices(boolean update){
//        if (update) {
//            try {
//                devices = JsonToDevices.getDevices();
//            } catch (IOException e) {
//                Log.e(TAG, "getDevices: failed to get Devices information:" + e.getMessage());
//                return null;
//            }
//            return devices;
//        }else {
//            return devices;
//        }
//    }
//
//    public List<Switch> getSwitches(boolean update){
//        if (update) {
//            try {
//                switchs = JsonToSwitches.getSwitches();
//            } catch (Exception e) {
//                Log.e(TAG, "getDevices: failed to get Switches information:" + e.getMessage());
//            }
//            return switchs;
//        }
//        else {
//            return switchs;
//        }
//    }
//
//    public List<Link> getLinks(boolean update){
//        if (update) {
//            try {
//                links = JsonToLinks.getLinks();
//            } catch (IOException e) {
//                Log.e(TAG, "getDevices: failed to get Links information:" + e.getMessage());
//            }
//            return links;
//        }
//        else {
//            return links;
//        }
//    }
}
