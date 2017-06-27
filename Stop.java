package com.example.ine5424.smartufsc;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by anna on 6/16/17.
 */

public class Stop {
    private String name; // stop name
    private Location location; // stop location

    public Stop() { }

    /**
     * @return the stop name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the stop location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * @return coordinates of the stop
     */
    public LatLng getCoordinates() {
        return location.getCoordinates();
    }


}
