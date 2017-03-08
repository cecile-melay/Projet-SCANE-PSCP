package com.example.denis.funculture.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import com.example.denis.funculture.activities.MapsActivity;
import com.example.denis.funculture.main.App;
import com.example.denis.funculture.main.MainActivity;

/**
 * Created by denis on 05/03/2017.
 */

//Ici on mettra toutes les fonctions usuelles nécessaires à différents endroits du code
public class Util {
    public Dialog createDialog(String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(App.getContext());
        builder.setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        return builder.create();
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
}
