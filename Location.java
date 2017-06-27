package com.example.ine5424.smartufsc;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by anna on 6/16/17.
 */

public class Location {
    private String type; // type of location
    private double[] coordinates; // coordinates in Lat Lng

    public Location() { }
    public Location(String t, double[] c) {
        type = t;
        coordinates = c;
    }

    /**
     * @return the type of the location
     */
    public String getType() {
        return this.type;
    }

    /**
     * @return the coordinates
     */
    public LatLng getCoordinates() {
        return new LatLng(coordinates[1], coordinates[0]);
    }

}
