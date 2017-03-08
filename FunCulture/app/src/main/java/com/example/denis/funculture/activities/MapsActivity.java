package com.example.denis.funculture.activities;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.example.denis.funculture.R;
import com.example.denis.funculture.component.sensor.Geolocalisation.AlertReceiver;
import com.example.denis.funculture.component.sensor.Geolocalisation.MyLocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
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
    private int intervalGeolocRefresh = 3000;

    private ArrayList<Double[]> zones = new ArrayList<Double[]>();
    private ArrayList<String> nomsZones = new ArrayList<String>();

    private static final int MY_PERMISSIONS_REQUEST_GEOLOCATION_FINE = 0;
    private static final int MY_PERMISSIONS_REQUEST_GEOLOCATION_COARSE = 0;
    private com.google.android.gms.maps.model.PolygonOptions PolygonOptions;

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
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        MapsActivity ma = this;
        // Check Geolocation permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_GEOLOCATION_FINE);
            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_GEOLOCATION_COARSE);
        }
        /* Use the LocationManager class to obtain GPS locations */
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locListener = new MyLocationListener(this, mMap);
        looper = Looper.myLooper();

        //Intent intent = new Intent(this, AlertReceiver.class);
        //PendingIntent pending = PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        zones.add(new Double[]{43.210103, 6.025803}); // Maison Jux
        nomsZones.add("Maison Jux");
        zones.add(new Double[]{43.209415, 6.026087}); // Cimetière
        nomsZones.add("Cimetière");
        zones.add(new Double[]{43.209254, 6.027240}); // Pharmacie
        nomsZones.add("Pharmacie");
        zones.add(new Double[]{43.208314, 6.025117});  // Banc montée
        nomsZones.add("Banc montée");
        zones.add(new Double[]{43.207563, 6.024693});  // Square Leo Lagrange
        nomsZones.add("Sqaure Leo Lagrange");
        zones.add(new Double[]{43.205904, 6.024352});  // Eglise
        nomsZones.add("Eglise");
        zones.add(new Double[]{43.206334, 6.025166});  // Boulangerie
        nomsZones.add("Boulangerie");

        addProximityAlerts(zones, nomsZones);
        //mMap.addPolygon(PolygonOptions);

        // Launch the Geolocation loop with a forced timeout
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locListener, looper);
            }
            else
                return;
            }
        }, 0, intervalGeolocRefresh);

        //mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
        //Location l = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

    }

    private void addProximityAlerts(ArrayList<Double[]> positions, ArrayList<String> name) {
        int requestCode = 0;
        for (int i = 0; i < name.size(); i++) {
            Intent intent = new Intent(this, AlertReceiver.class);
            intent.putExtra("name", name.get(i));
            PendingIntent proximityIntent = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locManager.addProximityAlert(
                        positions.get(i)[0], // the latitude of the central point of the alert region
                        positions.get(i)[1], // the longitude of the central point of the alert region
                        10, // the radius of the central point of the alert region, in meters
                        -1, // time for this proximity alert, in milliseconds, or -1 to     indicate no expiration
                        proximityIntent // will be used to generate an Intent to fire when entry to or exit from the alert region is detected
                );
                requestCode++;
            }
            else
                return;
        }
        IntentFilter filter = new IntentFilter("com.example.denis.funculture.activities");
        registerReceiver(new AlertReceiver(), filter);

    }

    /**
     * Partie Creation d'itineraire a suivre
     * TODO  récupérer les points sur l'arraylist de position de la fonction addProximityAlerts
     * TODO Utiliser les polylines https://developers.google.com/maps/documentation/android-api/shapes?hl=fr pour les reliér
     * Limites : vérifier que ça fonctionne hors routes (normalement oui car ça ne map pas sur un itinéraire
     *
     */
    // Instantiates a new Polyline object and adds points to define a rectangle
    PolylineOptions rectOptions = new PolylineOptions()
            /*
            * Ici on va rentrer la liste des points a relier sur la carte
             */
            .add(new LatLng(37.35, -122.0))
            .add(new LatLng(37.45, -122.0))  // North of the previous point, but at the same longitude
            .add(new LatLng(37.45, -122.2))  // Same latitude, and 30km to the west
            .add(new LatLng(37.35, -122.2))  // Same longitude, and 16km to the south
            .add(new LatLng(37.35, -122.0)); // Closes the polyline.

    // Get back the mutable Polyline
    //Polyline polyline = mMap.addPolyline(rectOptions);



    /*
    * Partie controle de trajectoire
    * Réutiliser la geolocalisation et comparer deux listes de points : celle de la trajectoire de base et celle de l'utilisateur
    * TODO Calculer la distance entre chaque point pour éviter les erreurs gps, vérifier la bonne direction en combinant : distance au point suivant (ligne droite) et angle entre la polylines et le segment tracé par l'utilisateur
     */
    public double calculCoeffiscientDirecteur(double xa, double ya, double xb, double yb ){
        /*
        * Le coeffiscient directeur sera la valeur a comparer pour savoir si un utilisateur fait demi tour
         */
        return (yb-ya)/(xb-xa);
    }

    public double calculDistance(double xa, double ya, double xb, double yb){
        /*
        *On va pouvoir savoir si un utilisateur reste dans la bonne direction en vérifiant que la distance avec le point prochain n'est pas trop grande
         */
        return Math.sqrt(((int)(xa-xb)^2)-((int)(yb-ya)^2));
    }
}
