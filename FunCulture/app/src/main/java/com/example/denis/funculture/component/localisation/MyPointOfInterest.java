package com.example.denis.funculture.component.localisation;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by denis on 25/04/2017.
 */

public class MyPointOfInterest {
    private int id;
    private LatLng point;
    private String name;
    private String description;
    private String sound;
    private List<String> pictures;

    public MyPointOfInterest(int id, LatLng point, String name) {
        this.pictures = new ArrayList<>();
    }

    public void addPicture(String picturePath) {
        this.pictures.add(picturePath);
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

    public int getId() {
        return id;
    }

    public LatLng getPoint() {
        return point;
    }

    public String getName() {
        return name;
    }

    public List<String> getPictures() {
        return pictures;
    }
}
