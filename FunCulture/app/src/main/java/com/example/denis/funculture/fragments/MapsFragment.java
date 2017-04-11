package com.example.denis.funculture.fragments;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.denis.funculture.R;
import com.example.denis.funculture.component.sensor.geoloc.AlertReceiver;
import com.example.denis.funculture.component.sensor.geoloc.MyLocationListener;
import com.example.denis.funculture.main.App;
import com.example.denis.funculture.utils.MyResources;
import com.example.denis.funculture.utils.Util;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Timer;


/*
 * Main Activity which contains the Google Map
 */
public class MapsFragment extends MyFragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Timer timer = new Timer();
    PolylineOptions polylineOptions;
    private LocationManager locManager;
    private LocationListener locListener;
    private Looper looper;
    private int intervalGeolocRefresh = 3000;
    private Location startLocation;
    private ArrayList<Marker> tabMarkers = new ArrayList<Marker>();
    public ArrayList<LatLng> way = new ArrayList<LatLng>();

    public static ArrayList<Double[]> zones = new ArrayList<Double[]>();
    public static ArrayList<String> nomsZones = new ArrayList<String>();
    public static ArrayList<Marker> markers = new ArrayList<Marker>();

    private static final int MY_PERMISSIONS_REQUEST_GEOLOCATION_FINE = 0;
    private static final int MY_PERMISSIONS_REQUEST_GEOLOCATION_COARSE = 0;
    private com.google.android.gms.maps.model.PolygonOptions polygonOptions;

    public MapsFragment() {
    }


    @Override
    protected int getLayoutId() {
        return R.layout.maps_fragment;
    }

    @Override
    protected String getTitle(){
        return MyResources.GPS;
    }

    @Override
    protected void init() {
        super.init();

        SupportMapFragment mapFragment = new SupportMapFragment();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.map_fragment_frame, mapFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locManager.removeUpdates(locListener);
            mMap.setMyLocationEnabled(false);
        }
        Toast.makeText(getActivity(), "OnPause()", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        App.getSingleton().setCurrentFragment(this);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
            //locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);;
            //mMap.setMyLocationEnabled(true);
        }
        Toast.makeText(getActivity(), "OnResume()", Toast.LENGTH_SHORT).show();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        MapsFragment ma = this;
        // Check Geolocation permission
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Util.checkPrivileges(getActivity(), MyResources.MY_PERMISSIONS_REQUEST_GEOLOCATION_FINE, MyResources.MY_PERMISSIONS_REQUEST_GEOLOCATION_COARSE);
        } else {

            mMap.setMyLocationEnabled(true);
            // Use the LocationManager class to obtain GPS locations
            locListener = new MyLocationListener(this, mMap);
            locManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);
            if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Toast.makeText(getActivity(),
                        "Activez le GPS",
                        Toast.LENGTH_SHORT).show();
            }

            // Use the LocationManager class to obtain GPS locations
            /*locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            startLocation = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            locListener = new MyLocationListener(this, mMap);
            if(locManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            {
                locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, locListener);
                Toast.makeText(this,
                        "Activez le GPS",
                        Toast.LENGTH_SHORT).show();
            }
            if(startLocation!=null) {
                locListener.onLocationChanged(startLocation);
                mMap.animateCamera(CameraUpdateFactory.zoomTo(25), 2000, null);
            }
            else
                locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);

            looper = Looper.myLooper();

            //Intent intent = new Intent(this, AlertReceiver.class);
            //PendingIntent pending = PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

            zones.add(new Double[]{43.210103, 6.025803}); // Maison Jux
            nomsZones.add("Maison Jux");
            zones.add(new Double[]{43.209415, 6.026087}); // Cimetière
            nomsZones.add("Cimetière");
            zones.add(new Double[]{43.209254, 6.027240}); // Pharmacie
            nomsZones.add("Pharmacie");
            zones.add(new Double[]{43.208314, 6.025117});  // Banc montée
            nomsZones.add("Banc montée");
            zones.add(new Double[]{43.207563, 6.024693});  // Square Leo Lagrange
            nomsZones.add("Square Leo Lagrange");
            zones.add(new Double[]{43.205904, 6.024352});  // Eglise
            nomsZones.add("Eglise");
            zones.add(new Double[]{43.206334, 6.025166});  // Boulangerie
            nomsZones.add("Boulangerie");
            */

            zones.add(new Double[]{43.616909, 7.064413});  // Parking
            nomsZones.add("Parking");
            zones.add(new Double[]{43.616824, 7.064771});  // Ping Pong
            nomsZones.add("Table de ping pong");
            zones.add(new Double[]{43.624938, 7.050284});  // Bord gauche Newton
            nomsZones.add("Bord gauche Newton");
            zones.add(new Double[]{43.624557, 7.050940});  // Passerelle Newton
            nomsZones.add("Passerelle Newton");
            zones.add(new Double[]{43.617337, 7.064026});  // Administration MIAGE
            nomsZones.add("Administration");
            zones.add(new Double[]{43.721782, 7.252956});  // Entrée Villa Arson
            nomsZones.add("Entrée Villa Arson");
            zones.add(new Double[]{43.721575, 7.252669});  // Pinède Villa Arson
            nomsZones.add("Pinède Villa Arson");
            zones.add(new Double[]{43.721603, 7.252355});  // Vieux batiment pinède Villa Arson
            nomsZones.add("Vieux batiment pinède Villa Arson");
            zones.add(new Double[]{43.721338, 7.252879});  // Pareterre carrelage Villa Arson
            nomsZones.add("Pareterre carrelage Villa Arson");
            zones.add(new Double[]{43.721031, 7.252900});  // Passerelle entrée rouge Villa Arson
            nomsZones.add("Passerelle entrée rouge de la Villa Arson");
            zones.add(new Double[]{43.721018, 7.253120});  // Gauche rouge Villa Arson
            nomsZones.add("Gauche rouge de la Villa Arson");
            zones.add(new Double[]{43.720887, 7.253013});  // Arrière rouge Villa Arson
            nomsZones.add("Arrière rouge de la Villa Arson");
            zones.add(new Double[]{43.720976, 7.253480});  // Grand plateau gauche Villa Arson
            nomsZones.add("Grand plateau gauche Villa Arson");
            zones.add(new Double[]{43.719751, 7.253722});  // Terrasse finale avec pyramide aztec Villa Arson
            nomsZones.add("Terrasse finale avec pyramide aztec Villa Arson");


            Circle circle;
            circle = mMap.addCircle(new CircleOptions() // Parking
                    .center(new LatLng(43.616909, 7.064413))
                    .radius(5)
                    .strokeColor(Color.BLUE)
                    .fillColor(Color.RED));
            circle = mMap.addCircle(new CircleOptions() // Ping Pong
                    .center(new LatLng(43.616824, 7.064771))
                    .radius(5)
                    .strokeColor(Color.RED)
                    .fillColor(Color.BLUE));
            circle = mMap.addCircle(new CircleOptions() // Bord gauche Newton
                    .center(new LatLng(43.624938, 7.050284))
                    .radius(5)
                    .strokeColor(Color.RED)
                    .fillColor(Color.GREEN));
            circle = mMap.addCircle(new CircleOptions() // Passerelle Newton
                    .center(new LatLng(43.624557, 7.050940))
                    .radius(5)
                    .strokeColor(Color.BLUE)
                    .fillColor(Color.YELLOW));
            circle = mMap.addCircle(new CircleOptions() // Administration MIAGE
                    .center(new LatLng(43.617337, 7.064026))
                    .radius(15)
                    .strokeColor(Color.BLUE)
                    .fillColor(Color.WHITE));
            circle = mMap.addCircle(new CircleOptions() // Entrée Villa Arson
                    .center(new LatLng(43.721782, 7.252956))
                    .radius(5)
                    .strokeColor(Color.BLUE));
            circle = mMap.addCircle(new CircleOptions() // Pinède Villa Arson
                    .center(new LatLng(43.721575, 7.252669))
                    .radius(5)
                    .strokeColor(Color.GREEN));
            circle = mMap.addCircle(new CircleOptions() // Vieux batiment pinède Villa Arson
                    .center(new LatLng(43.721603, 7.252355))
                    .radius(5)
                    .strokeColor(Color.RED));
            circle = mMap.addCircle(new CircleOptions() // Pareterre carrelage Villa Arson
                    .center(new LatLng(43.721338, 7.252879))
                    .radius(5)
                    .strokeColor(Color.YELLOW));
            circle = mMap.addCircle(new CircleOptions() // Passerelle entrée rouge Villa Arson
                    .center(new LatLng(43.721031, 7.252900))
                    .radius(5)
                    .strokeColor(Color.RED));
            circle = mMap.addCircle(new CircleOptions() // Gauche rouge Villa Arson
                    .center(new LatLng(43.721018, 7.253120))
                    .radius(5)
                    .strokeColor(Color.WHITE));
            circle = mMap.addCircle(new CircleOptions() // Arrière rouge Villa Arson
                    .center(new LatLng(43.720887, 7.253013))
                    .radius(5)
                    .strokeColor(Color.BLUE));
            circle = mMap.addCircle(new CircleOptions() // Grand plateau gauche Villa Arson
                    .center(new LatLng(43.720976, 7.253480))
                    .radius(5)
                    .strokeColor(Color.YELLOW));
            circle = mMap.addCircle(new CircleOptions() // Terrasse finale avec pyramide aztec Villa Arson
                    .center(new LatLng(43.719751, 7.253722))
                    .radius(5)
                    .strokeColor(Color.GREEN));


            //addProximityAlerts(zones, nomsZones);

            Marker markerParking = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(43.616909, 7.064413))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.photo))
                    .title("Parking")
                    .snippet("parking de la formation MIAGE")
            );
            Marker markerPingPong = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(43.616824, 7.064771))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.pingpong))
                    .title("Ping Pong")
                    .snippet("Table de ping pong")
            );
            Marker markerBordGaucheNewton = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(43.624938, 7.050284))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.photo))
                    .title("Bord gauche Newton")
                    .snippet("Bord gauche Newton")
            );
            Marker markerPasserelleNewton = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(43.624557, 7.050940))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.photo))
                    .title("Passerelle Newton")
                    .snippet("Passerelle Newton")
            );
            Marker markerAdministrationMIAGE = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(43.617337, 7.064026))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.photo))
                    .title("Administration")
                    .snippet("Administration MIAGE")
            );
            Marker markerEntreeVillaArson= mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(43.721782, 7.252956))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.photo))
                    .title("Entrée Villa Arson")
                    .snippet("Accueil Villa Arson")
            );
            Marker markerPinedeVillaArson= mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(43.721575, 7.252669))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.photo))
                    .title("Pinède Villa Arson")
                    .snippet("Un espace de verdure")
            );
            Marker markerAncienBatimentVillaArson= mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(43.721603, 7.252355))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.photo))
                    .title("Vieux batiment pinède Villa Arson")
                    .snippet("Un ancien batiment dans la pinède de la Villa Arson")
            );
            Marker markerPareterreVillaArson= mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(43.721338, 7.252879))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.photo))
                    .title("Pareterre carrelage Villa Arson")
                    .snippet("Retour à la civilisation")
            );
            Marker markerEntreeRougeVillaArson= mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(43.721031, 7.252900))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.photo))
                    .title("Passerelle rouge entrée Villa Arson")
                    .snippet("L'entrée Rouge")
            );
            Marker markerBordGaucheVillaArson= mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(43.721018, 7.253120))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.photo))
                    .title("Bord gauche batiment rouge Villa Arson")
                    .snippet("Le bord gauche rouge")
            );
            Marker markerArriereVillaArson= mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(43.720887, 7.253013))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.photo))
                    .title("Bord arrière batiment rouge de la Villa Arson")
                    .snippet("L'arrière rouge")
            );
            Marker markerGrandPlateauVillaArson= mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(43.720976, 7.253480))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.photo))
                    .title("Grand plateau gauche Villa Arson")
                    .snippet("Grand espace bétonné")
            );
            Marker markerTerrasseFinaleVillaArson= mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(43.719751, 7.253722))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.photo))
                    .title("Terrasse finale avec pyramide aztec Villa Arson")
                    .snippet("Pyramide aztec")
            );

            markers.add(markerParking);
            markers.add(markerPingPong);
            markers.add(markerBordGaucheNewton);
            markers.add(markerPasserelleNewton);
            markers.add(markerAdministrationMIAGE);
            markers.add(markerEntreeVillaArson);
            markers.add(markerPinedeVillaArson);
            markers.add(markerPareterreVillaArson);
            markers.add(markerAncienBatimentVillaArson);
            markers.add(markerEntreeRougeVillaArson);
            markers.add(markerBordGaucheVillaArson);
            markers.add(markerArriereVillaArson);
            markers.add(markerGrandPlateauVillaArson);
            markers.add(markerTerrasseFinaleVillaArson);

            //locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
            // Launch the Geolocation loop with a forced timeout
            /*timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (ActivityCompat.checkSelfPermission(MapsFragment.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsFragment.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                        locManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locListener, looper);
                    else
                        return;
                }
            }, 0, intervalGeolocRefresh);

            //mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
            //Location l = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);*/

        }

    }

    private void addProximityAlerts(ArrayList<Double[]> positions, ArrayList<String> name) {
        int requestCode = 0;
        for (int i = 0; i < name.size(); i++) {
            Intent intent = new Intent(getActivity(), AlertReceiver.class);
            intent.putExtra("name", name.get(i));
            PendingIntent proximityIntent = PendingIntent.getBroadcast(getActivity(), requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locManager.addProximityAlert(
                        positions.get(i)[0], // the latitude of the central point of the alert region
                        positions.get(i)[1], // the longitude of the central point of the alert region
                        8, // the radius of the central point of the alert region, in meters
                        -1, // time for this proximity alert, in milliseconds, or -1 to     indicate no expiration
                        proximityIntent // will be used to generate an Intent to fire when entry to or exit from the alert region is detected
                );
                requestCode++;
            } else
                return;
        }
        IntentFilter filter = new IntentFilter("com.example.denis.funculture.activities");
        getActivity().registerReceiver(new AlertReceiver(), filter);
    }
    public void addPositionToWay(LatLng position){
        if(polylineOptions == null) {
            polylineOptions = new PolylineOptions().add(position);
            mMap.addPolyline(polylineOptions);
        } else {
            polylineOptions.add(position);
        }
        Log.d("MapsFragment" ,"new pos : " + position.toString() + " way size : " + way.size());
        way.add(position);
    }
    public ArrayList<Marker> getTabMarkers() {
        return this.tabMarkers;
    }





}
