package com.example.denis.funculture.utils;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import com.example.denis.funculture.main.App;

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
}
