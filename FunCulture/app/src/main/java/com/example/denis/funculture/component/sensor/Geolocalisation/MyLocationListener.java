package com.example.denis.funculture.component.sensor.Geolocalisation;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.denis.funculture.R;
import com.example.denis.funculture.activities.MapsActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Timer;

/**
 * Created by jux on 22/02/2017.
 */


/*
 * Class to manage geolocation loop
 */
public class MyLocationListener implements android.location.LocationListener {

    private Context context;
    public static MapsActivity MA;
    private GoogleMap mMap;
    private int compteur = 0;
    private Timer timer = new Timer();
    private LatLng myposition;
    private LatLng myOldPosition;
    private Marker marker;
    private Boolean shoudlIRealyMoveMap = true;

    public MyLocationListener(MapsActivity ma, GoogleMap gm)
    {
        this.MA = ma;
        this.context = this.MA.getApplicationContext();
        this.mMap = gm;
    }

    @Override
    public void onLocationChanged(final Location loc)
    {
        /*Toast.makeText( this.context,
                "Moved",
                Toast.LENGTH_SHORT).show();
        Toast.makeText( this.context,
                String.valueOf(loc.getAccuracy())+"m d'imprécision GPS",
                Toast.LENGTH_SHORT ).show();*/

        if(loc.getAccuracy()>10) {
            //mMap.setMyLocationEnabled(false);
            Toast.makeText( this.context,
                    "Bad GPS signal",
                    Toast.LENGTH_SHORT).show();
            //return;
        }

        if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            //mMap.setMyLocationEnabled(true);

        this.myposition = new LatLng(loc.getLatitude(),loc.getLongitude());
        this.myOldPosition = this.myposition;
        Log.e("myLocalisation", String.valueOf(this.myposition));

        /*if(compteur==0) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myposition));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(25), 2000, null);
        }
        else
            MA.getTabMarkers().get(MA.getTabMarkers().size()-1).remove();
        // On test si le déplacement vaut la peine de recentrer la map
        //if (shoudlIRealyMoveMap(this.myposition, this.myOldPosition)) {
        // Update in GUI main Thread
        MA.runOnUiThread(new Runnable() {
            public void run() {
                marker = mMap.addMarker(new MarkerOptions()
                        .position(myposition)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.me))
                        //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                        .title("Me")
                        .snippet("Jux")
                );
                MA.getTabMarkers().add(marker);
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(myposition)); // On centre la map dynamiquement en fonction du déplacement de l'utilisateur
                //if (compteur < 10)  // On zoom sur l'user
                //{
                  //mMap.animateCamera(CameraUpdateFactory.zoomTo(20), 2000, null);
                //}
            }
        });*/
        amINearToPointOfInterest(myposition);
        //}
        compteur++;
    }


    @Override
    public void onProviderDisabled(String provider)
    {
        Toast.makeText( this.context,
                "Gps Disabled",
                Toast.LENGTH_SHORT ).show();
    }

    @Override
    public void onProviderEnabled(String provider)
    {
        Toast.makeText( this.context,
                "Gps Enabled. Location in progress ...",
                Toast.LENGTH_SHORT).show();
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(21), 2000, null);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
        /*String newStatus = "";
        switch (status) {
            case LocationProvider.OUT_OF_SERVICE:
                newStatus = "OUT_OF_SERVICE";
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                newStatus = "TEMPORARILY_UNAVAILABLE";
                break;
            case LocationProvider.AVAILABLE:
                newStatus = "AVAILABLE";
                break;
        }
        Toast.makeText(this.context, provider+" "+newStatus, Toast.LENGTH_SHORT).show();*/
    }

    public Boolean shoudlIRealyMoveMap(LatLng newPosition, LatLng oldPosition) {
        float[] distance = {3};
        android.location.Location.distanceBetween(newPosition.latitude, newPosition.longitude,oldPosition.latitude,oldPosition.longitude,distance);
        //Toast.makeText(context, Arrays.toString(distance), Toast.LENGTH_SHORT).show();
        return distance[0]>=2;

    }

    public void amINearToPointOfInterest(LatLng myposition)
    {
        float[] distanceBetween = {3};
        for (int i=0 ; i<MapsActivity.zones.size() ; i++)
        {
            android.location.Location.distanceBetween(myposition.latitude, myposition.longitude, MapsActivity.zones.get(i)[0], MapsActivity.zones.get(i)[1], distanceBetween);
            if(distanceBetween[0]<=10)
            {
                Toast.makeText( this.context,
                        distanceBetween[0]+"m de "+MapsActivity.nomsZones.get(i),
                        Toast.LENGTH_SHORT ).show();
            }

        }


    }

}

