package com.m1miage.jux.mygooglemap;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Location;
import android.widget.Toast;
import android.os.Bundle;
import android.content.Context;
import android.util.Log;
/**
 * Created by jux on 22/02/2017.
 */


/*
 * Class to manage geolocation loop
 */
public class MyLocationListener implements android.location.LocationListener {

    private Context context;
    private GoogleMap mMap;
    private int compteur = 0;

    public MyLocationListener(Context c, GoogleMap gm)
    {
        this.context = c;
        this.mMap = gm;
    }

    @Override
    public void onLocationChanged(Location loc)
    {
        updateMyPositionOnMap(loc.getLatitude(),loc.getLongitude());
        this.compteur++;
        /*String Text = String.valueOf(loc.getLatitude()+" -- "+ loc.getLongitude());
        Toast.makeText(this.context,
                Text,
                Toast.LENGTH_SHORT).show();*/
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

    public void updateMyPositionOnMap(Double lat,Double lng)
    {
        LatLng me = new LatLng(lat,lng);
        Log.e("myLocalisation",String.valueOf(me));
        mMap.addMarker(new MarkerOptions().position(me).title("Jux Le Terrible"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(me));
        if(compteur==0)
        {
            mMap.animateCamera(CameraUpdateFactory.zoomTo(20), 1000, null);
        }
    }

}
