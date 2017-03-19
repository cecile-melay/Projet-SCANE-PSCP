package com.example.denis.funculture.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.denis.funculture.R;
import com.example.denis.funculture.component.sensor.Accelerometer;
import com.example.denis.funculture.component.sensor.Pedometer;
import com.example.denis.funculture.main.App;

/**
 * Created by denis on 05/03/2017.
 */

public class PedometerActivity extends Activity {
    LinearLayout contentView;
    Pedometer pedometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getSingleton().setCurrentActivity(this);
        this.contentView = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_view, null, false);
        setContentView(this.contentView);

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //init();
    }

    private void init() {
        if(this.pedometer == null) {
            this.pedometer = new Pedometer(this);
            this.contentView.addView(pedometer.getView());
        }

        this.pedometer.start();
    }

    @Override
    protected void onPause() {
        //this.pedometer.stop();
        super.onPause();
    }
}
