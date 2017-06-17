package com.example.ine5424.smartufsc;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by anna on 6/15/17.
 */

public class BusStop {
    private LatLng location;
    private String name;

    public BusStop(LatLng l, String n) {
        this.location = l;
        this.name = n;
    }

    public void setLocation(LatLng l) {
        this.location = l;
    }

    public void setName(String n) {
        this.name = n;
    }

    public LatLng getLocation() {
        return location;
   }

   public String getName() {
       return name;
   }

}
