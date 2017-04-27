package com.example.denis.funculture.fragments;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.View;

import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.concurrent.TimeUnit;
import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.LinearLayout;

import com.example.denis.funculture.R;
import com.example.denis.funculture.component.sensor.MyTimer;
import com.example.denis.funculture.component.sensor.Pedometer;
import com.example.denis.funculture.component.sensor.geoloc.AlertReceiver;
import com.example.denis.funculture.component.sensor.geoloc.MyLocationListener;
import com.example.denis.funculture.main.App;
import com.example.denis.funculture.main.MainActivity;
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


/*
 * Main Activity which contains the Google Map
 */
public class MapsFragment extends MyFragment implements GoogleMap.OnMarkerClickListener,
                                                        OnMapReadyCallback {

    private MediaPlayer mediaPlayer;
    private GoogleMap mMap;

    private ImageView btNext;
    private ImageView btPlay;
    private ImageView btPrev;

    private double startTime = 0;
    private double finalTime = 0;
    private int nbPointChemin = 10;

    private Handler myHandler = new Handler();;
    private int forwardTime = 5000;
    private int backwardTime = 5000;
    private SeekBar seekbar;
    private TextView tx1,tx2,tx3;

    public static int oneTimeOnly = 0;


    private MyTimer timer;
    PolylineOptions polylineOptions;
    private LocationManager locManager;
    private MyLocationListener locListener;
    private Pedometer pedometer;
    private Looper looper;
    private int intervalGeolocRefresh = 3000;
    private Location startLocation;
    private ArrayList<Marker> tabMarkers = new ArrayList<Marker>();

    public ArrayList<LatLng> way = new ArrayList<LatLng>();
    private String previousMarkerName = "";

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

        controlleurAudio();

        if(this.pedometer == null) {
            this.pedometer = new Pedometer(getActivity());
            LinearLayout llPerdometer = (LinearLayout) contentView.findViewById(R.id.ll_pedometer_view);
            llPerdometer.addView(pedometer.getView());
        }

        if(this.timer == null) {
            this.timer = new MyTimer(getActivity());
            LinearLayout llTimer = (LinearLayout) contentView.findViewById(R.id.ll_timer_view);
            llTimer.addView(timer.getView());
        }

        this.pedometer.start();
        this.timer.start();
    }

    private void controlleurAudio() {
        btNext = (ImageView) contentView.findViewById(R.id.btNext);
        btPlay = (ImageView)contentView.findViewById(R.id.btPlay);
        btPrev = (ImageView)contentView.findViewById(R.id.btPrev);

        tx1 = (TextView)contentView.findViewById(R.id.textView2);
        tx2 = (TextView)contentView.findViewById(R.id.textView3);
        tx3 = (TextView)contentView.findViewById(R.id.textView4);
        tx3.setText("Song.mp3");


        seekbar = (SeekBar)contentView.findViewById(R.id.seekBar);
        seekbar.setClickable(false);

        BoutonsControlleurAudio();
    }

    private void BoutonsControlleurAudio() {
        btPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer== null) {
                    return;
                }
                if(mediaPlayer.isPlaying()) {
                    PauseControlleurAudio();
                } else {
                    PlayControlleurAudio();
                }
            }
        });
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SautAvantControlleurAudio();
            }
        });
        btPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SautArriereControlleurAudio();
            }
        });
    }

    private void SautArriereControlleurAudio() {
        if(mediaPlayer == null) {
            return;
        }
        int temp = (int)startTime;

        if((temp-backwardTime)>0){
            startTime = startTime - backwardTime;
            mediaPlayer.seekTo((int) startTime);
            Toast.makeText(getActivity().getApplicationContext(),"You have Jumped backward 5 seconds",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getActivity().getApplicationContext(),"Cannot jump backward 5 seconds",Toast.LENGTH_SHORT).show();
        }
    }

    private void SautAvantControlleurAudio() {
        if(mediaPlayer == null) {
            return;
        }
        int temp = (int)startTime;

        if((temp+forwardTime)<=finalTime){
            startTime = startTime + forwardTime;
            mediaPlayer.seekTo((int) startTime);
            Toast.makeText(getActivity().getApplicationContext(),"You have Jumped forward 5 seconds",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getActivity().getApplicationContext(),"Cannot jump forward 5 seconds",Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("NewApi")
    private void PauseControlleurAudio() {
        Toast.makeText(getActivity().getApplicationContext(), "Pausing sound",Toast.LENGTH_SHORT).show();
        if(mediaPlayer == null) {
            return;
        }
        mediaPlayer.pause();
        btPlay.setImageDrawable(Util.getMainActivity().getDrawable(R.drawable.play));
        btPlay.setEnabled(true);
    }

    @SuppressLint("NewApi")
    private void PlayControlleurAudio() {
        Toast.makeText(getActivity().getApplicationContext(), "Playing sound",Toast.LENGTH_SHORT).show();
        if(mediaPlayer == null) {
            return;
        }
        mediaPlayer.start();

        btPlay.setImageDrawable(Util.getMainActivity().getDrawable(R.drawable.pause));;
        finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();

        if (oneTimeOnly == 0) {
            seekbar.setMax((int) finalTime);
            oneTimeOnly = 1;
        }

        tx2.setText(String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                finalTime)))
        );

        tx1.setText(String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                startTime)))
        );

        seekbar.setProgress((int)startTime);
        myHandler.postDelayed(UpdateSongTime,100);
    }


    @Override
    public void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //locManager.removeUpdates(locListener);
            //mMap.setMyLocationEnabled(false);
        }
        Toast.makeText(getActivity(), "OnPause()", Toast.LENGTH_SHORT).show();
        Util.getMainActivity().swithQCMButtonVisibility();
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
        Util.getMainActivity().swithQCMButtonVisibility();
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
        mMap.setOnMarkerClickListener(this);
        // Check Geolocation permission
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Util.checkPrivileges(getActivity(), MyResources.MY_PERMISSIONS_REQUEST_GEOLOCATION_FINE, MyResources.MY_PERMISSIONS_REQUEST_GEOLOCATION_COARSE);
        } else {

            mMap.setMyLocationEnabled(true);

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
            // zones route NEWTON
            zones.add(new Double[]{43.625312, 7.049848});
            nomsZones.add("Route Newton 1");
            zones.add(new Double[]{43.625738, 7.050184});
            nomsZones.add("Route Newton 2");
            zones.add(new Double[]{43.626132, 7.051074});
            nomsZones.add("Route Newton 3");
            zones.add(new Double[]{43.626344, 7.051955});
            nomsZones.add("Route Newton 4");

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

            // Test route Newton
            Marker markerRouteNewton1 = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(43.625312, 7.049848))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.photo))
                    .title("Route Newton 1")
                    .snippet("Route Newton 1")
            );
            Marker markerRouteNewton2 = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(43.625738, 7.050184))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.photo))
                    .title("Route Newton 2")
                    .snippet("Route Newton 2")
            );
            Marker markerRouteNewton3 = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(43.626132, 7.051074))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.photo))
                    .title("Route Newton 3")
                    .snippet("Route Newton 3")
            );
            Marker markerRouteNewton4 = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(43.626344, 7.051955))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.photo))
                    .title("Route Newton 4")
                    .snippet("Route Newton 4")
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
            markers.add(markerRouteNewton1);
            markers.add(markerRouteNewton2);
            markers.add(markerRouteNewton3);
            markers.add(markerRouteNewton4);

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

            // Chemin MIAGE
            Polyline polygonMIAGE = mMap.addPolyline(new PolylineOptions()
                    .add(new LatLng(markerRouteNewton1.getPosition().latitude, markerRouteNewton1.getPosition().longitude),
                            new LatLng(markerRouteNewton2.getPosition().latitude, markerRouteNewton2.getPosition().longitude),
                            new LatLng(markerRouteNewton3.getPosition().latitude, markerRouteNewton3.getPosition().longitude),
                            new LatLng(markerRouteNewton4.getPosition().latitude, markerRouteNewton4.getPosition().longitude))
                    .width(25)
                    .color(Color.BLUE)
                    .geodesic(true));

            // Chemin NEWTON
            Polyline polygonNEWTON = mMap.addPolyline(new PolylineOptions()
                    .add(new LatLng(43.616824, 7.064771), new LatLng(43.617156, 7.063899), new LatLng(43.617465, 7.063620),
                            new LatLng(43.617447, 7.063478), new LatLng(43.617005, 7.063652))
                    .width(25)
                    .color(Color.BLUE)
                    .geodesic(true));

        }
/*
Pour les tests je vais faire que le mode tracking passe à false automatiquement quand on a 10 points dans le chemin
 */
        ((MainActivity) getActivity()).setFabClicListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(locListener.getTrackingMode()) {
                    if(MapsFragment.this.way.size()<= nbPointChemin) {
                        MapsFragment.this.addPositionToWay(locListener.getMyposition());
                        Toast.makeText(getActivity(), "Point sauvegardé " +
                                "" + locListener.getMyposition().toString(), Toast.LENGTH_SHORT).show();
                    }else{
                        locListener.setTrackingMode(false);
                        Toast.makeText(getActivity(), "Tracking mode"+locListener.getTrackingMode().toString(), Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

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
        if(way != null && way.size()!=0){
            Polyline polygon = mMap.addPolyline(new PolylineOptions()
                    .add(way.get(way.size()-1), position)
                    .width(5)
                    .color(Color.RED)
                    .geodesic(true));
        }
        Log.d("MapsFragment" ,"new pos : " + position.toString() + " way size : " + way.size());
        way.add(position);
    }
    public ArrayList<Marker> getTabMarkers() {
        return this.tabMarkers;
    }


    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {

        marker.showInfoWindow();
        int son = LancerSon(marker);

        Log.e("previousMarkerName", this.previousMarkerName);
        Log.e("actualMarkerName", marker.getTitle());

        // Avoid sound repetition for auto start sound
        if(!this.previousMarkerName.equals(marker.getTitle())) {
            if (mediaPlayer != null) {
                mediaPlayer.pause();
                mediaPlayer = MediaPlayer.create(getActivity(), son);
                myHandler.postDelayed(UpdateSongTime,100);
                PlayControlleurAudio();
                mediaPlayer.start();
            } else if (mediaPlayer != null && mediaPlayer.getCurrentPosition() != 0){
                myHandler.postDelayed(UpdateSongTime,100);
                PlayControlleurAudio();
                mediaPlayer.start();
            } else {
                mediaPlayer = MediaPlayer.create(getActivity(), son);
                myHandler.postDelayed(UpdateSongTime,100);
                PlayControlleurAudio();
                mediaPlayer.start();
            }
        }
        else
            {
                // TO DO : ASK IF I WANT TO REPEAT THE SOUND
            }

        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);


            // Return false to indicate that we have not consumed the event and that we wish
            // for the default behavior to occur (which is for the camera to move such that the
            // marker is centered and for the marker's info window to open, if it has one).
            return false;
        }
        this.previousMarkerName = marker.getTitle();
        return true;
    }

    private int LancerSon(Marker marker) {
        // Check which maker have been clicked or approched
        int son = 0;
        switch(marker.getTitle()) {
            case ("Ping Pong"):
                son = R.raw.ping;
                break;
            case ("Parking"):
                son = R.raw.parking;
                break;
            case ("Administration"):
                son = R.raw.administration;
                break;
            case ("Route Newton 1"):
                son = R.raw.entreenewton;
                break;
            case ("Route Newton 2"):
                son = R.raw.newton2;
                break;
            case ("Route Newton 3"):
                son = R.raw.newton3;
                break;
            case ("Route Newton 4"):
                son = R.raw.finnewton;
                break;
            default:
                if(marker.getSnippet().contains("o"))
                    son = R.raw.abr;
                else
                    son = R.raw.song;
                break;
        }
        return son;
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            if(mediaPlayer == null) {
                return;
            }
            startTime = mediaPlayer.getCurrentPosition();
            tx1.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))
            );
            seekbar.setProgress((int)startTime);
            myHandler.postDelayed(this, 100);
        }
    };
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
