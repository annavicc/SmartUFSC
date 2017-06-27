package com.example.ine5424.smartufsc;

import com.google.android.gms.maps.model.LatLng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by anna on 6/22/17.
 */

public class Bus {
    private int id; // bus id
    private Location location; // current location of the bus
    private int line; // line the bus is current operating for
    private String last_update; // last update of location
    private ArrayList<Integer> passengerstops; // passengers stop requests

    /**
     * @return bus id
     */
    public int getId() {
        return id;
    }

    /**
     * @return location of the bus
     */
    public LatLng getLocation() {
        return location.getCoordinates();
    }

    /**
     * @return the line the bus is operating
     */
    public int getLine() {
        return line;
    }

    /**
     * @return the time in HH:mm:ss of the last location update
     */
    public String getLastUpdate() {
        String defaultTimezone = TimeZone.getDefault().getID();
        Date date = null;
        String d = last_update;
        try {
            date = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ")).
                    parse(d.replaceAll("Z$", "+0000"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
        return time.format(date);
    }

    /**
     * @return list of passenger stop requests
     */
    public ArrayList<Integer> getPassengerstops() {
        return passengerstops;
    }
}
