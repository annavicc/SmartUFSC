package com.example.ine5424.smartufsc;

import android.app.DownloadManager;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by anna on 6/15/17.
 */

public class BusTrackerApi {

    private static BusTrackerApi api;
    private String url = "http://10.0.2.2:8000/api/";
    private RequestQueue requestQueue;
    private static ArrayList<BusLine> busLinesList;
    private static ArrayList<String> busLinesString;

    private BusTrackerApi() {
        busLinesList = new ArrayList<BusLine>();
        busLinesString = new ArrayList<String>();
    }

    public static BusTrackerApi getInstance() {
        if (api == null) {
            api = new BusTrackerApi();
        }
        return api;
    }

    public void setRequestQueue(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public void sendRequest(String address) {
        address = url + address;
        JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (Request.Method.GET, address, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        parseLines(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        System.out.println("Erro na request " + error.toString());
                    }

                });
        requestQueue.add(jsObjRequest);
    }

    public void sendRequestStops(String address) {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, address, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            parseBusStops(response.getString("stop"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        System.out.println("Erro na request " + error.toString());
                    }
                });
        requestQueue.add(jsObjRequest);
    }

    public ArrayList<String> getLinesList() {
        ArrayList<String> lines = new ArrayList<String>();
        for (BusLine b : busLinesList) {
            lines.add(b.getCode() + " - " + b.getName());
        }
        return busLinesString;
    }

    public ArrayList<BusLine> getBusLines() {
        return busLinesList;
    }

    private void parseLines(JSONArray json) {
        String id, name;
        JSONArray stops;
        for (int i = 0; i < json.length(); i++) {
            try {
                JSONObject obj = json.getJSONObject(i);
                id = obj.getString("id");
                name = obj.getString("name");
                stops = obj.getJSONArray("linestops");
                parseLinesStops(stops);
                busLinesList.add (new BusLine(Integer.parseInt(id), name));
                busLinesString.add(id + " - " + name);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void parseLinesStops(JSONArray stops) {
        ArrayList<String> lineStops = new ArrayList<String>();
        for(int i = 0 ; i < stops.length(); i++) {
            try {
                lineStops.add(stops.get(i).toString());
                sendRequestStops(lineStops.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void parseBusStops(String stop) {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, stop, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println(response.getJSONObject("location").getJSONArray("coordinates"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        System.out.println("Erro na request " + error.toString());
                    }
                });
        requestQueue.add(jsObjRequest);

    }

    private void parseLatLng(String l) {
        LatLng loc;
//        getInstance().sendRequest("l", );

    }


}
