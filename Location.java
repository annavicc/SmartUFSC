package com.example.ine5424.smartufsc;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by anna on 6/16/17.
 */

public class Location {
    private String type;
    private double[] coordinates;

    public Location(String t, double[] c) {
        type = t;
        coordinates = c;
    }

    public String getType() {
        return this.type;
    }

    public LatLng getCoordinates() {
        return new LatLng(coordinates[1], coordinates[0]);
    }

}
