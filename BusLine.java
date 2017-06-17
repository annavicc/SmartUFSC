package com.example.ine5424.smartufsc;

import java.util.ArrayList;

/**
 * Created by anna on 5/27/17.
 */

public class BusLine {
    private int code;
    private String name;
    private ArrayList<BusStop> stops;


    public BusLine(int c, String n) {
        this.code = c;
        this.name = n;
    }

    public void setCode(int c) {
        this.code = c;
    }

    public void setName(String n) {
        this.name = n;
    }

    public int getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }
}
