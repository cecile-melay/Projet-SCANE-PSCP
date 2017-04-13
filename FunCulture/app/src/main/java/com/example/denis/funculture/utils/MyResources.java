package com.example.denis.funculture.utils;

import com.example.denis.funculture.R;
import com.example.denis.funculture.main.App;

public class MyResources {
        //Ici un exemple de comment accéder à une string (on fera tous les appels ici afin de simplifier le code)
        public static final String GPS = App.getSingleton().getContext().getString(R.string.gps);
        public static final String ACCELEROMETER = App.getSingleton().getContext().getString(R.string.accelerometer);
        public static final String PEDOMETER = App.getSingleton().getContext().getString(R.string.pedometer);
        public static final String CAMERA = App.getSingleton().getContext().getString(R.string.camera);
        public static final String QRCODE = App.getSingleton().getContext().getString(R.string.qrcode);
        public static final String RECOGNIZE_ACTIVITY = App.getSingleton().getContext().getString(R.string.recognize_activity);
        public static final String CHOOSE_SENSOR = App.getSingleton().getContext().getString(R.string.choose_sensor);
        public static final String STEP_COUNT = App.getSingleton().getContext().getString(R.string.step_count);
        public static final int MY_PERMISSIONS_REQUEST_GEOLOCATION_FINE = 0;
        public static final int MY_PERMISSIONS_REQUEST_GEOLOCATION_COARSE = 0;

}
