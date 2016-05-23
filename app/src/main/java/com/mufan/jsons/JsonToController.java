package com.mufan.jsons;

import android.util.Log;

import com.mufan.models.Memory;
import com.mufan.shelly.FloodlightProvider;
import com.mufan.utils.HTTPUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * JsonToController class
 * Created by mufan on 2016/5/22.
 */
public class JsonToController {

    private static FloodlightProvider floodlightProvider = FloodlightProvider.getSingleton();
    private static final String TAG = "JsonToController";
    public static boolean getControllerInfo()throws IOException {

        if (floodlightProvider.getController().getIP() == null || floodlightProvider.getController().getOpenFlowPort() == -1) {
            Log.e(TAG, "getControllerInfo: not set the controller IP or Port");
            return false;
        }
        String roleURL = "http://" + floodlightProvider.getController().getIP()
                + ":" + floodlightProvider.getController().getOpenFlowPort()
                + "/wm/core/role/json";

        String tablesURL = "http://" + floodlightProvider.getController().getIP()
                + ":" + floodlightProvider.getController().getOpenFlowPort()
                + "/wm/core/storage/tables/json";

        String healthURL = "http://" + floodlightProvider.getController().getIP()
                + ":" + floodlightProvider.getController().getOpenFlowPort()
                + "/wm/core/health/json";

        String allModulesURL = "http://" + floodlightProvider.getController().getIP()
                + ":" + floodlightProvider.getController().getOpenFlowPort()
                + "/wm/core/module/all/json";

        String loadedModulesURL = "http://" + floodlightProvider.getController().getIP()
                + ":" + floodlightProvider.getController().getOpenFlowPort()
                + "/wm/core/module/loaded/json";

        String memoryURL = "http://" + floodlightProvider.getController().getIP()
                + ":" + floodlightProvider.getController().getOpenFlowPort()
                + "/wm/core/memory/json";

        String uptimeURL = "http://" + floodlightProvider.getController().getIP()
                + ":" + floodlightProvider.getController().getOpenFlowPort()
                + "/wm/core/system/uptime/json";

        Future<Object> futureRole = HTTPUtil.getJsonFromURL(roleURL);
        Future<Object> futureTables = HTTPUtil.getJsonFromURL(tablesURL);
        Future<Object> futureAllModules = HTTPUtil.getJsonFromURL(allModulesURL);
        Future<Object> futureHealth = HTTPUtil.getJsonFromURL(healthURL);
        Future<Object> futureLoadedModules = HTTPUtil.getJsonFromURL(loadedModulesURL);
        Future<Object> futureMemory = HTTPUtil.getJsonFromURL(memoryURL);
        Future<Object> futureUptime = HTTPUtil.getJsonFromURL(uptimeURL);

        JSONObject jsonObject;
        try {
            String jsonRole = (String) futureRole.get(5, TimeUnit.SECONDS);
            jsonObject = new JSONObject(jsonRole);
            floodlightProvider.getController().setRole(jsonObject.getString("role"));
        } catch (InterruptedException e) {
            Log.e(TAG, "getControllerInfo: failed to get role of controller: " + e.getMessage());
            return false;
        } catch (ExecutionException e) {
            Log.e(TAG, "getControllerInfo: failed to get role of controller: " + e.getMessage());
            return false;
        } catch (TimeoutException e) {
            Log.e(TAG, "getControllerInfo: failed to get role of controller: " + e.getMessage());
            return false;
        } catch (JSONException e) {
            Log.e(TAG, "getControllerInfo: failed to get role of controller: " + e.getMessage());
            return false;
        }

        try {
            String jsonTables = (String) futureTables.get(5,TimeUnit.SECONDS);
            JSONArray jsonArray = new JSONArray(jsonTables);
            List<String> tables = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                tables.add(jsonArray.getString(i));
            }
            floodlightProvider.getController().setTables(tables);
        } catch (InterruptedException e) {
            Log.e(TAG, "getControllerInfo: failed to get tables of controller:" + e.getMessage());
            return false;
        } catch (ExecutionException e) {
            Log.e(TAG, "getControllerInfo: failed to get tables of controller:" + e.getMessage());
            return false;
        } catch (TimeoutException e) {
            Log.e(TAG, "getControllerInfo: failed to get tables of controller:" + e.getMessage());
            return false;
        } catch (JSONException e) {
            Log.e(TAG, "getControllerInfo: failed to get tables of controller:" + e.getMessage());
            return false;
        }

        try {
            String jsonHealth = (String) futureHealth.get(5, TimeUnit.SECONDS);
            jsonObject = new JSONObject(jsonHealth);
            if (jsonObject.getBoolean("healthy")) {
                floodlightProvider.getController().setHealth("Yes");
            } else{
                floodlightProvider.getController().setHealth("No");
            }
        } catch (InterruptedException e) {
            Log.e(TAG, "getControllerInfo: failed to get healthy of controller:" + e.getMessage());
            return false;
        } catch (ExecutionException e) {
            Log.e(TAG, "getControllerInfo: failed to get healthy of controller:" + e.getMessage());
            return false;
        } catch (TimeoutException e) {
            Log.e(TAG, "getControllerInfo: failed to get healthy of controller:" + e.getMessage());
            return false;
        } catch (JSONException e) {
            Log.e(TAG, "getControllerInfo: failed to get healthy of controller:" + e.getMessage());
            return false;
        }

        try {
            String jsonMemory = (String) futureMemory.get(5, TimeUnit.SECONDS);
            jsonObject = new JSONObject(jsonMemory);
            Memory memory = new Memory(jsonObject.getLong("free"), jsonObject.getLong("total"));
            floodlightProvider.getController().setMemory(memory);
        } catch (InterruptedException e) {
            Log.e(TAG, "getControllerInfo: failed to get memory of controller:" + e.getMessage());
            return false;
        } catch (ExecutionException e) {
            Log.e(TAG, "getControllerInfo: failed to get memory of controller:" + e.getMessage());
            return false;
        } catch (TimeoutException e) {
            Log.e(TAG, "getControllerInfo: failed to get memory of controller:" + e.getMessage());
            return false;
        } catch (JSONException e) {
            Log.e(TAG, "getControllerInfo: failed to get memory of controller:" + e.getMessage());
            return false;
        }

        try {
            String jsonLoadedModules = (String) futureLoadedModules.get(5, TimeUnit.SECONDS);
            jsonObject = new JSONObject(jsonLoadedModules);
            List<String> modules = new ArrayList<>();
            Iterator<?> i = jsonObject.keys();
            while (i.hasNext()) {
                String key = (String) i.next();
                if (jsonObject.get(key) instanceof JSONObject) {
                    modules.add(key);
                }
            }
            floodlightProvider.getController().setLoadedModules(modules);
        } catch (InterruptedException e) {
            Log.e(TAG, "getControllerInfo: failed to get loaded-modules of controller:" + e.getMessage());
            return false;
        } catch (ExecutionException e) {
            Log.e(TAG, "getControllerInfo: failed to get loaded-modules of controller:" + e.getMessage());
            return false;
        } catch (TimeoutException e) {
            Log.e(TAG, "getControllerInfo: failed to get loaded-modules of controller:" + e.getMessage());
            return false;
        } catch (JSONException e) {
            Log.e(TAG, "getControllerInfo: failed to get loaded-modules of controller:" + e.getMessage());
            return false;
        }

        try {
            String jsonAllModules = (String) futureAllModules.get(5, TimeUnit.SECONDS);
            jsonObject = new JSONObject(jsonAllModules);
            List<String> modules = new ArrayList<>();
            Iterator<?> i = jsonObject.keys();
            while (i.hasNext()) {
                String key = (String) i.next();
                if (jsonObject.get(key) instanceof JSONObject) {
                    modules.add(key);
                }
            }
            floodlightProvider.getController().setAllModules(modules);
        } catch (InterruptedException e) {
            Log.e(TAG, "getControllerInfo: failed to get all-modules of controller:" + e.getMessage());
            return false;
        } catch (ExecutionException e) {
            Log.e(TAG, "getControllerInfo: failed to get all-modules of controller:" + e.getMessage());
            return false;
        } catch (TimeoutException e) {
            Log.e(TAG, "getControllerInfo: failed to get all-modules of controller:" + e.getMessage());
            return false;
        } catch (JSONException e) {
            Log.e(TAG, "getControllerInfo: failed to get all-modules of controller:" + e.getMessage());
            return false;
        }

        try {
            String jsonUptime = (String) futureUptime.get(5, TimeUnit.SECONDS);
            jsonObject = new JSONObject(jsonUptime);
            floodlightProvider.getController().setUptime(jsonObject.getLong("systemUptimeMsec"));
        } catch (InterruptedException e) {
            Log.e(TAG, "getControllerInfo: failed to get uptime of controller:" + e.getMessage());
            return false;
        } catch (ExecutionException e) {
            Log.e(TAG, "getControllerInfo: failed to get uptime of controller:" + e.getMessage());
            return false;
        } catch (TimeoutException e) {
            Log.e(TAG, "getControllerInfo: failed to get uptime of controller:" + e.getMessage());
            return false;
        } catch (JSONException e) {
            Log.e(TAG, "getControllerInfo: failed to get uptime of controller:" + e.getMessage());
            return false;
        }
        return true;
    }
}
