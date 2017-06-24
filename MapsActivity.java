package com.example.ine5424.smartufsc;


import android.graphics.Color;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.MainThread;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;


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
import com.google.android.gms.maps.model.PolylineOptions;


import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.DELETE;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private static final String STOP_REQUEST = "Solicitar parada neste ponto!";
    private static final String CANCEL_REQUEST = "Cancelar solicitação de parada.";
    LocationManager locationManager;
    private boolean stopRequested;
    private List<Marker> stops;
    private static List<Marker> busesMarkers = new ArrayList<Marker>();
    private Timer timer;

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

    public void putBusOnMap(final LatLng loc, final int id) {
        Marker m = mMap.addMarker(new MarkerOptions().position(loc).title(String.valueOf(id)).
                icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        m.setTag("bus");
        busesMarkers.add(m);
    }

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


//        updateBusLocation();
    }
    private static final ScheduledExecutorService worker =
            Executors.newSingleThreadScheduledExecutor();

    public static void moveBuses() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                for (Marker marker : busesMarkers) {
                    updateBusesLocation(marker);
                }

            }
        });
//        new AsyncTask<Object, Object, Object>() {
//            @Override
//            protected Object doInBackground(Object... params) {
//                for (Marker marker : busesMarkers) {
//                    updateBusesLocation(marker);
//                }
//                return null;
//            }
//        }.execute();
    }

    public static void updateBusesLocation(final Marker m) {
        final int busId = Integer.parseInt(m.getTitle());
        MainActivity.getAPI().getBus(busId).enqueue(new Callback<Bus>() {
            @Override
            public void onResponse(Call<Bus> call, Response<Bus> response) {
                LatLng location = response.body().getLocation();
                m.setPosition(location);
            }

            @Override
            public void onFailure(Call<Bus> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
    }

    public void drawRoute() {
        PolylineOptions path = new PolylineOptions().width(4).color(Color.BLUE);
        for (int i = 0; i < stops.size(); i++) {
            path.add(stops.get(i).getPosition());
        }
        mMap.addPolyline(path);
    }

    public Marker moveMarker(int id, double lat, double lng, double i) {
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


//        LatLng l1 = stops.get(0).getPosition();
//        LatLng l2 = stops.get(stops.size()-1).getPosition();
        LatLngInterpolator l = new LatLngInterpolator.Spherical();
        Marker m1 = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("Bus moving").
                icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        MarkerAnimation.animateMarkerToGB(m1, new LatLng(lat+i, lng+ i), l);
//        m1.setPosition();
        return m1;

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if (marker.getTag().toString().equalsIgnoreCase("bus")) {
            moveBuses();
            return;
        }
        int busId = Integer.parseInt(marker.getId());
        int stopId = (Integer)marker.getTag();
        if (!stopRequested) {
            marker.setSnippet(CANCEL_REQUEST);
            sendStopRequest(busId, stopId);
            stopRequested = true;
        } else {
            marker.setSnippet(STOP_REQUEST);
            deletePassengerStop(Integer.parseInt((Paper.book().read("requestId").toString())));
            stopRequested = false;
        }

        marker.showInfoWindow();
    }

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
