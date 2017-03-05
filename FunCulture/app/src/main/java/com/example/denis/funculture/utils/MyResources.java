package com.example.denis.funculture.utils;

import android.content.Context;

import com.example.denis.funculture.R;
import com.example.denis.funculture.main.App;

public class MyResources {
        //Ici un exemple de comment accéder à une string (on fera tous les appels ici afin de simplifier le code)
        public static final String GPS = App.getContext().getString(R.string.gps);
        public static final String ACCELEROMETER = App.getContext().getString(R.string.accelerometer);
        public static final String PEDOMETER = App.getContext().getString(R.string.pedometer);

}
