package com.mufan.jsons;

import android.util.Log;

import com.mufan.models.OFPort;
import com.mufan.models.SwitchPortBandwidth;
import com.mufan.shelly.FloodlightProvider;
import com.mufan.utils.HTTPUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * StatisticsCollector class
 * Created by mufan on 2016/6/3.
 */
public class StatisticsCollector {
    private static FloodlightProvider floodlightProvider = FloodlightProvider.getSingleton();
    private static JSONObject jsonObject;
    private static JSONArray jsonArray;
    private static Future<Object> future;

    private static final String TAG = "StatisticsCollector";

    public static boolean enableStatistics() throws IOException{
        if (floodlightProvider.getController().getIP() == null || floodlightProvider.getController().getOpenFlowPort() == -1) {
            Log.e(TAG, "enableStatistics: Don't set the controller IP or Port");
            return false;
        }
        String url = "http://" + floodlightProvider.getController().getIP()
                + ":" + floodlightProvider.getController().getOpenFlowPort()
                + "/wm/statistics/config/enable/json";
        future = HTTPUtil.getJsonFromURL(url);
        try {
            String json = (String)future.get(5, TimeUnit.SECONDS);
            if(json == null) return false;
            jsonObject = new JSONObject(json);
            if (jsonObject.getString("statistics-collection").equalsIgnoreCase("enabled")) {
                Log.i(TAG, "enableStatistics: Enable statistics collection success");
                return true;
            }
            else {
                Log.i(TAG, "enableStatistics: Enable statistics collection failed");
                return false;
            }
        } catch (InterruptedException e){
            Log.e(TAG, "enableStatistics: Failed to enable statistics collection:" + e.getMessage());
            return false;
        } catch (TimeoutException e) {
            Log.e(TAG, "enableStatistics: Failed to enable statistics collection:" + e.getMessage());
            return false;
        }catch (ExecutionException e) {
            Log.e(TAG, "enableStatistics: Failed to enable statistics collection:" + e.getMessage());
            return false;
        } catch (JSONException e) {
            Log.e(TAG, "enableStatistics: Failed to enable statistics collection:" + e.getMessage());
            return false;
        }
    }

    public static boolean disableStatistics() throws IOException{
        if (floodlightProvider.getController().getIP()==null||floodlightProvider.getController().getOpenFlowPort()==-1) {
            Log.e(TAG, "disableStatistics: Don't set the controller IP or Port");
            return false;
        }
        String url = "http://" + floodlightProvider.getController().getIP()
                + ":" + floodlightProvider.getController().getOpenFlowPort()
                + "/wm/statistics/config/disable/json";
        future = HTTPUtil.getJsonFromURL(url);
        try {
            String json = (String)future.get(5, TimeUnit.SECONDS);
            jsonObject = new JSONObject(json);
            if (jsonObject.getString("statistics-collection").equalsIgnoreCase("disabled")) {
                Log.i(TAG, "disableStatistics: Enable statistics collection success");
                return true;
            }
            else {
                Log.i(TAG, "disableStatistics: Enable statistics collection failed");
                return false;
            }
        } catch (InterruptedException e){
            Log.e(TAG, "disableStatistics: Failed to enable statistics collection:" + e.getMessage());
            return false;
        } catch (TimeoutException e) {
            Log.e(TAG, "disableStatistics: Failed to enable statistics collection:" + e.getMessage());
            return false;
        }catch (ExecutionException e) {
            Log.e(TAG, "disableStatistics: Failed to enable statistics collection:" + e.getMessage());
            return false;
        } catch (JSONException e) {
            Log.e(TAG, "disableStatistics: Failed to enable statistics collection:" + e.getMessage());
            return false;
        }
    }

    public static SwitchPortBandwidth getSwitchPortBandwidth(String sw, OFPort port) throws IOException{
        SwitchPortBandwidth switchPortBandwidth = null;
        if (floodlightProvider.getController().getIP()==null||floodlightProvider.getController().getOpenFlowPort()==-1) {
            Log.e(TAG, "getSwitchPortBandwidth: Don't set the controller IP or Port");
            return null;
        }
        String url = "http://" + floodlightProvider.getController().getIP()
                + ":" + floodlightProvider.getController().getOpenFlowPort()
                + "/wm/statistics/bandwidth/" + sw + "/" + port.getPortNumber() +"/json";
        future = HTTPUtil.getJsonFromURL(url);
        try {
            String json = (String)future.get(5, TimeUnit.SECONDS);
            jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject= (JSONObject)jsonArray.get(i);
                if (jsonObject.getString("port").equals("local")) {
                    continue;
                }
                if (jsonObject.getInt("port") == port.getPortNumber()) {
                    switchPortBandwidth = SwitchPortBandwidth.of(jsonObject.getString("dpid"), OFPort.of(jsonObject.getInt("port")), jsonObject.getLong("bits-per-second-rx"), jsonObject.getLong("bits-per-second-tx"));
                    break;
                }
            }
        } catch (InterruptedException e){
            Log.e(TAG, "getSwitchPortBandwidth: Failed to enable statistics collection:" + e.getMessage());
            return null;
        } catch (TimeoutException e) {
            Log.e(TAG, "getSwitchPortBandwidth: Failed to enable statistics collection:" + e.getMessage());
            return null;
        }catch (ExecutionException e) {
            Log.e(TAG, "getSwitchPortBandwidth: Failed to enable statistics collection:" + e.getMessage());
            return null;
        } catch (JSONException e) {
            Log.e(TAG, "getSwitchPortBandwidth: Failed to enable statistics collection:" + e.getMessage());
            return null;
        }
        return switchPortBandwidth;
    }

    public static List<SwitchPortBandwidth> getSwitchAllPortBandwidth(String sw, String port) throws IOException {
        List<SwitchPortBandwidth> switchPortBandwidths = new ArrayList<>();
        if (floodlightProvider.getController().getIP() == null || floodlightProvider.getController().getOpenFlowPort() == -1) {
            Log.e(TAG, "getSwitchAllPortBandwidth: Don't set the controller IP or Port");
            return switchPortBandwidths;
        }
        String url = "http://" + floodlightProvider.getController().getIP()
                + ":" + floodlightProvider.getController().getOpenFlowPort()
                + "/wm/statistics/bandwidth/" + sw + "/" + port +"/json";
        future = HTTPUtil.getJsonFromURL(url);
        try {
            String json = (String)future.get(5, TimeUnit.SECONDS);
            jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject= (JSONObject)jsonArray.get(i);
                if (jsonObject.getString("port").equals("local")) {
                    continue;
                }
                switchPortBandwidths.add(SwitchPortBandwidth.of(jsonObject.getString("dpid"), OFPort.of(jsonObject.getInt("port")), jsonObject.getLong("bits-per-second-rx"), jsonObject.getLong("bits-per-second-tx")));
            }
        } catch (InterruptedException e){
            Log.e(TAG, "getSwitchAllPortBandwidth: Failed to enable statistics collection:" + e.getMessage());
            return switchPortBandwidths;
        } catch (TimeoutException e) {
            Log.e(TAG, "getSwitchAllPortBandwidth: Failed to enable statistics collection:" + e.getMessage());
            return switchPortBandwidths;
        }catch (ExecutionException e) {
            Log.e(TAG, "getSwitchAllPortBandwidth: Failed to enable statistics collection:" + e.getMessage());
            return switchPortBandwidths;
        } catch (JSONException e) {
            Log.e(TAG, "getSwitchAllPortBandwidth: Failed to enable statistics collection:" + e.getMessage());
            return switchPortBandwidths;
        }
        return switchPortBandwidths;
    }
}
