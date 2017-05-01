package com.example.denis.funculture.component.localisation;

import com.example.denis.funculture.utils.MyServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PointOfInterest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by denis on 25/04/2017.
 */

public class Path {
    private int id;
    private String name;
    private List<LatLng> points;
    private List<MyPointOfInterest> pointsOfInterest;

    public Path(int id, String name) {
        points = new ArrayList<>();
        pointsOfInterest = new ArrayList<>();
        this.id = id;
        this.name = name;
    }

    public void addPoint(LatLng point) {
        this.points.add(point);
    }

    public void addPointOfInterest(MyPointOfInterest pointOfInterest) {
        this.pointsOfInterest.add(pointOfInterest);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void savePointsOfPath() {
        for(int i=0; i<this.points.size(); i++) {
            LatLng point = this.points.get(i);
            MyServices.getSingleton().postPoint(point.latitude, point.longitude, i, id);
        }
    }

    public void saveOnServer() {
        MyServices.getSingleton().postPath(this);
        savePointsOfPath();
    }
}
