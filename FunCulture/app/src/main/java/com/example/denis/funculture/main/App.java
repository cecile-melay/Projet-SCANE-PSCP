package com.example.denis.funculture.main;

import android.content.Context;

/**
 * Created by denis on 05/03/2017.
 */

public class App {
    //Ici on stockera toutes les variables globales de l'app
    private static App instance;
    private static Context context;

    public static App getSingleton() {
        if(instance != null) {
            return instance;
        }

        return new App();
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext (Context c) {
        context = c;
    }
}
