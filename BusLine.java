package com.example.ine5424.smartufsc;

import java.util.ArrayList;

/**
 * Created by anna on 6/16/17.
 */

public class BusLine {
    private int id; // id of the bus line
    private String name; // name of the bus line
    private ArrayList<Integer> linestops; // list with line stops
    private ArrayList<Integer> buses; // list of buses that operates for this line

    public BusLine() {

    }

    public BusLine(int c, String n) {
        this.id = c;
        this.name = n;
        linestops = new ArrayList<Integer>();
        buses = new ArrayList<Integer>();
    }

    /**
     * @return the code of the bus line
     */
    public int getCode() {
        return this.id;
    }

    /**
     * @return the bus line name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return a list with all stops for this line
     */
    public ArrayList<Integer> getLineStops() {
        return this.linestops;
    }

    /**
     * @return a list with all buses that operate for this line
     */
    public ArrayList<Integer> getBuses() {
        return this.buses;
    }
}
