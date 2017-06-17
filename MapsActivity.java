package com.example.ine5424.smartufsc;

import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;

    //fakedata
    double lat1 = -27.59945278;
    double long1 = -48.51875782;
    double lat2 = -27.60435879;
    double long2 = -48.52219104;
    ArrayList<Marker> buses = new ArrayList<Marker>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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

        int lineCode = this.getIntent().getExtras().getInt("busLine");
        HashMap<LatLng, String> busStops = this.getLineStops(lineCode);
        for (LatLng location: busStops.keySet()) {
            mMap.addMarker(new MarkerOptions().position(location).title(busStops.get(location)));
        }


        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom((LatLng)busStops.keySet().toArray()[0], 11);
        mMap.animateCamera(yourLocation);

        long t = System.currentTimeMillis();

        while (System.currentTimeMillis() < t + 2000);

    }

    public HashMap<LatLng, String> getLineStops(int lineCode) {
        HashMap<LatLng, String> stops = new HashMap<LatLng, String>();

        switch(lineCode) {
            case 000:
                stops.put(new LatLng(-27.602513, -48.518982), "Fundação Certi");
                stops.put(new LatLng(-27.430717, -48.445486), "Sapiens Park");
                break;
            case 210:
                stops.put(new LatLng(-27.451983, -48.455970), "TICAN");
                stops.put(new LatLng(-27.583227, -48.545400), "Beiramar 15");
                stops.put(new LatLng(-27.586386, -48.552566), "Beiramar 16");
                stops.put(new LatLng(-27.588390, -48.559077), "Beiramar 17");
                stops.put(new LatLng(-27.598908, -48.553910), "TICEN");
                break;
            case 221:
                stops.put(new LatLng(-27.451983, -48.455970), "TICAN");
                stops.put(new LatLng(-27.553390, -48.500352), "Floripa Shopping");
                stops.put(new LatLng(-27.584353, -48.544499), "Mauro Ramos 1 - Shopping Beiramar");
                stops.put(new LatLng(-27.589325, -48.543139), "Mauro Ramos 2");
                stops.put(new LatLng(-27.595960, -48.543820), "Mauro Ramos 3");

                break;
            case 233:
                stops.put(new LatLng(-27.451983, -48.455970), "TICAN");
                stops.put(new LatLng(-27.553390, -48.500352), "Floripa Shopping");
                stops.put(new LatLng(-27.584131, -48.522833), "TITRI");
                stops.put(new LatLng(-27.553390, -48.500352), "UFSC - Delfino Conti");
                break;
            case 999:
                stops.put(new LatLng(-27.60312280, -48.51721286), "1");
                stops.put(new LatLng(-27.60411159, -48.52461576), "2");
                stops.put(new LatLng(-27.59684756, -48.52322101), "3");
                break;
        }
        return stops;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void addBusesOnMap(View view) throws JSONException, InterruptedException {

        //TODO: request buses locations to webserver, informing bus line

        for(Marker bus : buses){
            bus.remove();
        }

        buses = new ArrayList<Marker>();

        //begin fakedata -------
        LatLng bus = new LatLng(lat1, long1);
        LatLng bus2 = new LatLng(lat2, long2);
        Marker busInRoute = mMap.addMarker(new MarkerOptions().position(bus).title("221_1").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        Marker busInRoute2 = mMap.addMarker(new MarkerOptions().position(bus2).title("221_1").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        buses.add(busInRoute);
        buses.add(busInRoute2);

        //end fakedata -------------

        ArrayList<LatLng> directionPoint = new ArrayList<LatLng>();
        directionPoint.add(new LatLng(-27.60312280, -48.51721286));
        directionPoint.add(new LatLng(-27.60411159, -48.52461576));
        directionPoint.add(new LatLng(-27.59684756, -48.52322101));

        PolylineOptions rectLine = new PolylineOptions().width(3).color(Color.YELLOW);

        for (int i = 0; i < directionPoint.size(); i++) {
            rectLine.add(directionPoint.get(i));
        }
        Polyline polylin = mMap.addPolyline(rectLine);

    }
}
