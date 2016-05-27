package com.mufan.jsons;

import android.util.Log;

import com.mufan.models.AttachmentPoint;
import com.mufan.models.Device;
import com.mufan.shelly.FloodlightProvider;
import com.mufan.utils.HTTPUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * JsonToDevices class
 * Created by mufan on 2016/5/24.
 */
public class JsonToDevices {

    private static FloodlightProvider floodlightProvider = FloodlightProvider.getSingleton();

    private static final String TAG = "JsonToDevices";
    public static List<Device> getDevices()throws IOException {
        if (floodlightProvider.getController().getIP() == null || floodlightProvider.getController().getOpenFlowPort() == -1) {
            Log.e(TAG, "getDevices: not set the controller IP or Port");
            return null;
        }
        String url = "http://" + floodlightProvider.getController().getIP()
                + ":" + floodlightProvider.getController().getOpenFlowPort()
                + "/wm/device/";

        Future<Object> future = HTTPUtil.getJsonFromURL(url);
        List<Device> devices = new ArrayList<>();
        try {
            String jsonDevices = (String) future.get(5, TimeUnit.SECONDS);
            JSONArray jsonArray = new JSONArray(jsonDevices);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Device device = new Device(jsonObject.getJSONArray("mac").getString(0));
                if (!jsonObject.getJSONArray("ipv4").isNull(0)) {
                    JSONArray ips = jsonObject.getJSONArray("ipv4");
                    if (ips.getString(0).equals("0.0.0.0"))
                        device.setIpv4_addr(ips.getString(1));
                    else
                        device.setIpv4_addr(ips.getString(0));
                }
                if (!jsonObject.getJSONArray("vlan").isNull(0)) {
                    String s = jsonObject.getJSONArray("vlan").getString(0);
                    s = s.replace("0x", "");
                    device.setVlan(Integer.valueOf(s,16));
                }
                if (!jsonObject.getJSONArray("attachmentPoint").isNull(0)) {
                    AttachmentPoint attachmentPoint = new AttachmentPoint(jsonObject.getJSONArray("attachmentPoint")
                            .getJSONObject(0).getString("switchDPID"));
                    attachmentPoint.setPort(jsonObject.getJSONArray("attachmentPoint")
                            .getJSONObject(0).getInt("port"));
                    attachmentPoint.setErrorStatus(jsonObject.getJSONArray("attachmentPoint")
                            .getJSONObject(0).getString("errorStatus"));
                    device.setAttachmentPoint(attachmentPoint);
                }
                Date d = new Date(jsonObject.getLong("lastSeen"));
                device.setLastSeen(d);
                devices.add(device);
            }
        } catch (InterruptedException e) {
            Log.e(TAG, "getDevices: failed to get Devices information from Controller:" + e.getMessage());
            return null;
        } catch (ExecutionException e) {
            Log.e(TAG, "getDevices: failed to get Devices information from Controller:" + e.getMessage());
            return null;
        } catch (TimeoutException e) {
            Log.e(TAG, "getDevices: failed to get Devices information from Controller:" + e.getMessage());
            return null;
        } catch (JSONException e) {
            Log.e(TAG, "getDevices: failed to get Devices information from Controller:" + e.getMessage());
            return null;
        }
        return devices;
    }
}
