package com.example.denis.funculture.component.localisation;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by remi on 04/05/2017.
 */

public class PointOfPath {
    int id;
    private LatLng latLng;
    private int position;

    public PointOfPath(int id) {
        this.id = id;
        this.latLng = new LatLng(0,0);
        this.position = 0;
    }
    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public String ToString() {
        return String.format(" PointOfPath id : %d LatLng : %s position : %d ------ ", id, latLng.toString(), position);
    }
}
