package com.example.denis.funculture.main;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.denis.funculture.R;
import com.example.denis.funculture.fragments.Accueil;
import com.example.denis.funculture.fragments.ChooseSensorFragment;
import com.example.denis.funculture.fragments.Inscription;
import com.example.denis.funculture.fragments.MapsFragment;
import com.example.denis.funculture.fragments.MyFragment;
import com.example.denis.funculture.fragments.QCMFragment;
import com.example.denis.funculture.fragments.SeConnecter;
import com.example.denis.funculture.utils.MyResources;
import com.example.denis.funculture.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String FRAGMENT_TAG = "fragmentTag";
    private Toolbar toolbar;
    private NavigationView navigationView;
    private FloatingActionButton fab;
    private FloatingActionButton fab2;
    private List<MyFragment> fragments = new ArrayList<>();
    private MyFragment homeFragment;
    private MapsFragment mapFragment;

    public void setFabClicListener(View.OnClickListener listener) {
        this.fab.setOnClickListener(listener);
    }

    public void swithQCMButtonVisibility() {
        if(this.fab2.getVisibility() == View.VISIBLE) {
            this.fab2.setVisibility(View.INVISIBLE);
            this.fab.setVisibility(View.INVISIBLE);
        } else {
            this.fab2.setVisibility(View.VISIBLE);
            this.fab.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getSingleton().setCurrentActivity(this);
        setContentView(R.layout.activity_main);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.fab = (FloatingActionButton) findViewById(R.id.fab);
        this.fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        this.fab2.setVisibility(View.INVISIBLE);
        this.fab.setVisibility(View.INVISIBLE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Ici on peut mettre un comportement, on verra lequel", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        this.fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFragment(QCMFragment.class);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        this.navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        init();

        Util.setMainActivity(this);
        Util.checkPrivileges(this, MyResources.MY_PERMISSIONS_REQUEST_GEOLOCATION_FINE, MyResources.MY_PERMISSIONS_REQUEST_GEOLOCATION_COARSE);
        startFragment(ChooseSensorFragment.class);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_accueil) {

            startFragment(Accueil.class);

        } else if (id == R.id.nav_map) {

            startFragment(MapsFragment.class);

        } else if (id == R.id.nav_desc) {

            startFragment(ChooseSensorFragment.class);

        } else if (id == R.id.nav_legende) {

            startFragment(ChooseSensorFragment.class);

        } else if (id == R.id.nav_inscription) {

            startFragment(Inscription.class);

        } else if (id == R.id.nav_connexion) {

            startFragment(SeConnecter.class);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    //ici on fera toutes les actions nécessaires au démarrage de l'app
    private void init() {
        App.getSingleton().setContext(this);
    }


    public void startFragment(final Class<? extends MyFragment> fragmentClass) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    MyFragment fragment = fragmentClass.newInstance();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_frame, fragment, FRAGMENT_TAG);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.addToBackStack(null);
                    ft.commit();

                    if(fragmentClass == MapsFragment.class) {
                        mapFragment = (MapsFragment) fragment;
                    }
                    if(homeFragment == null) {
                        homeFragment = fragment;
                    } else {
                        fragments.add(fragment);
                    }

                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setNavTitle(String title) {
        this.toolbar.setTitle(title);
    }

    //Comportement lorsque que l'on ferme un fragment
    public void removeLastFragment() {
        if(this.fragments.size() < 1) {
            return;
        }

        //On le retire de la liste
        fragments.remove(fragments.size() -1);
    }

    public void closeCurrentFragment(Fragment fragmentToClose, boolean restoreMap) {
        fragments.remove(fragmentToClose);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if(restoreMap && mapFragment != null) {
            ft.replace(R.id.fragment_frame, mapFragment, FRAGMENT_TAG);
        } else {
            ft.replace(R.id.fragment_frame, homeFragment, FRAGMENT_TAG);
        }


        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.remove(fragmentToClose);

        ft.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(fragments.size() > 0) {
                return super.onKeyDown(keyCode, event);
            } else {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                finish();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(MyResources.LEAVE_APP).setPositiveButton(MyResources.YES, dialogClickListener)
                        .setNegativeButton(MyResources.NO, dialogClickListener).show();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
