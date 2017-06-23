package com.example.ine5424.smartufsc;

import com.google.android.gms.common.api.Api;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.POST;

/**
 * Created by anna on 6/18/17.
 */

public class PassengerStops {

    private int id;
    private int bus;
    private int busstop;

    public PassengerStops() {

    }

    public int getRequestId() {
        return this.id;
    }

    public int getBusId() {
        return this.bus;
    }

    public int getStopId() {
        return this.busstop;
    }

    public void savePassengerStop(int bus, int busstop) {
        ApiInterface api = MainActivity.getAPI();
        try {
            api.savePassengerStop(bus, busstop).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  void deletePassengerStop(int requestId) {
        ApiInterface api = MainActivity.getAPI();
        try {
            api.deletePassengerStop(requestId).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
