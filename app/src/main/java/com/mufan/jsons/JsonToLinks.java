package com.mufan.jsons;

import android.util.Log;

import com.mufan.models.Link;
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
 * JsonToLinks class
 * Created by mufan on 2016/5/24.
 */
public class JsonToLinks {

    private static FloodlightProvider floodlightProvider = FloodlightProvider.getSingleton();
    private static final String TAG = "JsonToLinks";
    public static List<Link> getLinks()throws IOException {
        if (floodlightProvider.getController().getIP() == null || floodlightProvider.getController().getOpenFlowPort() == -1) {
            Log.e(TAG, "getLinks: not set the controller IP or Port");
            return null;
        }
        String url = "http://" + floodlightProvider.getController().getIP()
                + ":" + floodlightProvider.getController().getOpenFlowPort()
                + "/wm/topology/links/json";

        Future<Object> future = HTTPUtil.getJsonFromURL(url);
        List<Link> links = new ArrayList<>();
        try {
            String jsonLink = (String) future.get(5, TimeUnit.SECONDS);
            JSONArray jsonArray = new JSONArray(jsonLink);
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Link link = new Link();
                link.setSrcSwitch(jsonObject.getString("src-switch"));
                link.setSrcPort(Integer.parseInt(jsonObject.getString("src-port")));
                link.setDstSwtich(jsonObject.getString("dst-switch"));
                link.setDstPort(Integer.parseInt(jsonObject.getString("dst-port")));
                link.setType(jsonObject.getString("type"));
                link.setDirection(jsonObject.getString("direction"));
                links.add(link);
            }
        } catch (InterruptedException e) {
            Log.e(TAG, "getLinks: failed to get Links information from Controller:" + e.getMessage());
            return null;
        } catch (TimeoutException e) {
            Log.e(TAG, "getLinks: failed to get Links information from Controller:" + e.getMessage());
            return null;
        } catch (ExecutionException e) {
            Log.e(TAG, "getLinks: failed to get Links information from Controller:" + e.getMessage());
            return null;
        } catch (JSONException e) {
            Log.e(TAG, "getLinks: failed to get Links information from Controller:" + e.getMessage());
            return null;
        }
        return links;
    }
}
