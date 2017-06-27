package com.example.ine5424.smartufsc;

/**
 * Created by anna on 6/16/17.
 */

public class LineStop {
    private int order; // the order of that stop within the line context
    private int stop; // stop id
    private int line; // line id

    public LineStop() {

    }

    /**
     * @return the order of the stop for the line
     */
    public int getOrder() {
        return order;
    }

    /**
     * @return the stop id
     */
    public int getStop() {
        return stop;
    }

    /**
     * @return the line id
     */
    public int getLine() {
        return line;
    }

}
