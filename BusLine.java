package com.example.ine5424.smartufsc;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by anna on 6/16/17.
 */

public class BusLine {
    private int id;
    private String name;
    private ArrayList<Integer> linestops;
    private ArrayList<Integer> buses;

    public BusLine() {

    }

    public BusLine(int c, String n) {
        this.id = c;
        this.name = n;
        linestops = new ArrayList<Integer>();
        buses = new ArrayList<Integer>();
    }

    public void setCode(int c) {
        this.id = c;
    }

    public void setName(String n) {
        this.name = n;
    }

    public int getCode() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<Integer> getLineStops() {
        return this.linestops;
    }

    public ArrayList<Integer> getBuses() {
        return this.buses;
    }
}
