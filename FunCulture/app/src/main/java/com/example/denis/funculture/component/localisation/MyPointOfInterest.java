package com.example.denis.funculture.component.localisation;

import android.graphics.Point;

import com.example.denis.funculture.utils.Util;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by denis on 25/04/2017.
 */

public class MyPointOfInterest {
    private int id;
    private PointOfPath point;
    private String name;
    private String description;
    private String sound;
    private List<String> pictures;

    public MyPointOfInterest(int id, LatLng point, String name) {
        this.pictures = new ArrayList<>();
    }

    public MyPointOfInterest(int id, int pointId, String name, String description, String sound) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.sound = sound;
        this.point = Util.getCurrentPath().getPoint(pointId);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PointOfPath getPoint() {
        return point;
    }

    public void setPoint(PointOfPath point) {
        this.point = point;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }
}
