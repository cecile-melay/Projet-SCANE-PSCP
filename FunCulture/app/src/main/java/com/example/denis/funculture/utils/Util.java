package com.example.denis.funculture.utils;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import com.example.denis.funculture.main.App;

/**
 * Created by denis on 05/03/2017.
 */

//Ici on mettra toutes les fonctions usuelles nécessaires à différents endroits du code
public class Util {

    public static void createDialog(String message)
    {
        Activity activity = App.getSingleton().getCurrentActivity();
        if(activity == null)
            return;

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        builder.show();
    }

    public static void checkPrivileges(Activity AC, int MY_PERMISSIONS_REQUEST_GEOLOCATION_FINE, int MY_PERMISSIONS_REQUEST_GEOLOCATION_COARSE)
    {
        // Boite de dialogue pour demander les permissions GPS
        if (ActivityCompat.checkSelfPermission(AC, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AC, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AC,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_GEOLOCATION_FINE);
            ActivityCompat.requestPermissions(AC,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_GEOLOCATION_COARSE);
        }
    }

    public static void writeInPrefs(String key, String value) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(App.getSingleton().getContext());
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String readFromPrefs(String key) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(App.getSingleton().getContext());
        return settings.getString(key, null);
    }
}
