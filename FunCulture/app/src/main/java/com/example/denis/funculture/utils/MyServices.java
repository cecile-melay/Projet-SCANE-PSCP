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

    public static MyServices getSingleton() {
        if(singleton == null) {
            singleton = new MyServices();
        }

        return singleton;
    }

    public void getZones() {
        JsonTask task = new JsonTask();

        task.execute("http://92.222.71.189:1337/getZones");
    }
    private class JsonTask extends AsyncTask<String, String, String> {
        private static final String TAG = "JsonTask";
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
                    Log.d("Response: ", "> " + line);

                }

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
            Log.d(TAG, result);
        }
    }
}


