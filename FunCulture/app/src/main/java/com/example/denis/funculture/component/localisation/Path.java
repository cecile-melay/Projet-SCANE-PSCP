package com.example.denis.funculture.component.localisation;

import com.example.denis.funculture.component.qcm.QCM;
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
    private QCM qcm;
    private List<PointOfPath> points;
    private List<MyPointOfInterest> pointsOfInterest;

    public Path(int id, String name) {
        points = new ArrayList<>();
        pointsOfInterest = new ArrayList<>();
        this.id = id;
        this.name = name;
    }

    public void addPoint(PointOfPath point) {
        this.points.add(point);
    }

    public void addPoint(LatLng point) {
        PointOfPath pop = new PointOfPath(1);
        pop.setPosition(points.size() + 1);
        pop.setLatLng(point);
        this.points.add(pop);
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
            PointOfPath point = this.points.get(i);
            MyServices.getSingleton().postPoint(point.getLatLng().latitude, point.getLatLng().longitude, i, id);
        }
    }

    public void saveOnServer() {
        MyServices.getSingleton().postPath(this);
        savePointsOfPath();
    }

    public PointOfPath getPoint(int pointId) {
        for(PointOfPath point : this.points) {
            if(point.getId() == pointId) {
                return point;
            }
        }

        return null;
    }

    public List<PointOfPath> getPoints() {
        return points;
    }

    public List<MyPointOfInterest> getPointsOfInterest() {
        return pointsOfInterest;
    }

    public String ToString() {
        String s = String.format(" Path id : %d name : %s ------ ", id, name);

        for(PointOfPath pointOfPath : points) {
            s += pointOfPath.ToString();
        }

        for(MyPointOfInterest pointOfInterest : pointsOfInterest) {
            s += pointOfInterest.ToString();
        }

        return s;
    }

    public void setQCM(QCM qcm) {
        this.qcm = qcm;
    }

    public QCM getQcm() {
        return qcm;
    }
}
