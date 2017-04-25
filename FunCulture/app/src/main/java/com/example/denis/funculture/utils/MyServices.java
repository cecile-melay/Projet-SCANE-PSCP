package com.example.denis.funculture.utils;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by denis on 19/04/2017.
 */

public class MyServices {
    private static MyServices singleton;
    private static final String SERVER_IP = "92.222.71.189";
    private static final String SERVER_PORT = "1337";

    public static MyServices getSingleton() {
        if(singleton == null) {
            singleton = new MyServices();
        }

        return singleton;
    }

    private String getUrl(String function) {
        return String.format("http://%s:%s/%s", SERVER_IP, SERVER_PORT, function);
    }

    private String addParamToUrl(String url, String param) {
        return String.format(url + "/%s", param);
    }

    public void getZones() {
        MyTask task = new MyTask();
        String functionName = "getZones";
        task.execute(getUrl(functionName));
    }

    public void postPoint(String id, String lat, String lng, String pos, String path) {
        MyTask task = new MyTask();
        String functionName = "insertPoint";
        String url = getUrl(functionName);
        url = addParamToUrl(url, id);
        url = addParamToUrl(url, lat);
        url = addParamToUrl(url, lng);
        url = addParamToUrl(url, pos);
        url = addParamToUrl(url, path);

        Log.d("PostPoint url : ", url);
        task.execute(url);
    }

    private class MyTask extends AsyncTask<String, String, String> {
        private static final String TAG = "MyTask";
        ProgressDialog dialog;
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Util.getMainActivity());
            dialog.setMessage("Please wait");
            dialog.setCancelable(false);
            dialog.show();
        }

        protected String doInBackground(String... params) {
            HttpURLConnection connection;
            BufferedReader reader;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                }
                Log.d("getZones response : ", buffer.toString());
                connection.disconnect();
                reader.close();
                return buffer.toString();


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (dialog.isShowing()){
                dialog.dismiss();
            }

            Util.createDialog(result);
            Log.d(TAG, result);
        }
    }
}


