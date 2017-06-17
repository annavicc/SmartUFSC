package com.example.ine5424.smartufsc;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by anna on 6/16/17.
 */

public class Line {
    private int id;
    private String name;
    private ArrayList<Integer> linestops;
    private ArrayList<Integer> buses;

    public Line() {

    }

    public Line(int c, String n) {
        this.id = c;
        this.name = n;
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
}
