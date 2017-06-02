package com.example.denis.funculture.fragments;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.denis.funculture.R;
import com.example.denis.funculture.utils.MyResources;
import com.example.denis.funculture.utils.MyServices;
import com.example.denis.funculture.utils.Util;

/**
 * Created by Cécile on 10/04/2017.
 */

public class SeConnecter extends MyFragment {
    TextView tvConnected;
    LinearLayout llConnection;
    EditText etPseudo;
    EditText etPass;
    Button btLogin;
    Button btRegister;

    @Override
    protected void init() {
        etPseudo = (EditText) contentView.findViewById(R.id.et_pseudo);
        etPass = (EditText) contentView.findViewById(R.id.et_pass);
        tvConnected = (TextView) contentView.findViewById(R.id.tv_connected);
        llConnection = (LinearLayout) contentView.findViewById(R.id.ll_connection);
        btLogin = (Button) contentView.findViewById(R.id.bt_login);
        btRegister = (Button) contentView.findViewById(R.id.bt_register);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Util.isEmpty(etPass) || Util.isEmpty(etPseudo)) {
                    Util.createDialog(MyResources.MISSING_FIELD_WARNING);
                }
                else {
                    String pseudo = etPseudo.getText().toString();
                    String pass = etPass.getText().toString();

                    MyServices.getSingleton().loginUser(pseudo, pass, true);
                }
            }
        });

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.getMainActivity().showRegisterLayout();
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

    @Override
    public void onResume() {
        super.onResume();
        checkUserConnected();
    }

    public void checkUserConnected() {
        if(Util.getCurrentUser() == null) {
            llConnection.setVisibility(View.VISIBLE);
            tvConnected.setVisibility(View.GONE);
        } else {
            llConnection.setVisibility(View.GONE);
            tvConnected.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void finish(boolean restoreMap) {
        Util.getMainActivity().removeFragment(this);
        super.finish(restoreMap);
    }
}
