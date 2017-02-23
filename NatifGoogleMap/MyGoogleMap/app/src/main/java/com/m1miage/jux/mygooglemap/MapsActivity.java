package com.m1miage.jux.mygooglemap;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.location.LocationListener;
import android.location.LocationManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


/*
 * Main Activity which contains the Google Map
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Timer timer = new Timer();
    private static LocationManager locManager;
    private static LocationListener locListener;
    private static Looper looper;
    private final int intervalGeolocRefresh = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        MapsActivity ma = this;
        // Check Geolocation permission /!\ NOT RELIABLE MAYBE MUST HAVE TO ALLOW IN APP MANAGER THE GEOLOC PERMISSION FOR THIS APP /!\
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        /* Use the LocationManager class to obtain GPS locations */
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locListener = new MyLocationListener(this, mMap);
        looper = Looper.myLooper();

        Toast.makeText( this,
                "Geolocation in progress ...",
                Toast.LENGTH_SHORT).show();
        // Launch the Geolocation loop with a forced timeout
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                locManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locListener, looper);
        }
        }, 0, intervalGeolocRefresh);

        //mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
        //Location l = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);




    }

}
