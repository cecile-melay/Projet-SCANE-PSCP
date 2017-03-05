package com.example.denis.funculture.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.denis.funculture.R;
import com.example.denis.funculture.component.sensor.Accelerometer;

/**
 * Created by denis on 05/03/2017.
 */

public class AccelerometerActivity extends Activity{
    LinearLayout contentView;
    Accelerometer accelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.contentView = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_view, null, false);
        setContentView(this.contentView);

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        if(this.accelerometer == null) {
            this.accelerometer = new Accelerometer(this);
            this.contentView.addView(accelerometer.getView());
        }

        this.accelerometer.start();
    }

    @Override
    protected void onPause() {
        this.accelerometer.stop();
        super.onPause();
    }
}
