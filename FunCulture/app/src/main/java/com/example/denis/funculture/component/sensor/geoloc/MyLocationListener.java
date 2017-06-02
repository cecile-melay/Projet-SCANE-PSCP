package com.example.denis.funculture.component.sensor.geoloc;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.denis.funculture.fragments.MapsFragment;
import com.example.denis.funculture.utils.Util;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Timer;

/**
 * Created by jux on 22/02/2017.
 */


/*
 * Class to manage geolocation loop
 */
public class MyLocationListener implements android.location.LocationListener {

    private Context context;
    public static MapsFragment MA;
    private GoogleMap mMap;
    private int compteur = 0;
    private Timer timer = new Timer();
    private LatLng myposition;
    private LatLng myOldPosition;
    private Marker marker;
    private Boolean shoudlIRealyMoveMap = true;
    private Boolean trackingMode = false;
    private Boolean followMode = false;

    private LatLng myPreviousInterestPoint;
    private LatLng myCurrentInterestPoint;

    private LatLng myPreviousPoint;
    private LatLng myCurrentPoint;





    public MyLocationListener(MapsFragment ma, GoogleMap gm)
    {
        this.MA = ma;
       // this.MA.addStructChemin(myposition);
        this.context = this.MA.getActivity().getApplicationContext();
        this.mMap = gm;
        //this.myPreviousInterestPoint = new LatLng(MapsFragment.zones.get(0)[0], MapsFragment.zones.get(0)[1]);
    }

    @Override
    public void onLocationChanged(final Location loc)
    {

        ;
        /*Toast.makeText( this.context,
                "Moved",
                Toast.LENGTH_SHORT).show();
        Toast.makeText( this.context,
                String.valueOf(loc.getAccuracy())+"m d'imprécision GPS",
                Toast.LENGTH_SHORT ).show();*/

        if(loc.getAccuracy()>10) {
            //Toast.makeText( this.context,
                    //"Bad GPS signal",
                    //Toast.LENGTH_SHORT).show();
            //return;

        }

        if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            //mMap.setMyLocationEnabled(true);
        this.myposition = new LatLng(loc.getLatitude(),loc.getLongitude());
        this.myOldPosition = this.myposition;
        Log.d("myLocalisation", String.valueOf(this.myposition));

        if(Util.centerMapOnUser()) {
            float zoomLevel = (float) 16.0; //This goes up to 21
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myposition, zoomLevel));
        }
        /*
        * Controle de position hors du mode tracking
         */
        if (!trackingMode) {
            /*
            * ON se place dans le cas ou on va forcément suivre un chemin
             */

            canIChangeStep(myposition);

            /*if (!followMode  && myposition!= null && LocationControle.calculDistance(this.MA.way.get(0), myposition) >= 0.00000001){
                Toast.makeText(this.context,
                        "Go to "+this.MA.way.get(0).toString(),
                        Toast.LENGTH_SHORT).show();

            }*/
            /*followMode = true;*/

            /*Toast.makeText(this.context,
                    "Follow mode on",
                    Toast.LENGTH_SHORT).show();
            int step = 1;*/
            /*while (step < this.MA.way.size()){
                if (loc.getAccuracy()<5){
                    if (LocationControle.calculDistance(this.MA.way.get(step),myposition)> LocationControle.calculDistance(this.MA.way.get(step),myOldPosition)) {
                        Toast.makeText(this.context,
                                "Wrong direction ",
                                Toast.LENGTH_SHORT).show();
                    }else if (LocationControle.calculDistance(this.MA.way.get(step+1),myposition)< LocationControle.calculDistance(this.MA.way.get(step-1),myposition)){
                        step++;
                        Toast.makeText(this.context,
                                "Udpate step",
                                Toast.LENGTH_SHORT).show();
                    }
                }

            }*/

        }



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
//        Toast.makeText( this.context,
//                "Gps Disabled",
//                Toast.LENGTH_SHORT ).show();
    }

    @Override
    public void onProviderEnabled(String provider)
    {
//        Toast.makeText( this.context,
//                "Gps Enabled. Location in progress ...",
//                Toast.LENGTH_SHORT).show();
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

    public void canIChangeStep(LatLng myposition){
        if(this.MA.way == null || this.MA.way.size() == 0) {
            return;
        }

        float[] distanceBetween = {3};
            Toast.makeText( this.context,
                    distanceBetween[0]+"m de départ : point  "+ this.MA.way.get(0),
                    Toast.LENGTH_SHORT ).show();



        for (int i = 0; i< this.MA.way.size() ; i++)
        {
            android.location.Location.distanceBetween(myposition.latitude, myposition.longitude, this.MA.way.get(i).latitude, this.MA.way.get(i).longitude, distanceBetween);
            if(i==0 && distanceBetween[0]>3){
                Toast.makeText( this.context,
                        distanceBetween[0]+"m de départ : point  "+ this.MA.way.get(i),
                        Toast.LENGTH_SHORT ).show();


            }
            else if(distanceBetween[0]<=3)
            {
                Toast.makeText( this.context,
                        distanceBetween[0]+"m du point numéro"+i+" "+this.MA.way.get(i),
                        Toast.LENGTH_SHORT ).show();
                this.myPreviousPoint = new LatLng(this.MA.way.get(i - 1).latitude, this.MA.way.get(i - 1).longitude);
                this.myCurrentPoint = new LatLng(this.MA.way.get(i).latitude, this.MA.way.get(i).longitude);
                changePolylineColor(this.myPreviousPoint, this.myCurrentPoint);

                /*
                *
                * Rajouter le cas ou l'utilisateur s'approche d'un autre point
                 */



            }
        }
    }

    public void amINearToPointOfInterest(LatLng myposition)
    {
        float[] distanceBetween = {3};
        for (int i = 0; i< MapsFragment.zones.size() ; i++)
        {
            android.location.Location.distanceBetween(myposition.latitude, myposition.longitude, MapsFragment.zones.get(i)[0], MapsFragment.zones.get(i)[1], distanceBetween);
            if(distanceBetween[0]<=10)
            {
                Toast.makeText( this.context,
                        distanceBetween[0]+"m de "+ MapsFragment.nomsZones.get(i),
                        Toast.LENGTH_SHORT ).show();
                MA.onMarkerClick(MapsFragment.markers.get(i));

                if(i>0)
                {
                    this.myPreviousInterestPoint = new LatLng(MapsFragment.zones.get(i - 1)[0], MapsFragment.zones.get(i - 1)[1]);
                    this.myCurrentInterestPoint = new LatLng(MapsFragment.zones.get(i)[0], MapsFragment.zones.get(i)[1]);
                    changePolylineColor(this.myPreviousInterestPoint, this.myCurrentInterestPoint);
                }

            }
        }

    }

    public void changePolylineColor(LatLng start, LatLng end)
    {
        Polyline polygon = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(start.latitude, start.longitude), new LatLng(end.latitude, end.longitude))
                .width(25)
                .color(Color.GREEN)
                .geodesic(true));
    }

    public Boolean getTrackingMode() {
        return trackingMode;
    }

    public void setTrackingMode(Boolean trackingMode) {
        this.trackingMode = trackingMode;
    }

    public LatLng getMyposition() {
        return myposition;
    }


}

