package com.example.ine5424.smartufsc;

import java.io.IOException;

/**
 * Created by anna on 6/18/17.
 */

public class PassengerStops {

    private int id; // id of the document
    private int bus; // bus id
    private int busstop; // bus stop it

    public PassengerStops() { }

    /**
     * @return the id of the passenger request
     */
    public int getRequestId() {
        return this.id;
    }

    /**
     * @return the id of the bus that received the request
     */
    public int getBusId() {
        return this.bus;
    }

    /**
     * @return the stop id
     */
    public int getStopId() {
        return this.busstop;
    }

    /**
     * @param bus the bus id that received the request
     * @param busstop the stop id of the request
     */
    public void savePassengerStop(int bus, int busstop) {
        ApiInterface api = MainActivity.getAPI();
        try {
            api.savePassengerStop(bus, busstop).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param requestId the id of the request
     */
    public  void deletePassengerStop(int requestId) {
        ApiInterface api = MainActivity.getAPI();
        try {
            api.deletePassengerStop(requestId).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
