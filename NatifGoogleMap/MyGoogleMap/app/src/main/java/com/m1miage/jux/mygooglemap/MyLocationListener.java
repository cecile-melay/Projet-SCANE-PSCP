package com.m1miage.jux.mygooglemap;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import android.location.Location;
import android.widget.Toast;
import android.os.Bundle;
import android.content.Context;
import android.util.Log;

import java.util.Timer;
/**
 * Created by jux on 22/02/2017.
 */


/*
 * Class to manage geolocation loop
 */
public class MyLocationListener implements android.location.LocationListener {

    private Context context;
    private MapsActivity ma;
    private GoogleMap mMap;
    private int compteur = 0;
    private Timer timer = new Timer();
    private LatLng myposition;
    private Marker marker;

    public MyLocationListener(MapsActivity ma, GoogleMap gm)
    {
        this.ma = ma;
        this.context = this.ma.getApplicationContext();
        this.mMap = gm;
    }

    @Override
    public void onLocationChanged(final Location loc)
    {
        updateMyPositionOnMap(loc.getLatitude(),loc.getLongitude());
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
                "Gps Enabled",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
    }

    public void updateMyPositionOnMap(final Double lat,final Double lng)
    {
        this.myposition = new LatLng(lat,lng);
        Log.e("myLocalisation",String.valueOf(this.myposition));
        // Update in GUI main Thread
        ma.runOnUiThread(new Runnable() {
            public void run() {
                //.icon(BitmapDescriptorFactory.fromAsset("ez.png");
                //MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(lat, lng)).title("Me");

                mMap.clear();
                marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(lat, lng))
                        //.icon(BitmapDescriptorFactory.fromResource(R.drawable.my_position))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                        .title("Me")
                        .snippet("Jux")
                );
                mMap.moveCamera(CameraUpdateFactory.newLatLng(myposition));

                //mMap.addMarker(new MarkerOptions().position(myposition).title("Jux Le Terrible"));
                if(compteur<2)
                {
                    //mMap.moveCamera(CameraUpdateFactory.newLatLng(myposition));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(25), 3000, null);
                }
            }
        });

    }



}
