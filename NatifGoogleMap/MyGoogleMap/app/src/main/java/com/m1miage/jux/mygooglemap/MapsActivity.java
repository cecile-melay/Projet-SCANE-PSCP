package com.m1miage.jux.mygooglemap;

import android.Manifest;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.test.espresso.core.deps.guava.collect.Lists;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.location.LocationListener;
import android.location.LocationManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.content.Intent;
import android.app.PendingIntent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/*
 * Main Activity which contains the Google Map
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Timer timer = new Timer();
    private LocationManager locManager;
    private LocationListener locListener;
    private Looper looper;
    private int intervalGeolocRefresh = 2000;

    private ArrayList<Double[]> zones = new ArrayList<Double[]>();
    private ArrayList<String> nomsZones = new ArrayList<String>();

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

        //Intent intent = new Intent(this, AlertReceiver.class);
        //PendingIntent pending = PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        //locManager.addProximityAlert(43.210103, 6.025803, 200, -1, pending);

        zones.add(new Double[]{43.210103,6.025803}); // Maison Jux
        nomsZones.add("Maison Jux");
        zones.add(new Double[]{43.209415,6.026087}); // Cimetière
        nomsZones.add("Cimetière");
        zones.add(new Double[]{43.209254,6.027240}); // Pharmacie
        nomsZones.add("Pharmacie");
        zones.add(new Double[]{43.208314,6.025117});  // Banc montée
        nomsZones.add("Banc montée");
        zones.add(new Double[]{43.207563,6.024693});  // Square Leo Lagrange
        nomsZones.add("Sqaure Leo Lagrange");
        zones.add(new Double[]{43.205904,6.024352});  // Eglise
        nomsZones.add("Eglise");
        zones.add(new Double[]{43.206334,6.025166});  // Boulangerie
        nomsZones.add("Boulangerie");

        addProximityAlerts(zones, nomsZones);

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

    private void addProximityAlerts(ArrayList<Double[]> positions, ArrayList<String> name) {

        int requestCode = 0;
        for(int i=0 ; i<name.size() ; i++)
        {
            //Bundle extras = new Bundle();
            //extras.putString("name", name.get(i));
            //extras.putInt("id", requestCode);
            Intent intent = new Intent(this, AlertReceiver.class);
            //Intent intent = new Intent("com.m1miage.jux.mygooglemap");
            intent.putExtra("name", name.get(i));
            PendingIntent proximityIntent = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            locManager.addProximityAlert(
                    positions.get(i)[0], // the latitude of the central point of the alert region
                    positions.get(i)[1], // the longitude of the central point of the alert region
                    10, // the radius of the central point of the alert region, in meters
                    -1, // time for this proximity alert, in milliseconds, or -1 to     indicate no expiration
                    proximityIntent // will be used to generate an Intent to fire when entry to or exit from the alert region is detected
            );
            requestCode++;
        }
        IntentFilter filter = new IntentFilter("com.m1miage.jux.mygooglemap");
        registerReceiver(new AlertReceiver(), filter);

        /*
        //Intent intent = new Intent("com.m1miage.jux.mygooglemap");
        Intent intent = new Intent(this, AlertReceiver.class);
        //intent.setAction("com.m1miage.jux.mygooglemap");
        PendingIntent proximityIntent = PendingIntent.getBroadcast(this, 2, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        locManager.addProximityAlert(
                43.210103, // the latitude of the central point of the alert region
                6.025803, // the longitude of the central point of the alert region
                7, // the radius of the central point of the alert region, in meters
                -1, // time for this proximity alert, in milliseconds, or -1 to     indicate no expiration
                proximityIntent // will be used to generate an Intent to fire when entry to or exit from the alert region is detected
        );

        IntentFilter filter = new IntentFilter("com.m1miage.jux.mygooglemap");
        registerReceiver(new AlertReceiver(), filter);
        //Toast.makeText(getApplicationContext(),"Alert Added",Toast.LENGTH_SHORT).show();
        */
    }

}
