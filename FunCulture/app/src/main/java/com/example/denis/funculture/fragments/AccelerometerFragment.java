package com.example.denis.funculture.fragments;

import android.os.Bundle;

import com.example.denis.funculture.R;
import com.example.denis.funculture.component.sensor.Accelerometer;
import com.example.denis.funculture.main.App;
import com.example.denis.funculture.utils.MyResources;

/**
 * Created by denis on 05/03/2017.
 */

public class AccelerometerFragment extends MyFragment {
    Accelerometer accelerometer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        App.getSingleton().setCurrentFragment(this);
        init();
    }

    @Override
    protected void init() {
        if (this.accelerometer == null) {
            this.accelerometer = new Accelerometer(getActivity());
            this.contentView.addView(accelerometer.getView());
        }

        this.accelerometer.start();
    }

    @Override
    public void onPause() {
        this.accelerometer.stop();
        super.onPause();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_view;
    }

    @Override
    protected String getTitle(){
        return MyResources.ACCELEROMETER;
    }
}
