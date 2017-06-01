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
        public static final String QCM_NO_SELECTED = App.getSingleton().getContext().getString(R.string.qcm_no_selected);
        public static final String QCM_SEE_RESULT = App.getSingleton().getContext().getString(R.string.qcm_see_result);
        public static final String QCM = App.getSingleton().getContext().getString(R.string.qcm);
        public static final String OK = App.getSingleton().getContext().getString(R.string.ok);
        public static final String END_QCM = App.getSingleton().getContext().getString(R.string.end_qcm);
        public static final String YES = App.getSingleton().getContext().getString(R.string.yes);
        public static final String NO = App.getSingleton().getContext().getString(R.string.no);
        public static final String LEAVE_APP = App.getSingleton().getContext().getString(R.string.leave_app);
        public static final String SUCCESS_REGISTER = App.getSingleton().getContext().getString(R.string.success_register);
        public static final String MISSING_FIELD_WARNING = App.getSingleton().getContext().getString(R.string.missing_field_warning);
        public static final String CREATE_PATH = App.getSingleton().getContext().getString(R.string.create_path);
        public static final String CHOOSE_PATH = App.getSingleton().getContext().getString(R.string.choose_path);
        public static final String ADD_POINT = App.getSingleton().getContext().getString(R.string.add_point);
        public static final String QUERIES = App.getSingleton().getContext().getString(R.string.queries);
        public static final String WEBVIEW = App.getSingleton().getContext().getString(R.string.webview);
        public static final String UPDATE_USER = App.getSingleton().getContext().getString(R.string.update_user);
        public static final int MY_PERMISSIONS_REQUEST_GEOLOCATION_FINE = 0;
        public static final int MY_PERMISSIONS_REQUEST_GEOLOCATION_COARSE = 0;
        public static final String CONNEXION = App.getSingleton().getContext().getString(R.string.connection);
        public static final String LOGIN_FAILED = App.getSingleton().getContext().getString(R.string.login_failed);
    public static final String SUCCESS_UPDATE = App.getSingleton().getContext().getString(R.string.success_update_user);
}
