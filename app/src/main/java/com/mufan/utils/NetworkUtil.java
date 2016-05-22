package com.mufan.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * This is NetworkUtils for judging the network status.
 * Created by lipeng on 2016/5/21.
 */
public class NetworkUtil {

    private static final String TAG = "NetworkUtil";

    /**
     * 判断ip和端口是否可达
     * @param ip ip地址
     * @param port 端口
     * @return 是否可达
     */
    public static boolean isConnect (String ip, int port) {

        String url = "http://" + ip + ":" + port + "/ui/index.html";
        Future<Object> future = HTTPUtil.isURLAvailable(url);
        try {
            boolean isOK = (boolean) future.get(5, TimeUnit.SECONDS);
            return isOK;
        } catch (InterruptedException e) {
            Log.e(TAG, "isConnect: cannot to connect the URL: " + url );
            Log.e(TAG, "isConnect: exception: " + e.getMessage());
            return false;
            // e.printStackTrace();
        } catch (ExecutionException e) {
            Log.e(TAG, "isConnect: cannot to connect the URL: " + url );
            Log.e(TAG, "isConnect: exception: " + e.getMessage());
            return false;
            //e.printStackTrace();
        } catch (TimeoutException e) {
            Log.e(TAG, "isConnect: cannot to connect the URL: " + url );
            Log.e(TAG, "isConnect: exception: " + e.getMessage());
            return false;
            //e.printStackTrace();
        }
    }

    /**
     * 判断当前网络是否是wifi
     * @param context Context
     * @return 是否是wifi
     */
    public static boolean isWifi(Context context) {

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (info != null) {
            return info.isConnected();
        }
        return false;
    }

    /**
     * 判断当前网络是否是手机网络
     * @param context Context
     * @return 是否是手机网络
     */
    public static boolean isMobile(Context context) {

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (info != null) {
            return info.isConnected();
        }
        return false;
    }

    /**
     * 判断当前是否有网络连接
     * @param activity Activity
     * @return 是否有网络连接
     */
    public static boolean isNetworkAvailable (Activity activity) {

        Context context = activity.getApplicationContext();
        boolean wifi = isWifi(context);
        boolean mobile = isMobile(context);
        if (!wifi && !mobile) {
            Log.d(TAG, "isNetworkAvailable: not have network");
            return false;
        } else if (wifi && !mobile) {
            Log.d(TAG, "isNetworkAvailable: wifi network");
            return true;
        } else if (!wifi && mobile){
            Log.d(TAG, "isNetworkAvailable: mobile network");
            return true;
        }
        return true;

//        // 获取手机所有连接管理对象
//        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (connectivityManager == null) {
//            return false;
//        } else {
//            // 获取NetworkInfo对象
//            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
//            if (networkInfo != null && networkInfo.length > 0) {
//                for (int i = 0; i < networkInfo.length; i++) {
//                    Log.d(TAG, "isNetworkAvailable: State: " + networkInfo[i].getState());
//                    Log.d(TAG, "isNetworkAvailable: Type: " + networkInfo[i].getTypeName());
//                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
//                        return true;
//                }
//            }
//        }
//        return false;
    }
}
