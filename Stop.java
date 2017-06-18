package com.example.ine5424.smartufsc;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by anna on 6/16/17.
 */

public class Stop {
    private String name;
    private Location location;

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public LatLng getCoordinates() {
        return location.getCoordinates();
    }


}
