package com.example.denis.testnatif;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.ActivityRecognition;


import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public static MainActivity instance;
    public GoogleApiClient mApiClient;
    public TextView textPedometer, textAccelerometer1, textAccelerometer2, textAccelerometer3, textActivity,
            textVehicule, textBicycle, textOnfoot, textRunning, textWalking, textStill, textTitling;
    public float start = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.instance = this;

        textPedometer = (TextView) findViewById(R.id.pedometer);
        textAccelerometer1 = (TextView) findViewById(R.id.accelerometer1);
        textAccelerometer2 = (TextView) findViewById(R.id.accelerometer2);
        textAccelerometer3 = (TextView) findViewById(R.id.accelerometer3);
        textActivity = (TextView) findViewById(R.id.activity);
        textVehicule = (TextView) findViewById(R.id.vehicule);
        textBicycle = (TextView) findViewById(R.id.bicycle);
        textOnfoot = (TextView) findViewById(R.id.onfoot);
        textRunning = (TextView) findViewById(R.id.running);
        textWalking = (TextView) findViewById(R.id.walking);
        textStill = (TextView) findViewById(R.id.still);
        textTitling = (TextView) findViewById(R.id.titling);

        Sensor pedometer, accelerometer;
        SensorManager manager;

        manager = (SensorManager)this.getSystemService(SENSOR_SERVICE);
        pedometer = manager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        manager.registerListener(this, pedometer, Sensor.REPORTING_MODE_CONTINUOUS);
        manager.registerListener(this, accelerometer, Sensor.REPORTING_MODE_CONTINUOUS);

        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mApiClient.connect();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){}

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if (event.values.length == 1) {
            float current = event.values[0];
            if (start < 0)
                start = event.values[0];

            textPedometer.setText("Nombre de pas : " + Float.toString(current - start));
        }
        else if(event.values.length == 3)
        {
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

    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        textActivity.setText("Service connecté");
        Intent intent = new Intent( this, ActivityRecognizedService.class );
        PendingIntent pendingIntent = PendingIntent.getService( this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT );
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates( mApiClient, 0, pendingIntent );
    }

    @Override
    public void onConnectionSuspended(int i)
    {
        textActivity.setText("Service suspendu");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {
        textActivity.setText("Service injoignable");
    }

    public Dialog createDialog(String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.instance);
        builder.setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        return builder.create();
    }
}
