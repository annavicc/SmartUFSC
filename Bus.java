package com.example.ine5424.smartufsc;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.IdentityHashMap;

/**
 * Created by anna on 6/22/17.
 */

public class Bus {
    private int id;
    private Location location;
    private int line;
    private String last_update;
    private ArrayList<Integer> passengerstops;

    public int getId() {
        return id;
    }

    public LatLng getLocation() {
        return location.getCoordinates();
    }

    public int getLine() {
        return line;
    }

    public String getLastUpdate() {
        return  last_update;
    }

    public ArrayList<Integer> getPassengerstops() {
        return passengerstops;
    }
}
