package com.example.denis.funculture.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.denis.funculture.R;
import com.example.denis.funculture.component.sensor.Pedometer;
import com.example.denis.funculture.main.App;

/**
 * Created by denis on 05/03/2017.
 */

public class PedometerFragment extends Fragment {
    LinearLayout contentView;
    Pedometer pedometer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        App.getSingleton().setCurrentFragment(this);
        //init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        this.contentView = (LinearLayout) inflater.inflate(R.layout.activity_view, null);
        init();
        return this.contentView;
    }

    private void init() {
        if(this.pedometer == null) {
            this.pedometer = new Pedometer(getActivity());
            this.contentView.addView(pedometer.getView());
        }

        this.pedometer.start();
    }

    @Override
    public void onPause() {
        //this.pedometer.stop();
        super.onPause();
    }
}
