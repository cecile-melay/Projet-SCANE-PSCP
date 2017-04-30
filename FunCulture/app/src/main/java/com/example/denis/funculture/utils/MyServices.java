package com.example.denis.funculture.utils;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.example.denis.funculture.component.User;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by denis on 19/04/2017.
 */

public class MyServices {
    private static final String TAG = "MyServices";
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

    public void insertUser(User user) {
        MyTask task = new MyTask();

        ///insertUser/:prenom/:nom/:dateNaiss/:lvlSport/:fc/:ville/:mail/:pass
        String functionName = String.format("insertUser/%s/%s/%s/%d/%d/%s/%s/%s",
                user.getPrenom(),
                user.getNom(),
                user.getDateNaiss(),
                user.getLvlSport(),
                user.getFc(),
                user.getVille(),
                user.getMail(),
                user.getPass());

        String url = getUrl(functionName);
        Log.d(TAG, "insertUser url : " + url);
        task.execute(url);
    }

    public void postPoint(double lat, double lng, int pos, int path) {
        MyTask task = new MyTask();
        String functionName = "insertPoint";
        String url = getUrl(functionName);
        url = addParamToUrl(url, String.valueOf(lat));
        url = addParamToUrl(url, String.valueOf(lng));
        url = addParamToUrl(url, String.valueOf(pos));
        url = addParamToUrl(url, String.valueOf(path));

        Log.d("PostPoint url : ", url);
        task.execute(url);
    }

    private class MyTask extends AsyncTask<String, String, String> {
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
            if (result != null) {
                Util.createDialog(result);
                Log.d(TAG, result);
            } else {
                Log.d(TAG, "result null");
            }

        }
    }
}


