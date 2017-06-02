package com.example.denis.funculture.utils;

/**
 * Created by remi on 02/06/2017.
 */

public class LoginModel {
    private static final String PREF_PSEUDO = "PREF_PSEUDO";
    private static final String PREF_PASS = "PREF_PASS";
    private static LoginModel instance;

    public static LoginModel getInstance() {
        if(instance == null) {
            instance = new LoginModel();
        }

        return instance;
    }

    public void saveConnexion(String pseudo, String pass) {
        Util.writeInPrefs(PREF_PSEUDO, pseudo);
        Util.writeInPrefs(PREF_PASS, pass);
    }

    public void checkConnexion() {
        String pseudo = Util.readFromPrefs(PREF_PSEUDO);
        String pass = Util.readFromPrefs(PREF_PASS);

        if(pseudo == null || pass == null) {
            return;
        }

        MyServices.getSingleton().loginUser(pseudo, pass, false);
    }
}
