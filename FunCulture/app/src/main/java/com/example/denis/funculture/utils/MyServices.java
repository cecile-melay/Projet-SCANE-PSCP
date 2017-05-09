package com.example.denis.funculture.utils;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.example.denis.funculture.component.User;
import com.example.denis.funculture.component.localisation.MyPointOfInterest;
import com.example.denis.funculture.component.localisation.Path;
import com.example.denis.funculture.component.localisation.PointOfPath;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PointOfInterest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        if (singleton == null) {
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
        String functionName = String.format("insertUser/%s/%s/%s/%d/%d/%s/%s/%s/%s",
                user.getPrenom(),
                user.getNom(),
                user.getDateNaiss(),
                user.getLvlSport(),
                user.getFc(),
                user.getVille(),
                user.getMail(),
                user.getPass(),
                user.getPseudo());

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

    public void postPath(Path path) {
        MyTask task = new MyTask();
        String functionName = "insertPath";
        String url = getUrl(functionName);
        url = addParamToUrl(url, Integer.toString(path.getId()));
        url = addParamToUrl(url, path.getName());

        Log.d("PostPath url : ", url);
        task.execute(url);
    }

    public void loginUser(String pseudo, String pass) {
        OnPostExecuteRunnable onPostExecuteRunnable = new OnPostExecuteRunnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonResult = new JSONObject(result);
                    Log.d(TAG, jsonResult.toString());
                    JSONArray resultArray = jsonResult.getJSONArray("rows");
                    JSONArray metaDataArray = jsonResult.getJSONArray("metaData");

                    if (resultArray.length() == 0) {
                        Util.createToast(MyResources.LOGIN_FAILED);
                        return;
                    }

                    JSONArray userData = resultArray.getJSONArray(0);
                    User currentUser = new User();
                    for (int i = 0; i < userData.length(); i++) {
                        switch (metaDataArray.getJSONObject(i).getString("name")) {
                            case "ID":
                                int id = userData.getInt(i);
                                currentUser.setId(id);
                                break;
                            case "PRENOM":
                                String prenom = userData.getString(i);
                                currentUser.setPrenom(prenom);
                                break;
                            case "NOM":
                                String nom = userData.getString(i);
                                currentUser.setNom(nom);
                                break;
                            case "DATENAISS":
                                String dateNaiss = userData.getString(i);
                                currentUser.setDateNaiss(dateNaiss);
                                break;
                            case "SPORTLEVEL":
                                int lvl = userData.getInt(i);
                                currentUser.setLvlSport(lvl);
                                break;
                            case "VILLE":
                                String ville = userData.getString(i);
                                currentUser.setVille(ville);
                                break;
                            case "MAIL":
                                String mail = userData.getString(i);
                                currentUser.setMail(mail);
                                break;
                            case "PASS":
                                String pass = userData.getString(i);
                                currentUser.setPass(pass);
                                break;
                            case "FC":
                                int fc = userData.getInt(i);
                                currentUser.setFc(fc);
                                break;
                            case "XP":
                                int xp = userData.getInt(i);
                                currentUser.setXp(xp);
                                break;
                            case "PSEUDO":
                                String pseudo = userData.getString(i);
                                currentUser.setPseudo(pseudo);
                                break;
                        }
                    }
                    Util.setCurrentUser(currentUser);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        MyTask task = new MyTask(onPostExecuteRunnable);
        String functionName = "login";
        String url = getUrl(functionName);
        url = addParamToUrl(url, pseudo);
        url = addParamToUrl(url, pass);

        Log.d("loginUser url : ", url);
        task.execute(url);
    }

    public void getPointsOfInterest(int id) {
        OnPostExecuteRunnable onPostExecuteRunnable = new OnPostExecuteRunnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonResult = new JSONObject(result);
                    Log.d(TAG, jsonResult.toString());
                    JSONArray resultArray = jsonResult.getJSONArray("rows");
                    JSONArray metaDataArray = jsonResult.getJSONArray("metaData");

                    if (resultArray.length() == 0) {
                        Util.createToast(MyResources.LOGIN_FAILED);
                        return;
                    }

                    for (int j = 0; j < resultArray.length(); j++) {
                        JSONArray userData = resultArray.getJSONArray(j);
                        int id = -1;
                        int pointId = 0;
                        String name = "";
                        String description = "";
                        String sound = "";

                        for (int i = 0; i < userData.length(); i++) {
                            switch (metaDataArray.getJSONObject(i).getString("name")) {
                                case "ID":
                                    id = userData.getInt(i);
                                    break;
                                case "NAME":
                                    name = userData.getString(i);
                                    break;
                                case "DESCRIPTION":
                                    description = userData.getString(i);
                                    break;
                                case "ASSOCIATEDSOUND":
                                    sound = userData.getString(i);
                                    break;
                                case "ASSOCIATEDPATHPOINT":
                                    pointId = userData.getInt(i);
                                    break;
                            }
                        }

                        if (id != -1) {
                            MyPointOfInterest poi = new MyPointOfInterest(id, pointId, name, description, sound);
                            Util.getCurrentPath().addPointOfInterest(poi);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        MyTask task = new MyTask(onPostExecuteRunnable);
        String functionName = "getPathPointsOfInterest";
        String url = getUrl(functionName);
        url = addParamToUrl(url, Integer.toString(id));

        Log.d("PointsOfInterest url : ", url);
        task.execute(url);
    }

    public void getPointsOfPath(int id) {
        OnPostExecuteRunnable onPostExecuteRunnable = new OnPostExecuteRunnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonResult = new JSONObject(result);
                    Log.d(TAG, jsonResult.toString());
                    JSONArray resultArray = jsonResult.getJSONArray("rows");
                    JSONArray metaDataArray = jsonResult.getJSONArray("metaData");

                    if (resultArray.length() == 0) {
                        Util.createToast(MyResources.LOGIN_FAILED);
                        return;
                    }

                    for (int j = 0; j < resultArray.length(); j++) {
                        JSONArray userData = resultArray.getJSONArray(j);

                        int id = 0;
                        int position = -1;
                        double lat = 0;
                        double lng = 0;
                        for (int i = 0; i < userData.length(); i++) {
                            switch (metaDataArray.getJSONObject(i).getString("name")) {
                                case "LAT":
                                    lat = userData.getDouble(i);
                                    break;
                                case "LNG":
                                    lng = userData.getDouble(i);
                                    break;
                                case "POSITIONINPATH":
                                    position = userData.getInt(i);
                                    break;
                                case "ID":
                                    id = userData.getInt(i);
                                    break;
                            }
                        }
                        if (position != -1) {
                            PointOfPath point = new PointOfPath(id);
                            point.setPosition(position);
                            point.setLatLng(new LatLng(lat, lng));

                            Util.getCurrentPath().addPoint(point);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        MyTask task = new MyTask(onPostExecuteRunnable);
        String functionName = "getPathPoints";
        String url = getUrl(functionName);
        url = addParamToUrl(url, Integer.toString(id));

        Log.d("getPathPoints url : ", url);
        task.execute(url);
    }

    public void getPath(int id) {
        OnPostExecuteRunnable onPostExecuteRunnable = new OnPostExecuteRunnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonResult = new JSONObject(result);
                    Log.d(TAG, jsonResult.toString());
                    JSONArray resultArray = jsonResult.getJSONArray("rows");
                    JSONArray metaDataArray = jsonResult.getJSONArray("metaData");

                    if (resultArray.length() == 0) {
                        Util.createToast(MyResources.LOGIN_FAILED);
                        return;
                    }

                    JSONArray userData = resultArray.getJSONArray(0);
                    int id = -1;
                    String name = "";
                    for (int i = 0; i < userData.length(); i++) {
                        switch (metaDataArray.getJSONObject(i).getString("name")) {
                            case "ID":
                                id = userData.getInt(i);
                                break;
                            case "NAME":
                                name = userData.getString(i);
                                break;
                        }
                    }

                    if (id != -1) {
                        Util.setCurrentPath(new Path(id, name));
                        getPointsOfPath(id);
                        getPointsOfInterest(id);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        MyTask task = new MyTask(onPostExecuteRunnable);
        String functionName = "getPath";
        String url = getUrl(functionName);
        url = addParamToUrl(url, Integer.toString(id));

        Log.d("getPath url : ", url);
        task.execute(url);
    }

    public void updateUser(User user) {
        MyTask task = new MyTask();

        ///updateUser/:id/:prenom/:nom/:dateNaiss/:lvlSport/:fc/:ville/:mail/:pass
        String functionName = String.format("updateUser/%d/%s/%s/%s/%d/%d/%s/%s/%s/%s/%d",
                user.getId(),
                user.getPrenom(),
                user.getNom(),
                user.getDateNaiss(),
                user.getLvlSport(),
                user.getFc(),
                user.getVille(),
                user.getMail(),
                user.getPass(),
                user.getPseudo(),
                user.getXp());

        String url = getUrl(functionName);
        Log.d(TAG, "updateUser url : " + url);
        task.execute(url);
    }

    private class MyTask extends AsyncTask<String, String, String> {
        private ProgressDialog dialog;
        private OnPostExecuteRunnable onPostExecute;

        public MyTask() {
        }

        public MyTask(OnPostExecuteRunnable onPostExecute) {
            this.onPostExecute = onPostExecute;
        }

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
                    buffer.append(line + "\n");
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
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if (result != null) {
                Log.d(TAG, result);

                if (onPostExecute != null) {
                    this.onPostExecute.setResult(result);
                    this.onPostExecute.run();
                }
            } else {
                Log.d(TAG, "result null");
            }
        }
    }

    class OnPostExecuteRunnable implements Runnable {
        String result;

        public void setResult(String result) {
            this.result = result;
        }

        @Override
        public void run() {

        }
    }
}


