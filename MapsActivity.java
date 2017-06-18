package com.example.ine5424.smartufsc;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.example.latlnginterpolation.LatLngInterpolator;
import com.example.latlnginterpolation.MarkerAnimation;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.POST;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private static final String STOP_REQUEST = "Solicitar parada neste ponto!";
    private static final String CANCEL_REQUEST = "Cancelar solicitação de parada.";
    LocationManager locationManager;
    private boolean stopRequested;
    private List<Marker> stops;

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
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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

    public void getLineStops(final int lineCode) {
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                try {
                    return (BusLine) MainActivity.getAPI().getLineById(lineCode)
                            .execute().body();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(Object o) {
                ArrayList<Integer> idLineStop = ((BusLine)o).getLineStops();
                for (Integer i : idLineStop) {
                    getBusStops(i);
                }

            }
        }.execute();
    }

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

    public void putStopsonMap(Stop stop, int id) {
        LatLng coordinate = stop.getCoordinates();
        String stopName = stop.getName();

        Marker m = mMap.addMarker(new MarkerOptions().position(coordinate).title(stopName));
        m.setSnippet(STOP_REQUEST);
        m.setTag((Integer)id);
        stops.add(m);
        drawRoute();

        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 11);
        mMap.animateCamera(yourLocation);
        moveMarker();
    }

    public void drawRoute() {
        PolylineOptions path = new PolylineOptions().width(4).color(Color.BLUE);
        for (int i = 0; i < stops.size(); i++) {
            path.add(stops.get(i).getPosition());
        }
        mMap.addPolyline(path);
    }

    public void moveMarker() {
        LatLng origin, dest;
        LatLngInterpolator interpolator = new LatLngInterpolator.Spherical();
        Marker marker = null;
//        for (int i = 0 ; i < stops.size()-1; i++) {
//            origin = stops.get(i).getPosition();
//
//            dest = stops.get(i+1).getPosition();
//            marker = mMap.addMarker(new MarkerOptions().position(origin).title("Bus moving").
//                    icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
//            MarkerAnimation.animateMarkerToGB(marker, dest, interpolator);
//
//        }

        LatLng l1 = stops.get(0).getPosition();
        LatLng l2 = stops.get(stops.size()-1).getPosition();
        LatLngInterpolator l = new LatLngInterpolator.LinearFixed();
        Marker m1 = mMap.addMarker(new MarkerOptions().position(l1).title("Bus moving").
                icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        MarkerAnimation.animateMarkerToGB(m1, l2, l);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        int busId = 1;
        int stopId = (Integer)marker.getTag();
        if (stopRequested) {
            marker.setSnippet(CANCEL_REQUEST);
            sendStopRequest(busId, stopId);
        } else {
            marker.setSnippet(STOP_REQUEST);
//            deletePassengerStop(busId, stopId);
        }
        stopRequested = !stopRequested;
        marker.showInfoWindow();
    }

    public void sendStopRequest(int busId, int stopId) {
        System.out.println(busId + " " + stopId);
        MainActivity.getAPI().savePassengerStop(busId, stopId).enqueue(new Callback<POST>() {
            @Override
            public void onResponse(Call<POST> call, Response<POST> response) {

            }

            @Override
            public void onFailure(Call<POST> call, Throwable t) {

            }
        });
    }


//    public void deletePassengerStop(int bus, int busstop) {
//        MainActivity.getAPI().deletePassengerStop(bus, busstop).enqueue(new Callback<DELETE>() {
//            @Override
//            public void onResponse(Call<DELETE> call, Response<DELETE> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<DELETE> call, Throwable t) {
//
//            }
//        });
//    }


}
