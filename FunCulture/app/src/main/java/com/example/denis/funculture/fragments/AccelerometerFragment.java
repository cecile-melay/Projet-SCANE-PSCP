package com.example.denis.funculture.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.denis.funculture.R;
import com.example.denis.funculture.component.sensor.Accelerometer;
import com.example.denis.funculture.main.App;

/**
 * Created by denis on 05/03/2017.
 */

public class AccelerometerFragment extends Fragment {
    LinearLayout contentView;
    Accelerometer accelerometer;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        this.contentView = (LinearLayout) inflater.inflate(R.layout.activity_view, null, false);
        init();

        return this.contentView;
    }

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

    private void init() {
        if(this.accelerometer == null) {
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
}
