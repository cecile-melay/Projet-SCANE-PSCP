package com.example.denis.funculture.component.sensor;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.denis.funculture.R;

/**
 * Created by denis on 05/03/2017.
 */

public class Pedometer implements SensorEventListener {
    private static final String TAG = "Pedometer";
    private Activity activity;
    private View view;
    Sensor pedometer;
    SensorManager manager;
    private TextView textPedometer;
    private float startCount = -1;


    public Pedometer(Activity activity) {
        this.activity = activity;
        init();
    }

    public View getView() {
        return this.view;
    }

    public void start() {
        manager.registerListener(this, pedometer, Sensor.REPORTING_MODE_CONTINUOUS);
    }

    public void stop() {
        manager.unregisterListener(this);
        this.startCount = -1;
    }

    private void init() {
        LayoutInflater inflater = activity.getLayoutInflater();
        this.view = inflater.inflate(R.layout.pedometer_view, null, false);

        textPedometer = (TextView) this.view.findViewById(R.id.pedometer);

        manager = (SensorManager) activity.getSystemService(activity.SENSOR_SERVICE);
        pedometer = manager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d(TAG, "onSensorChanged()");
        float current = event.values[0];
        if (startCount < 0) {
            startCount = event.values[0];
        }
        float count = current - startCount;

        textPedometer.setText("Nombre de pas : " + Float.toString(count));
        Log.d(TAG, "start : " + startCount + " current : " + current + " diff : " + count);
    }
}
