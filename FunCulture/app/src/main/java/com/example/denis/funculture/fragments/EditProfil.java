package com.example.denis.funculture.fragments;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.denis.funculture.R;
import com.example.denis.funculture.component.User;
import com.example.denis.funculture.utils.MyResources;
import com.example.denis.funculture.utils.MyServices;
import com.example.denis.funculture.utils.Util;

/**
 * Created by Cécile on 10/04/2017.
 */

public class EditProfil extends MyFragment {
    //Register Fields
    private EditText etSecondName;
    private EditText etFirstName;
    private EditText etBirth;
    private EditText etVille;
    private EditText etMail;
    private EditText etPass;
    private EditText etPseudo;
    private EditText etFc;
    private TextView tvXp;
    private Spinner spLevel;
    private Button btRegister;
    private Button btCancel;

    @Override
    protected void init() {
        this.etFirstName = (EditText) contentView.findViewById(R.id.et_first_name);
        this.etSecondName = (EditText) contentView.findViewById(R.id.et_second_name);
        this.etBirth = (EditText) contentView.findViewById(R.id.et_birth);
        this.etVille = (EditText) contentView.findViewById(R.id.et_ville);
        this.etMail = (EditText) contentView.findViewById(R.id.et_mail);
        this.etPass = (EditText) contentView.findViewById(R.id.et_pass);
        this.etPseudo = (EditText) contentView.findViewById(R.id.et_pseudo);
        this.etFc = (EditText) contentView.findViewById(R.id.et_fc);
        this.tvXp = (TextView) contentView.findViewById(R.id.tv_xp);
        this.spLevel = (Spinner) contentView.findViewById(R.id.sp_level);
        this.btRegister = (Button) contentView.findViewById(R.id.bt_register);
        this.btCancel = (Button) contentView.findViewById(R.id.bt_cancel);

        etFirstName.setText(Util.getCurrentUser().getPrenom());
        etSecondName.setText(Util.getCurrentUser().getNom());
        etBirth.setText(Util.getCurrentUser().getDateNaiss());
        etVille.setText(Util.getCurrentUser().getVille());
        etMail.setText(Util.getCurrentUser().getMail());
        etPass.setText(Util.getCurrentUser().getPass());
        etPseudo.setText(Util.getCurrentUser().getPseudo());
        etFc.setText(Integer.toString(Util.getCurrentUser().getFc()));
        tvXp.setText(Integer.toString(Util.getCurrentUser().getXp()));

        this.btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateChanges();
            }
        });

        this.btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(false);
            }
        });

        String[] sportLevels = new String[]{"Niveau sportif", "Débutant", "Intermédiaire", "Confirmé"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, sportLevels);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item_drop);
        this.spLevel.setAdapter(spinnerArrayAdapter);
        this.spLevel.setSelection(Util.getCurrentUser().getLvlSport());
    }

    private void validateChanges() {
        if (Util.isEmpty(etFirstName)
                || Util.isEmpty(etSecondName)
                || Util.isEmpty(etBirth)
                || Util.isEmpty(etMail)
                || Util.isEmpty(etVille)
                || Util.isEmpty(etPass)
                || Util.isEmpty(etFc)
                || Util.isEmpty(etPseudo)
                || spLevel.getSelectedItemPosition() == 0) {
            Util.createDialog(MyResources.MISSING_FIELD_WARNING);
        } else {
            int id = Util.getCurrentUser().getId();
            Util.setCurrentUser(new User(id, etFirstName.getText().toString(),
                    etSecondName.getText().toString(),
                    etBirth.getText().toString(),
                    spLevel.getSelectedItemPosition(),
                    Integer.parseInt(etFc.getText().toString()),
                    etVille.getText().toString(),
                    etMail.getText().toString(),
                    etPass.getText().toString(),
                    etPseudo.getText().toString()));

            MyServices.getSingleton().updateUser(Util.getCurrentUser());
            Util.createDialog(MyResources.SUCCESS_UPDATE);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.edit_profil;
    }

    @Override
    protected String getTitle() {
        return MyResources.CONNEXION;
    }
}
