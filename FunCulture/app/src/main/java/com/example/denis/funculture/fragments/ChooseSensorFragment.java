package com.example.denis.funculture.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.denis.funculture.R;
import com.example.denis.funculture.activities.AppareilPhoto;
import com.example.denis.funculture.activities.QRCodeScanner;
import com.example.denis.funculture.component.localisation.Path;
import com.example.denis.funculture.main.MainActivity;
import com.example.denis.funculture.utils.MyResources;
import com.example.denis.funculture.utils.MyServices;
import com.example.denis.funculture.utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by denis on 19/03/2017.
 */

public class ChooseSensorFragment extends MyFragment implements View.OnClickListener {
    private Spinner senssorSpinner;
    private Button testButton;

    @Override
    protected int getLayoutId() {
        return R.layout.choose_sensor_fragment;
    }

    @Override
    protected String getTitle() {
        return MyResources.CHOOSE_SENSOR;
    }

    @Override
    protected void init() {
        super.init();
        this.senssorSpinner = (Spinner) contentView.findViewById(R.id.sensorSpinner);
        this.testButton = (Button) contentView.findViewById(R.id.testButton);
        this.testButton.setOnClickListener(this);
        fillSensorSpinner();
    }

    //ici on ajoute les capteurs au spinner
    private void fillSensorSpinner() {
        List<String> spinnerArray = new ArrayList<>();
        spinnerArray.add(MyResources.GPS);
        spinnerArray.add(MyResources.ACCELEROMETER);
        spinnerArray.add(MyResources.PEDOMETER);
        spinnerArray.add(MyResources.RECOGNIZE_ACTIVITY);
        spinnerArray.add(MyResources.CAMERA);
        spinnerArray.add(MyResources.QRCODE);
        spinnerArray.add(MyResources.QCM);
        spinnerArray.add(MyResources.QUERIES);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_spinner_item, spinnerArray);
        this.senssorSpinner.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (v == this.testButton) {
            if (senssorSpinner != null && senssorSpinner.getSelectedItem() != null) {
                int position = senssorSpinner.getSelectedItemPosition();
                Intent intent;

                switch (position) {
                    //GPS
                    case 0:
                        //TODO : Show list with existing paths
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
                        break;

                    //ACCELEROMETER
                    case 1:
                        ((MainActivity) getActivity()).startFragment(AccelerometerFragment.class);
                        break;

                    //PEDOMETER
                    case 2:
                        ((MainActivity) getActivity()).startFragment(PedometerFragment.class);
                        break;

                    //ACTIVITY SERVICE
                    case 3:
                        ((MainActivity) getActivity()).startFragment(RecognitionActivityFragment.class);
                        break;

                    //Appareil Photo
                    case 4:
                        Intent cameraIntent = new Intent(getActivity(), AppareilPhoto.class);
                        startActivity(cameraIntent);
                        break;

                    //QR Code Scanner
                    case 5:
                        Intent qrcs = new Intent(getActivity(), QRCodeScanner.class);
                        startActivity(qrcs);
                        break;

                    //QCM
                    case 6:
                        ((MainActivity) getActivity()).startFragment(QCMFragment.class);
                        break;

                    //Queries
                    case 7:
                        ((MainActivity) getActivity()).startFragment(QueriesFragment.class);
                        break;
                }
            }
        }
    }
}
