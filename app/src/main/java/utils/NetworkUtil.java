package utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;

/**
 * This is NetworkUtils for judging the network status.
 * Created by lipeng on 2016/5/21.
 */
public class NetworkUtil {

    private static final String TAG = "NetworkUtil";

    /**
     * 判断ip地址是否可达，当前用ping的方式
     * @param ip ip地址
     * @param port 端口
     * @return 是否可以连接
     */
    public static boolean isConnect (String ip, int port) {

        try {
            String cmd = "ping -c 1 " + ip;
            Process process = Runtime.getRuntime().exec(cmd);
            int status = process.waitFor();
            Log.d(TAG, "isConnect: " + status);
            if (status == 0) {
                Log.d(TAG, "isConnect: success.");
                return true;
            } else {
                Log.d(TAG, "isConnect: fail, cannot reach the ip address " + ip);
            }
        } catch (IOException e) {
            Log.d(TAG, "isConnect: fail, IOException " + e.getMessage());
        } catch (InterruptedException e) {
            Log.d(TAG, "isConnect: fail, InterruptedException " + e.getMessage());
        }
        return false;
    }

    /**
     * 判断网络是否开启
     * @param activity Activity
     * @return 网络是否开启
     */
    public static boolean isNetworkAvailable (Activity activity) {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    Log.d(TAG, "isNetworkAvailable: State: " + networkInfo[i].getState());
                    Log.d(TAG, "isNetworkAvailable: Type: " + networkInfo[i].getTypeName());
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }
}
