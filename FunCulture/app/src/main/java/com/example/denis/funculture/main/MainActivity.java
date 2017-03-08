package com.example.denis.funculture.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.denis.funculture.R;
import com.example.denis.funculture.activities.AccelerometerActivity;
import com.example.denis.funculture.activities.MapsActivity;
import com.example.denis.funculture.activities.PedometerActivity;
import com.example.denis.funculture.component.sensor.Pedometer;
import com.example.denis.funculture.utils.MyResources;
import com.example.denis.funculture.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    Spinner senssorSpinner;
    Button testButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Ici on peut mettre un comportement, on verra lequel", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        init();

        Util.checkPrivileges(this, MyResources.MY_PERMISSIONS_REQUEST_GEOLOCATION_FINE, MyResources.MY_PERMISSIONS_REQUEST_GEOLOCATION_COARSE);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //ici on fera toutes les actions nécessaires au démarrage de l'app
    private void init() {
        App.setContext(getApplicationContext());

        findViews();
        fillSensorSpinner();
    }

    //ici on récupère les vues de notre layout xml
    private void findViews() {
        this.senssorSpinner = (Spinner) findViewById(R.id.sensorSpinner);
        this.testButton = (Button) findViewById(R.id.testButton);
        this.testButton.setOnClickListener(this);
    }

    //ici on ajoute les capteurs au spinner
    private void fillSensorSpinner() {
        List<String> spinnerArray =  new ArrayList<>();
        spinnerArray.add(MyResources.GPS);
        spinnerArray.add(MyResources.ACCELEROMETER);
        spinnerArray.add(MyResources.PEDOMETER);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, spinnerArray);
        this.senssorSpinner.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if(v == this.testButton) {
            if(senssorSpinner != null && senssorSpinner.getSelectedItem() != null) {
                int position = senssorSpinner.getSelectedItemPosition();
                Intent intent;

                switch (position) {
                    //GPS
                    case 0 :
                        intent = new Intent(this, MapsActivity.class);
                        this.startActivity(intent);
                        break;

                    //ACCELEROMETER
                    case 1 :
                        intent = new Intent(this, AccelerometerActivity.class);
                        this.startActivity(intent);
                        break;

                    //PEDOMETER
                    case 2 :
                        intent = new Intent(this, PedometerActivity.class);
                        this.startActivity(intent);
                        break;
                }
            }
        }
    }
}
