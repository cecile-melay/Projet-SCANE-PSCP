package com.example.denis.funculture.fragments;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.denis.funculture.R;
import com.example.denis.funculture.main.MainActivity;
import com.example.denis.funculture.utils.MyResources;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by denis on 19/03/2017.
 */

public class ChooseSensorFragment extends Fragment implements View.OnClickListener {
    private View contentView;
    private Spinner senssorSpinner;
    private Button testButton;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        this.contentView = inflater.inflate(R.layout.choose_sensor_fragment, null);
        this.senssorSpinner = (Spinner) contentView.findViewById(R.id.sensorSpinner);
        this.testButton = (Button) contentView.findViewById(R.id.testButton);
        this.testButton.setOnClickListener(this);
        fillSensorSpinner();

        return this.contentView;
    }

    //ici on ajoute les capteurs au spinner
    private void fillSensorSpinner() {
        List<String> spinnerArray =  new ArrayList<>();
        spinnerArray.add(MyResources.GPS);
        spinnerArray.add(MyResources.ACCELEROMETER);
        spinnerArray.add(MyResources.PEDOMETER);
        spinnerArray.add(MyResources.RECOGNIZE_ACTIVITY);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_spinner_item, spinnerArray);
        this.senssorSpinner.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if(v == this.testButton) {
            if(senssorSpinner != null && senssorSpinner.getSelectedItem() != null) {
                int position = senssorSpinner.getSelectedItemPosition();
                Intent intent;

                switch (position) {
                    //GPS
                    case 0 :
                        ((MainActivity) getActivity()).startFragment(MapsFragment.class);
                        break;

                    //ACCELEROMETER
                    case 1 :
                        ((MainActivity) getActivity()).startFragment(AccelerometerFragment.class);
                        break;

                    //PEDOMETER
                    case 2 :
                        ((MainActivity) getActivity()).startFragment(PedometerFragment.class);
                        break;

                    //ACTIVITY SERVICE
                    case 3 :
                        ((MainActivity) getActivity()).startFragment(RecognitionActivityFragment.class);
                        break;
                }
            }
        }
    }
}