package com.example.denis.funculture.utils;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.example.denis.funculture.component.Epreuve;
import com.example.denis.funculture.component.User;
import com.example.denis.funculture.component.localisation.MyPointOfInterest;
import com.example.denis.funculture.component.localisation.Path;
import com.example.denis.funculture.component.localisation.PointOfPath;
import com.example.denis.funculture.component.qcm.Answer;
import com.example.denis.funculture.component.qcm.QCM;
import com.example.denis.funculture.component.qcm.Question;
import com.example.denis.funculture.fragments.MapsFragment;
import com.google.android.gms.maps.model.LatLng;

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

    public void loginUser(String pseudo, String pass, final boolean showFailedMessage) {
        OnPostExecuteRunnable onPostExecuteRunnable = new OnPostExecuteRunnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonResult = new JSONObject(result);
                    Log.d(TAG, jsonResult.toString());
                    JSONArray resultArray = jsonResult.getJSONArray("rows");
                    JSONArray metaDataArray = jsonResult.getJSONArray("metaData");

                    if (resultArray.length() == 0 && showFailedMessage) {
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

    public void loadPointsOfInterest(int pathId, final boolean openMapAfterLoad) {
        OnPostExecuteRunnable onPostExecuteRunnable = new OnPostExecuteRunnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonResult = new JSONObject(result);
                    Log.d(TAG, jsonResult.toString());
                    JSONArray resultArray = jsonResult.getJSONArray("rows");
                    JSONArray metaDataArray = jsonResult.getJSONArray("metaData");

                    for (int j = 0; j < resultArray.length(); j++) {
                        JSONArray userData = resultArray.getJSONArray(j);
                        int point = 0;
                        int poiId = -1;
                        String name = "";
                        String description = "";
                        String sound = "";

                        for (int i = 0; i < userData.length(); i++) {
                            switch (metaDataArray.getJSONObject(i).getString("name")) {
                                case "ID":
                                    if(poiId == -1) {
                                        poiId = userData.getInt(i);
                                    }
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
                                    point = userData.getInt(i);
                                    break;
                            }
                        }

                        if (poiId != -1) {
                            Log.d(TAG, "load POI : " + poiId);
                            MyPointOfInterest poi = new MyPointOfInterest(poiId, point, name, description, sound);
                            Log.d(TAG, "load POI : " + poi.ToString());
                            loadEpreuve(poi);
                            Util.getCurrentPath().addPointOfInterest(poi);
                        }
                    }

                    if(openMapAfterLoad) {
                        Util.getMainActivity().startFragment(MapsFragment.class);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        MyTask task = new MyTask(onPostExecuteRunnable);
        String functionName = "getPathPointsOfInterest";
        String url = getUrl(functionName);
        url = addParamToUrl(url, Integer.toString(pathId));

        Log.d("PointsOfInterest url : ", url);
        task.execute(url);
    }

    public void loadPaths() {
        OnPostExecuteRunnable onPostExecuteRunnable = new OnPostExecuteRunnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonResult = new JSONObject(result);
                    Log.d(TAG, jsonResult.toString());
                    JSONArray resultArray = jsonResult.getJSONArray("rows");
                    JSONArray metaDataArray = jsonResult.getJSONArray("metaData");

                    for (int j = 0; j < resultArray.length(); j++) {
                        JSONArray userData = resultArray.getJSONArray(j);
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
                            Path path = new Path(id, name);
                            Util.addPath(path);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        MyTask task = new MyTask(onPostExecuteRunnable);
        String functionName = "getPaths";
        String url = getUrl(functionName);
        Log.d("getPaths url : ", url);
        task.execute(url);
    }

    public void loadPointsOfPath(int id, final boolean openMapAfterLoad) {
        OnPostExecuteRunnable onPostExecuteRunnable = new OnPostExecuteRunnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonResult = new JSONObject(result);
                    Log.d(TAG, jsonResult.toString());
                    JSONArray resultArray = jsonResult.getJSONArray("rows");
                    JSONArray metaDataArray = jsonResult.getJSONArray("metaData");

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
                    loadPointsOfInterest(Util.getCurrentPath().getId(), openMapAfterLoad);
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

    public void loadQCM(final Path path) {
        OnPostExecuteRunnable onPostExecuteRunnable = new OnPostExecuteRunnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonResult = new JSONObject(result);
                    Log.d(TAG, jsonResult.toString());
                    JSONArray resultArray = jsonResult.getJSONArray("rows");
                    JSONArray metaDataArray = jsonResult.getJSONArray("metaData");

                    for (int j = 0; j < resultArray.length(); j++) {
                        JSONArray userData = resultArray.getJSONArray(j);

                        int id = -1;
                        int idTag = 0;
                        String name = "";
                        int xp = 0;
                        for (int i = 0; i < userData.length(); i++) {
                            switch (metaDataArray.getJSONObject(i).getString("name")) {
                                case "NAME":
                                    name = userData.getString(i);
                                    break;
                                case "IDTAG":
                                    idTag = userData.getInt(i);
                                    break;
                                case "XP":
                                    xp = userData.getInt(i);
                                    break;
                                case "ID":
                                    id = userData.getInt(i);
                                    break;
                            }
                        }
                        if (id != -1 && name != null) {
                            QCM qcm = new QCM(id, name);
                            qcm.setXp(xp);
                            qcm.setIdTag(idTag);

                            path.setQCM(qcm);
                            loadQCMQuestions(qcm);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        MyTask task = new MyTask(onPostExecuteRunnable);
        String functionName = "getQcm";
        String url = getUrl(functionName);
        url = addParamToUrl(url, Integer.toString(path.getId()));

        Log.d("getQcm url : ", url);
        task.execute(url);
    }

    private void loadQCMQuestions(final QCM qcm) {
        OnPostExecuteRunnable onPostExecuteRunnable = new OnPostExecuteRunnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonResult = new JSONObject(result);
                    Log.d(TAG, jsonResult.toString());
                    JSONArray resultArray = jsonResult.getJSONArray("rows");
                    JSONArray metaDataArray = jsonResult.getJSONArray("metaData");

                for (int j = 0; j < resultArray.length(); j++) {
                        JSONArray userData = resultArray.getJSONArray(j);

                        int id = -1;
                        String category = "";
                        String name = "";
                        int xp = 0;
                        for (int i = 0; i < userData.length(); i++) {
                            switch (metaDataArray.getJSONObject(i).getString("name")) {
                                case "NAME":
                                    name = userData.getString(i);
                                    break;
                                case "CATEGORY":
                                    category = userData.getString(i);
                                    break;
                                case "ID":
                                    id = userData.getInt(i);
                                    break;
                            }
                        }
                        if (id != -1 && name != null) {
                            Question quest = new Question(category, name);
                            quest.setId(id);
                            qcm.addQuestion(quest);
                            loadAnswers(quest);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        MyTask task = new MyTask(onPostExecuteRunnable);
        String functionName = "getQuestions";
        String url = getUrl(functionName);
        url = addParamToUrl(url, Integer.toString(qcm.getId()));

        Log.d("getQuestions url : ", url);
        task.execute(url);
    }

    private void loadAnswers(final Question quest) {
        OnPostExecuteRunnable onPostExecuteRunnable = new OnPostExecuteRunnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonResult = new JSONObject(result);
                    Log.d(TAG, jsonResult.toString());
                    JSONArray resultArray = jsonResult.getJSONArray("rows");
                    JSONArray metaDataArray = jsonResult.getJSONArray("metaData");

                    for (int j = 0; j < resultArray.length(); j++) {
                        JSONArray userData = resultArray.getJSONArray(j);

                        String isTrue = "";
                        String name = "";
                        for (int i = 0; i < userData.length(); i++) {
                            switch (metaDataArray.getJSONObject(i).getString("name")) {
                                case "NAME":
                                    name = userData.getString(i);
                                    break;
                                case "ISTRUE":
                                    isTrue = userData.getString(i);
                                    break;
                            }
                        }
                        if (name != null) {
                            boolean bool = false;
                            if(isTrue.equals("Y")) {
                                bool = true;
                            }
                            Answer answer = new Answer(j, false, "" + j, name, bool);
                            quest.addAnswer(answer);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        MyTask task = new MyTask(onPostExecuteRunnable);
        String functionName = "getAnswers";
        String url = getUrl(functionName);
        url = addParamToUrl(url, Integer.toString(quest.getId()));

        Log.d("getAnswers url : ", url);
        task.execute(url);
    }

    private void loadEpreuve(final MyPointOfInterest poi) {
        OnPostExecuteRunnable onPostExecuteRunnable = new OnPostExecuteRunnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonResult = new JSONObject(result);
                    Log.d(TAG, jsonResult.toString());
                    JSONArray resultArray = jsonResult.getJSONArray("rows");
                    JSONArray metaDataArray = jsonResult.getJSONArray("metaData");

                    for (int j = 0; j < resultArray.length(); j++) {
                        JSONArray userData = resultArray.getJSONArray(j);

                        int id = -1;
                        String url = "";
                        String name = "";
                        int xp = 0;
                        for (int i = 0; i < userData.length(); i++) {
                            switch (metaDataArray.getJSONObject(i).getString("name")) {
                                case "ID":
                                    id = userData.getInt(i);
                                    break;
                                case "URL":
                                    url = userData.getString(i);
                                    break;
                                case "NAME":
                                    name = userData.getString(i);
                                    break;
                                case "XP":
                                    xp = userData.getInt(i);
                                    break;
                            }
                        }
                        if (name != null) {
                            Epreuve epreuve = new Epreuve();
                            epreuve.setName(name);
                            epreuve.setId(id);
                            epreuve.setXp(xp);
                            epreuve.setUrl(url);

                            poi.setEpreuve(epreuve);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        MyTask task = new MyTask(onPostExecuteRunnable);
        String functionName = "getEpreuve";
        String url = getUrl(functionName);
        url = addParamToUrl(url, Integer.toString(poi.getId()));

        Log.d("getEpreuve url : ", url);
        task.execute(url);
    }

    public void loadPath(int id, final boolean openMapsAfterLoad) {
        OnPostExecuteRunnable onPostExecuteRunnable = new OnPostExecuteRunnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonResult = new JSONObject(result);
                    Log.d(TAG, jsonResult.toString());
                    JSONArray resultArray = jsonResult.getJSONArray("rows");
                    JSONArray metaDataArray = jsonResult.getJSONArray("metaData");

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
                        Path path = new Path(id, name);
                        Util.setCurrentPath(path);
                        loadQCM(path);
                        loadPointsOfPath(id, openMapsAfterLoad);
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


