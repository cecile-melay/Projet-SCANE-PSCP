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
import com.example.denis.funculture.main.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by denis on 05/03/2017.
 */

//Ici on mettra toutes les fonctions usuelles nécessaires à différents endroits du code
public class Util {
    private static MainActivity mainActivity;

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

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    public static void setMainActivity(MainActivity activity) {
        mainActivity = activity;
    }

    public static String formatTimeInMillis(long time) {
        return String.format("%02d : %02d",
                TimeUnit.MILLISECONDS.toMinutes(time),
                TimeUnit.MILLISECONDS.toSeconds(time) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)));
    }

    public static String formatTimeInSec(int time) {
        return formatTimeInMillis(time * 1000);
    }

    public static double timeDiff(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return 0;
        }
        long diff = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long days = diff / daysInMilli;
        diff = diff % daysInMilli;

        long hours = diff / hoursInMilli;
        diff = diff % hoursInMilli;

        long minutes = diff / minutesInMilli;
        diff = diff % minutesInMilli;

        long seconds = diff / secondsInMilli;

        double result = days * 24 + hours + (double)minutes / (double)60;
        return result;
    }

    public static double getHourFromDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int hours = cal.get(Calendar.HOUR_OF_DAY);
        int mins = cal.get(Calendar.MINUTE);

        double result = hours + (double)mins / (double)60;
        return result;
    }

    public static String getStringDayMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String month = new SimpleDateFormat("MMMM").format(cal.getTime());
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return String.format("%d %s.", day, month.substring(0, 4));
    }

    public static String getStringHourMinute(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minutes = cal.get(Calendar.MINUTE);

        return String.format("%02d:%02d", hour, minutes);
    }
}
