package com.example.denis.funculture.fragments;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.denis.funculture.R;
import com.example.denis.funculture.utils.MyResources;
import com.example.denis.funculture.utils.MyServices;
import com.example.denis.funculture.utils.Util;

import java.util.ArrayList;

/**
 * Created by denis on 02/04/2017.
 */

public class Accueil extends MyFragment {
    Button btStart;

    @Override
    protected int getLayoutId() { return R.layout.accueil;
    }

    @Override
    protected String getTitle() {
        return MyResources.WELCOME;
    }

    @Override
    protected void init() {
        btStart = (Button) contentView.findViewById(R.id.bt_start);
        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LinearLayout layout = new LinearLayout(getActivity());
                layout.setOrientation(LinearLayout.VERTICAL);

                ArrayList<String> spinnerArray = new ArrayList<>();

                for (int i = 0; i < Util.getPaths().size(); i++) {
                    spinnerArray.add(Util.getPaths().get(i).getName());
                }

                final Spinner spinner = new Spinner(getActivity());
                ArrayAdapter<String> spinnerArrayAdapter =
                        new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_dropdown_item,
                                spinnerArray);
                spinner.setAdapter(spinnerArrayAdapter);
                layout.addView(spinner);

                builder.setView(layout);
                builder.setMessage(MyResources.CHOOSE_PATH)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                int pathId =
                                        Util.getPaths().get(spinner.getSelectedItemPosition()).getId();
                                MyServices.getSingleton().loadPath(pathId, true);
                            }
                        });
                builder.show();
            }
        });
    }
}
