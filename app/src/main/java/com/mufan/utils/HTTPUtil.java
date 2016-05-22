package com.mufan.utils;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * This is a util of HTTP.
 * Created by lipeng on 2016/5/22.
 */
public class HTTPUtil {

    private final static int THREADS = Runtime.getRuntime().availableProcessors();
    public static ExecutorService executor = Executors.newFixedThreadPool(THREADS);
    private static final String TAG = "HTTPUtil";

    private static String readAll(Reader rd) throws IOException {

        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    /**
     * 判断该URL是否可以访问
     * @param surl URL
     * @return 是否可以访问
     */
    public static Future<Object> isURLAvailable(final String surl) {

        final Future<Object> future = executor.submit(new Callable<Object>() {

            @Override
            public Object call() throws Exception {

                try {
                    URL url = new URL(surl);
                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    //connection.setRequestProperty("Accept", "application/json");
                    int code = connection.getResponseCode();
                    if (code == 200) {
                        return true;
                    } else {
                        return false;
                    }
                } catch (IOException e) {
                    Log.e(TAG, "getJsonFromURL: failed to deserialize data from URL: "+ surl);
                    Log.e(TAG, "getJsonFromURL: exception: " +  e.getMessage());
                    return false;
                }
            }
        });
        return future;
    }

    public static Future<Object> getJsonFromURL(final String surl){

        final Future<Object> future = executor.submit(new Callable<Object>() {

            @Override
            public Object call() throws Exception {

                try {
                    URL url = new URL(surl);
                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Accept", "application/json");
                    connection.setDoOutput(true);
                    connection.setDoInput(true);

                    //send request
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream
                            , Charset.forName("UTF-8")));
                    String json = readAll(reader);
                    inputStream.close();
                    return json;
                } catch (IOException e) {
                    Log.e(TAG, "getJsonFromURL: failed to deserialize data from URL: "+ surl);
                    Log.e(TAG, "getJsonFromURL: exception: " +  e.getMessage());
                    return null;
                    //e.printStackTrace();
                }
            }
        });
        return future;
    }

    public static Future<Object> postJsonToURL(final String surl,final String json){

        final Future<Object> future = executor.submit(new Callable<Object>() {

            @Override
            public Object call() throws Exception {

                String jsonResponse = "";
                try {
                    URL url = new URL(surl);
                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-type", "application/json");
                    connection.setDoInput(true);
                    connection.setDoOutput(true);

                    //send request
                    OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                    writer.write(json);
                    writer.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = reader.readLine())!=null) {
                        jsonResponse = jsonResponse.concat(line);
                    }
                    writer.close();
                    reader.close();
                    return jsonResponse;
                } catch (IOException e) {
                    Log.e(TAG, "postJsonToURL: failed to Post data to URL: " + surl);
                    Log.e(TAG, "postJsonToURL: exception: " + e.getMessage() );
                    //e.printStackTrace();
                    return jsonResponse;
                }
            }
        });
        return future;
    }

    public static Future<Object> deleteJsonToURL(final String surl,final String json){

        final Future<Object> future = executor.submit(new Callable<Object>() {

            @Override
            public Object call() throws Exception {

                String jsonResponse = "";
                try {
                    DefaultHttpClient httpClient = new DefaultHttpClient();
                    HttpDeleteUtil httpDeleteUtil = new HttpDeleteUtil(surl);
                    StringEntity jsonEntity = new StringEntity(json);
                    //jsonEntity.setContentEncoding("UTF-8");
                    //jsonEntity.setContentType("application/json");
                    httpDeleteUtil.setEntity(jsonEntity);
                    HttpResponse response = httpClient.execute(httpDeleteUtil);
                    if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                        HttpEntity entity = response.getEntity();
                        jsonResponse = EntityUtils.getContentCharSet(entity);
                    }
                } catch (IOException e) {
                    Log.e(TAG, "deleteJsonToURL: failed to Delete data for URL: " + surl );
                    Log.e(TAG, "deleteJsonToURL: exception: " + e.getMessage() );
                    return jsonResponse;
                    //e.printStackTrace();
                }
                return jsonResponse;
            }
        });
        return future;
    }
}
