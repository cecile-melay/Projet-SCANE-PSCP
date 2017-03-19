package com.example.denis.funculture.main;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Context;

import com.example.denis.funculture.component.sensor.ActivityRecognizedService;

/**
 * Created by denis on 05/03/2017.
 */

public class App {
    //Ici on stockera toutes les variables globales de l'app
    private static App instance;
    private Context context;
    private Activity currentActivity;
    private Fragment currentFragment;
    private ActivityRecognizedService recognitionActivity;

    public static App getSingleton() {
        if(instance == null) {
            instance = new App();
        }

        return instance;
    }

    private App() {
        this.recognitionActivity = new ActivityRecognizedService();
    }

    public Context getContext() {
        return this.context;
    }

    public void setContext (Context c) {
        this.context = c;
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

    public ActivityRecognizedService getRecognitionActivity() {
        return this.recognitionActivity;
    }

    public void setCurrentFragment(Fragment currentFragment) {
        this.currentFragment = currentFragment;
    }

    public Fragment getCurrentFragment() {
        return currentFragment;
    }
}
