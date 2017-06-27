package com.example.ine5424.smartufsc;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private static final String STOP_REQUEST = "Solicitar parada neste ponto!";
    private static final String CANCEL_REQUEST = "Cancelar solicitação de parada.";
    private boolean stopRequested; // informs if a passenger made a request to stop
    private List<Marker> stops; // bus stops for the selected line
    private static List<Marker> busesMarkers = new ArrayList<Marker>(); // buses operating on the selected line
    private Timer timer;
    private int stopRequestId = -1; // stop request for the passenger

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        stopRequested = false;
        stops = new ArrayList<Marker>();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setOnInfoWindowClickListener(this);

        int lineCode = this.getIntent().getExtras().getInt("lineCode");
        getLineStops(lineCode);


    }

    /**
     * Get all line stops that are part of the selected line
     * @param lineCode the bus line code
     */
    public void getLineStops(final int lineCode) {
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                try {
                    BusLine line = (BusLine) MainActivity.getAPI().getLineById(lineCode)
                            .execute().body();
                    return line;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(Object o) {
                BusLine line = (BusLine)o;
                ArrayList<Integer> idLineStop = line.getLineStops();
                ArrayList<Integer> busesId = line.getBuses();
                for (Integer i : idLineStop) {
                    getBusStops(i);
                }
                for (Integer i : busesId) {
                    getBuses(i);
                }

            }
        }.execute();
    }

    /**
     * Get the bus stop for a given line
     * @param lineStop the bus line stop
     */
    public void getBusStops(final int lineStop) {
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                try {
                    return (LineStop)MainActivity.getAPI().getLineStop(lineStop).execute().body();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(Object o) {
                LineStop line = (LineStop)o;
                getStop(line, ((LineStop) o).getStop());
            }
        }.execute();
    }

    /**
     * Get a bus by id to put it on the map
     * @param busId the bus id
     */
    public void getBuses(final int busId) {
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                try {
                    return (Bus)MainActivity.getAPI().getBus(busId).execute().body();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(Object o) {
                Bus bus = (Bus)o;
                putBusOnMap(bus.getLocation(), bus.getId());
            }
        }.execute();
    }

    /**
     * Puts a bus on the map
     * @param loc the current location the bus is at
     * @param id the bus id
     */
    public void putBusOnMap(final LatLng loc, final int id) {

        Marker m = mMap.addMarker(new MarkerOptions().position(loc).title(String.valueOf(id)).
                icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        m.setTag("bus");
        busesMarkers.add(m);
    }

    /**
     * @return true if there are buses operating for the selected bus line
     */
    public static boolean isPopulated() {
        return !busesMarkers.isEmpty();
    }

    public void getStop(final LineStop lineStop, final int stop) {
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                try {
                    return (Stop)MainActivity.getAPI().getStop(lineStop.getStop()).execute().body();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(Object o) {
                putStopsonMap((Stop)o, stop);
            }
        }.execute();
    }

    /**
     * Puts bus stops on the map
     * @param stop the object stop
     * @param id the id of the stop
     */
    public void putStopsonMap(Stop stop, int id) {
        LatLng coordinate = stop.getCoordinates();
        String stopName = stop.getName();

        Marker m = mMap.addMarker(new MarkerOptions().position(coordinate).title(stopName));
        m.setSnippet(STOP_REQUEST);
        m.setTag((Integer)id);
        stops.add(m);

        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 11);
        mMap.animateCamera(yourLocation);
    }

    /**
     * Move buses on the map
     */
    public static void moveBuses() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                for (Marker marker : busesMarkers) {
                    updateBusesLocation(marker);
                }
            }
        });
    }

    /**
     * Updates the bus location on the map
     * @param m the marker that represents the bus to be updated
     */
    public static void updateBusesLocation(final Marker m) {
        final int busId = Integer.parseInt(m.getTitle());
        MainActivity.getAPI().getBus(busId).enqueue(new Callback<Bus>() {
            @Override
            public void onResponse(Call<Bus> call, Response<Bus> response) {
                LatLng location = response.body().getLocation();
                String lastUpdate = response.body().getLastUpdate();
                m.setPosition(location);
                m.setSnippet("Última atualização: " + lastUpdate);
            }

            @Override
            public void onFailure(Call<Bus> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
    }


    /**
     * Handle a stop request made by a passenger
     * @param marker the stop that belongs to the request
     */
    @Override
    public void onInfoWindowClick(Marker marker) {
        if (marker.getTag().toString().equalsIgnoreCase("bus")) {
            moveBuses();
            return;
        }
        int busId = 27; // qual onibus
        int stopId = (Integer)marker.getTag();
        if (stopId != stopRequestId  && stopRequestId != -1) {
            return;
        }
        if (!stopRequested) {
            marker.setSnippet(CANCEL_REQUEST);
            sendStopRequest(busId, stopId);
            stopRequested = true;
            stopRequestId = stopId;
        } else {
            marker.setSnippet(STOP_REQUEST);
            stopRequestId = -1;
            deletePassengerStop(Integer.parseInt((Paper.book().read("requestId").toString())));
            stopRequested = false;
        }

        marker.showInfoWindow();
    }

    /**
     * Send stop request to the server
     * @param busId the bus id
     * @param stopId the stop id
     */
    public void sendStopRequest(final int busId, final int stopId) {
        MainActivity.getAPI().savePassengerStop(busId, stopId).enqueue(new Callback<PassengerStops>() {
            @Override
            public void onResponse(Call<PassengerStops> call, Response<PassengerStops> response) {
                Paper.book().write("busId", busId);
                Paper.book().write("stopId", stopId);
                Paper.book().write("requestId", response.body().getRequestId());
            }

            @Override
            public void onFailure(Call<PassengerStops> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
    }


    /**
     * Handles when a passenger cancels a stop request
     * @param requestId the id of the request made
     */
    public void deletePassengerStop(int requestId) {
        MainActivity.getAPI().deletePassengerStop(requestId).enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Paper.book().delete("requestId");
                Paper.book().delete("busId");
                Paper.book().delete("stopId");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println(t.toString());

            }
        });
    }

}
