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
import com.example.denis.funculture.utils.MyResources;

/**
 * Created by denis on 05/03/2017.
 */

public class PedometerFragment extends MyFragment {
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
    protected int getLayoutId(){
        return R.layout.activity_view;
    }

    @Override
    protected String getTitle(){
        return MyResources.PEDOMETER;
    }

    @Override
    protected void init() {
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
