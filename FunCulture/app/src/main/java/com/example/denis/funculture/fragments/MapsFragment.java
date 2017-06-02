package com.example.denis.funculture.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.View;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
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
import com.example.denis.funculture.component.localisation.MyPointOfInterest;
import com.example.denis.funculture.component.localisation.Path;
import com.example.denis.funculture.component.localisation.PointOfPath;
import com.example.denis.funculture.component.sensor.MyTimer;
import com.example.denis.funculture.component.sensor.Pedometer;
import com.example.denis.funculture.component.sensor.geoloc.AlertReceiver;
import com.example.denis.funculture.component.sensor.geoloc.MyLocationListener;
import com.example.denis.funculture.main.App;
import com.example.denis.funculture.main.MainActivity;
import com.example.denis.funculture.utils.MyResources;
import com.example.denis.funculture.utils.Util;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
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

    private Handler myHandler = new Handler();
    private int forwardTime = 5000;
    private int backwardTime = 5000;
    private SeekBar seekbar;
    private TextView tx1, tx2, tx3;

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
    private Path currentPath;

    public ArrayList<LatLng> way = new ArrayList<>();
    private String previousMarkerName = "";

    public static ArrayList<Double[]> zones = new ArrayList<Double[]>();
    public static ArrayList<String> nomsZones = new ArrayList<String>();
    public static ArrayList<Marker> markers = new ArrayList<Marker>();
    private HashMap<Marker, MyPointOfInterest> linkMap;

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
    protected String getTitle() {
        return MyResources.GPS;
    }


    @Override
    protected void init() {
        super.init();

        linkMap = new HashMap<>();
        SupportMapFragment mapFragment = new SupportMapFragment();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.map_fragment_frame, mapFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
        mapFragment.getMapAsync(this);

        controlleurAudio();

        if (this.pedometer == null) {
            this.pedometer = new Pedometer(getActivity());
            LinearLayout llPerdometer = (LinearLayout) contentView.findViewById(R.id.ll_pedometer_view);
            llPerdometer.addView(pedometer.getView());
        }

        if (this.timer == null) {
            this.timer = new MyTimer(getActivity());
            LinearLayout llTimer = (LinearLayout) contentView.findViewById(R.id.ll_timer_view);
            llTimer.addView(timer.getView());
        }

        this.pedometer.start();
        this.timer.start();
    }

    private void controlleurAudio() {
        btNext = (ImageView) contentView.findViewById(R.id.btNext);
        btPlay = (ImageView) contentView.findViewById(R.id.btPlay);
        btPrev = (ImageView) contentView.findViewById(R.id.btPrev);

        tx1 = (TextView) contentView.findViewById(R.id.textView2);
        tx2 = (TextView) contentView.findViewById(R.id.textView3);
        tx3 = (TextView) contentView.findViewById(R.id.textView4);
        tx3.setText("Song.mp3");


        seekbar = (SeekBar) contentView.findViewById(R.id.seekBar);
        seekbar.setClickable(false);

        BoutonsControlleurAudio();
    }

    private void BoutonsControlleurAudio() {
        btPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer == null) {
                    return;
                }
                if (mediaPlayer.isPlaying()) {
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
        if (mediaPlayer == null) {
            return;
        }
        int temp = (int) startTime;

        if ((temp - backwardTime) > 0) {
            startTime = startTime - backwardTime;
            mediaPlayer.seekTo((int) startTime);
            Toast.makeText(getActivity().getApplicationContext(), "You have Jumped backward 5 seconds", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Cannot jump backward 5 seconds", Toast.LENGTH_SHORT).show();
        }
    }

    private void SautAvantControlleurAudio() {
        if (mediaPlayer == null) {
            return;
        }
        int temp = (int) startTime;

        if ((temp + forwardTime) <= finalTime) {
            startTime = startTime + forwardTime;
            mediaPlayer.seekTo((int) startTime);
            Toast.makeText(getActivity().getApplicationContext(), "You have Jumped forward 5 seconds", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Cannot jump forward 5 seconds", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("NewApi")
    private void PauseControlleurAudio() {
        Toast.makeText(getActivity().getApplicationContext(), "Pausing sound", Toast.LENGTH_SHORT).show();
        if (mediaPlayer == null) {
            return;
        }
        mediaPlayer.pause();
        btPlay.setImageDrawable(Util.getMainActivity().getDrawable(R.drawable.play));
        btPlay.setEnabled(true);
    }

    @SuppressLint("NewApi")
    private void PlayControlleurAudio() {
        Toast.makeText(getActivity().getApplicationContext(), "Playing sound", Toast.LENGTH_SHORT).show();
        if (mediaPlayer == null) {
            return;
        }
        mediaPlayer.start();

        btPlay.setImageDrawable(Util.getMainActivity().getDrawable(R.drawable.pause));
        ;
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

        seekbar.setProgress((int) startTime);
        myHandler.postDelayed(UpdateSongTime, 100);
    }


    @Override
    public void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //locManager.removeUpdates(locListener);
            //mMap.setMyLocationEnabled(false);
        }
        //Toast.makeText(getActivity(), "OnPause()", Toast.LENGTH_SHORT).show();
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
        //Toast.makeText(getActivity(), "OnResume()", Toast.LENGTH_SHORT).show();
        Util.getMainActivity().swithQCMButtonVisibility();
        try{
            zoomOnMe();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(markers.size() > 0) {
            float zoomLevel = (float) 16.0; //This goes up to 21
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markers.get(0).getPosition(), zoomLevel));
        }
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
//        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            Util.checkPrivileges(getActivity(), MyResources.MY_PERMISSIONS_REQUEST_GEOLOCATION_FINE, MyResources.MY_PERMISSIONS_REQUEST_GEOLOCATION_COARSE);
//        } else {
        mMap.setMyLocationEnabled(true);
        PolylineOptions polylineOptions = new PolylineOptions()
                .width(25)
                .color(Color.BLUE)
                .geodesic(true);

        for (MyPointOfInterest poi : Util.getCurrentPath().getPointsOfInterest()) {
            zones.add(new Double[]{poi.getPoint().getLatLng().latitude,
                    poi.getPoint().getLatLng().longitude});
            nomsZones.add(poi.getName());
            mMap.addCircle(new CircleOptions()
                    .center(new LatLng(poi.getPoint().getLatLng().latitude,
                            poi.getPoint().getLatLng().longitude))
                    .radius(5)
                    .strokeColor(Color.BLUE)
                    .fillColor(Color.RED));

            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(poi.getPoint().getLatLng())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.photo))
                    .title(poi.getName())
                    .snippet(poi.getDescription())
            );
            markers.add(marker);
            linkMap.put(marker, poi);

            if(markers.size() > 0) {
                float zoomLevel = (float) 16.0; //This goes up to 21
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markers.get(0).getPosition(), zoomLevel));
            }
        }

        for (PointOfPath point : Util.getCurrentPath().getPoints()) {
            //Ajout à la polyline
            polylineOptions.add(point.getLatLng());
        }
        //Ajout polyline à la map
        mMap.addPolyline(polylineOptions);

        //addProximityAlerts(zones, nomsZones);

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

//        }

        ((MainActivity) getActivity()).setFabClicListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Context context = getContext();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final LatLng currentPoint = locListener.getMyposition();

                if (!locListener.getTrackingMode()) {
                    LinearLayout layout = new LinearLayout(context);
                    layout.setOrientation(LinearLayout.VERTICAL);

                    final EditText etId = new EditText(context);
                    etId.setHint("Id");
                    etId.setInputType(InputType.TYPE_CLASS_NUMBER);
                    layout.addView(etId);

                    final EditText etName = new EditText(context);
                    etName.setHint("Nom");
                    layout.addView(etName);

                    builder.setView(layout);
                    builder.setMessage(MyResources.CREATE_PATH)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    int pathId = Integer.parseInt(etId.getText().toString());
                                    String pathName = etName.getText().toString();
                                    currentPath = new Path(pathId, pathName);
                                    Util.createToast("new Path id : " + pathId + " name : " + pathName);
                                    locListener.setTrackingMode(true);
                                }
                            });
                    builder.show();
                }
                if (locListener.getTrackingMode()) {
                    LinearLayout layout = new LinearLayout(context);
                    layout.setOrientation(LinearLayout.VERTICAL);

                    TextView tvLat = new TextView(context);
                    tvLat.setText("Latitude : ");
                    layout.addView(tvLat);

                    final EditText etLat = new EditText(context);
                    etLat.setInputType(InputType.TYPE_CLASS_NUMBER);
                    etLat.setText(Double.toString(currentPoint.latitude));
                    layout.addView(etLat);

                    TextView tbLng = new TextView(context);
                    tbLng.setText("Longitude : ");
                    layout.addView(tbLng);

                    final EditText etLng = new EditText(context);
                    etLng.setInputType(InputType.TYPE_CLASS_NUMBER);
                    etLng.setText(Double.toString(currentPoint.longitude));
                    layout.addView(etLng);

                    builder.setView(layout);
                    builder.setMessage(MyResources.ADD_POINT)
                            .setNegativeButton("Ajouter au chemin", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    double lat = Double.parseDouble(etLat.getText().toString());
                                    double lng = Double.parseDouble(etLng.getText().toString());
                                    currentPath.addPoint(new LatLng(lat, lng));
                                    Util.createToast("Add lat : " + lat + " lng : " + lng);
                                }
                            })
                            .setNeutralButton("Annuler", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            })
                            .setPositiveButton("Sauvegarder chemin", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    savePath();
                                    locListener.setTrackingMode(false);
                                }
                            });
                    builder.show();
                    MapsFragment.this.addPositionToWay(locListener.getMyposition());
                }

            }
        });

        zoomOnMe();
    }

    private void zoomOnMe() {
    }

    private void savePath() {
        if (currentPath == null) {
            return;
        }

        currentPath.saveOnServer();
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

    public void addPositionToWay(LatLng position) {
        if (way != null && way.size() != 0) {
            Polyline polygon = mMap.addPolyline(new PolylineOptions()
                    .add(way.get(way.size() - 1), position)
                    .width(5)
                    .color(Color.RED)
                    .geodesic(true));
        }
        Log.d("MapsFragment", "new pos : " + position.toString() + " way size : " + way.size());
        way.add(position);
    }

    public ArrayList<Marker> getTabMarkers() {
        return this.tabMarkers;
    }


    /**
     * Called when the user clicks a marker.
     */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        if (this.previousMarkerName.equals(marker.getTitle())) {
            final MyPointOfInterest poi = linkMap.get(marker);

            if (poi == null) {
                return true;
            }
            if (poi.getEpreuve() != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(MyResources.LAUNCH_EPREUVE)
                        .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                poi.getEpreuve().launch();
                            }
                        });
                builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
            }
            this.previousMarkerName = marker.getTitle();
            return true;
        }

        marker.showInfoWindow();
        int son = LancerSon(marker);

        Log.d("previousMarkerName", this.previousMarkerName);
        Log.d("actualMarkerName", marker.getTitle());
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            mediaPlayer = MediaPlayer.create(getActivity(), son);
            myHandler.postDelayed(UpdateSongTime, 100);
            PlayControlleurAudio();
            mediaPlayer.start();
        } else if (mediaPlayer != null && mediaPlayer.getCurrentPosition() != 0) {
            myHandler.postDelayed(UpdateSongTime, 100);
            PlayControlleurAudio();
            mediaPlayer.start();
        } else {
            mediaPlayer = MediaPlayer.create(getActivity(), son);
            myHandler.postDelayed(UpdateSongTime, 100);
            PlayControlleurAudio();
            mediaPlayer.start();
        }

        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);
        }
        this.previousMarkerName = marker.getTitle();
        return true;
    }

    private int LancerSon(Marker marker) {
        // Check which maker have been clicked or approched
        int son;
        switch (marker.getTitle()) {
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
                if (marker.getSnippet().contains("o"))
                    son = R.raw.abr;
                else
                    son = R.raw.song;
                break;
        }
        return son;
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            if (mediaPlayer == null) {
                return;
            }
            startTime = mediaPlayer.getCurrentPosition();
            tx1.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))
            );
            seekbar.setProgress((int) startTime);
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
