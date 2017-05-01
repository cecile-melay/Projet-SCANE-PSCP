package com.example.denis.funculture.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.denis.funculture.R;
import com.example.denis.funculture.utils.MyResources;
import com.example.denis.funculture.utils.MyServices;
import com.example.denis.funculture.utils.Util;

/**
 * Created by Cécile on 10/04/2017.
 */

public class SeConnecter extends MyFragment {
    EditText etPseudo;
    EditText etPass;
    Button btLogin;

    @Override
    protected void init() {
        etPseudo = (EditText) contentView.findViewById(R.id.et_pseudo);
        etPass = (EditText) contentView.findViewById(R.id.et_pass);
        btLogin = (Button) contentView.findViewById(R.id.bt_login);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Util.isEmpty(etPass) || Util.isEmpty(etPseudo)) {
                    Util.createDialog(MyResources.MISSING_FIELD_WARNING);
                }
                else {
                    String pseudo = etPseudo.getText().toString();
                    String pass = etPass.getText().toString();

                    MyServices.getSingleton().loginUser(pseudo, pass);
                }
            }
        });
    }

    @Override
    protected int getLayoutId() { return R.layout.se_connecter;
    }

    @Override
    protected String getTitle() {
        return MyResources.CONNEXION;
    }
}
