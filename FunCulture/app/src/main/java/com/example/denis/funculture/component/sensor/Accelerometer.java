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

public class Accelerometer implements SensorEventListener{
    private static final String TAG = "Accelerometer";
    private Activity activity;
    private View view;
    Sensor accelerometer;
    SensorManager manager;
    private TextView textAccelerometer1;
    private TextView textAccelerometer2;
    private TextView textAccelerometer3;


    public Accelerometer(Activity activity) {
        this.activity = activity;
        init();
    }

    public View getView() {
        return this.view;
    }

    public void start() {
        manager.registerListener(this, accelerometer, Sensor.REPORTING_MODE_CONTINUOUS);
    }

    public void stop() {
        manager.unregisterListener(this);
    }

    private void init() {
        LayoutInflater inflater = activity.getLayoutInflater();
        this.view = inflater.inflate(R.layout.accelerometer_view, null, false);

        textAccelerometer1 = (TextView) this.view.findViewById(R.id.accelerometer1);
        textAccelerometer2 = (TextView) this.view.findViewById(R.id.accelerometer2);
        textAccelerometer3 = (TextView) this.view.findViewById(R.id.accelerometer3);

        manager = (SensorManager) activity.getSystemService(activity.SENSOR_SERVICE);
        accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d(TAG, "onSensorChanged()");
            float alpha = (float) 0.8;
            float gravity[] = new float[3];
            float linear_acceleration[] = new float[3];
            // alpha is calculated as t / (t + dT)
            // with t, the low-pass filter's time-constant
            // and dT, the event delivery rate


            gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
            gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
            gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

            linear_acceleration[0] = event.values[0] - gravity[0];
            linear_acceleration[1] = event.values[1] - gravity[1];
            linear_acceleration[2] = event.values[2] - gravity[2];

            textAccelerometer1.setText(" 1 : " + linear_acceleration[0]);
            textAccelerometer2.setText(" 2 : " + linear_acceleration[1]);
            textAccelerometer3.setText(" 3 : " + linear_acceleration[2]);
        }
}
