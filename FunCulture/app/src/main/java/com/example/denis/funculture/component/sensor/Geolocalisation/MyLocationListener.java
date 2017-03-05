package com.example.denis.funculture.component.sensor.Geolocalisation;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.denis.funculture.activities.MapsActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Arrays;
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
    private LatLng myOldPosition;
    private Marker marker;
    private Boolean shoudlIRealyMoveMap = true;

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
        if(compteur==0)
            this.myOldPosition = this.myposition;
        else
        {
            // On test si le déplacement vaut la peine de recentrer la map
            //if (shoudlIRealyMoveMap(this.myposition, this.myOldPosition)) {
                this.myOldPosition = this.myposition;
                Log.e("myLocalisation", String.valueOf(this.myposition));
                // Update in GUI main Thread
                ma.runOnUiThread(new Runnable() {
                    public void run() {
                        mMap.clear();
                        marker = mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(lat, lng))
                                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.my_position))
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                                .title("Me")
                                .snippet("Jux")
                        );
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(myposition)); // On centre la map dynamiquement en fonction du déplacement de l'utilisateur

                        if (compteur < 10)  // On zoom sur l'user
                        {
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(25), 3000, null);
                        }
                    }
                });
            //}
        }
        compteur++;
    }

    public Boolean shoudlIRealyMoveMap(LatLng newPosition, LatLng oldPosition){
        float[] distance = {3};
        android.location.Location.distanceBetween(newPosition.latitude, newPosition.longitude,oldPosition.latitude,oldPosition.longitude,distance);
        //Toast.makeText(context, Arrays.toString(distance), Toast.LENGTH_SHORT).show();
        return distance[0]>=2;

    }

}

