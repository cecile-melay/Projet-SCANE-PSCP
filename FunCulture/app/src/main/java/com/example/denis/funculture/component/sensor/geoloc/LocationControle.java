package com.example.denis.funculture.component.sensor.geoloc;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by user on 07/04/2017.
 */

public class LocationControle {

    public LocationControle(){};

    public static double calculCoeffiscientDirecteur(LatLng latLng1, LatLng latLng2) {
        /*
        * Le coeffiscient directeur sera la valeur a comparer pour savoir si un utilisateur fait demi tour
         */
        return (latLng2.latitude - latLng1.latitude) / (latLng2.longitude - latLng1.longitude);
    }

    public static double calculDistance(LatLng latLng1, LatLng latLng2) {
        /*
        *On va pouvoir savoir si un utilisateur reste dans la bonne direction en vérifiant que la distance avec le point prochain n'est pas trop grande
         */
        return Math.sqrt(( (latLng1.longitude - latLng2.longitude)*(latLng1.longitude - latLng2.longitude)) - ( (latLng2.latitude - latLng1.latitude) *(latLng2.latitude - latLng1.latitude)));
    }

}
