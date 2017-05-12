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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.denis.funculture.R;
import com.example.denis.funculture.component.User;
import com.example.denis.funculture.fragments.Accueil;
import com.example.denis.funculture.fragments.ChooseSensorFragment;
import com.example.denis.funculture.fragments.ChooseTags;
import com.example.denis.funculture.fragments.EditProfil;
import com.example.denis.funculture.fragments.MapsFragment;
import com.example.denis.funculture.fragments.MyFragment;
import com.example.denis.funculture.fragments.QCMFragment;
import com.example.denis.funculture.fragments.SeConnecter;
import com.example.denis.funculture.utils.MyResources;
import com.example.denis.funculture.utils.MyServices;
import com.example.denis.funculture.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String FRAGMENT_TAG = "fragmentTag";
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private FloatingActionButton fab;
    private FloatingActionButton fab2;
    private List<MyFragment> fragments = new ArrayList<>();
    private MyFragment homeFragment;
    private MapsFragment mapFragment;

    //Register Fields
    private EditText etSecondName;
    private EditText etFirstName;
    private EditText etBirth;
    private EditText etVille;
    private EditText etMail;
    private EditText etPass;
    private EditText etPseudo;
    private EditText etFc;
    private Spinner spLevel;
    private Button btRegister;
    private LinearLayout llRegister;
    private boolean isRegisterOpen = false;

    //Nav user infos
    private ImageView ivUser;
    private TextView tvUserPseudo;
    private TextView tvUserName;
    private LinearLayout llUserInfos;

    public void setFabClicListener(View.OnClickListener listener) {
        this.fab.setOnClickListener(listener);
    }

    public void swithQCMButtonVisibility() {
        if (this.fab2.getVisibility() == View.VISIBLE) {
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

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        this.navigationView = (NavigationView) findViewById(R.id.nav_view);
        tvUserPseudo = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_user_pseudo);
        tvUserName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_user_name);
        llUserInfos = (LinearLayout) navigationView.getHeaderView(0).findViewById(R.id.ll_user_infos);
        llUserInfos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.getCurrentUser() == null) {
                    return;
                }

                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                startFragment(EditProfil.class);
            }
        });
        ivUser = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.iv_user);
        navigationView.setNavigationItemSelectedListener(this);

        init();
        Util.checkPrivileges(this, MyResources.MY_PERMISSIONS_REQUEST_GEOLOCATION_FINE, MyResources.MY_PERMISSIONS_REQUEST_GEOLOCATION_COARSE);
        startFragment(ChooseSensorFragment.class);
        initRegisterFields();
    }

    private void initRegisterFields() {
        this.llRegister = (LinearLayout) findViewById(R.id.ll_register);
        this.etFirstName = (EditText) findViewById(R.id.et_first_name);
        this.etSecondName = (EditText) findViewById(R.id.et_second_name);
        this.etBirth = (EditText) findViewById(R.id.et_birth);
        this.etVille = (EditText) findViewById(R.id.et_ville);
        this.etMail = (EditText) findViewById(R.id.et_mail);
        this.etPass = (EditText) findViewById(R.id.et_pass);
        this.etPseudo = (EditText) findViewById(R.id.et_pseudo);
        this.etFc = (EditText) findViewById(R.id.et_fc);
        this.spLevel = (Spinner) findViewById(R.id.sp_level);
        this.btRegister = (Button) findViewById(R.id.bt_register);

        this.btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateRegister();
            }
        });

        String[] sportLevels = new String[]{"Niveau sportif", "Débutant", "Intermédiaire", "Confirmé"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, sportLevels);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item_drop);
        this.spLevel.setAdapter(spinnerArrayAdapter);
    }

    private void validateRegister() {
        if (Util.isEmpty(etFirstName)
                || Util.isEmpty(etSecondName)
                || Util.isEmpty(etBirth)
                || Util.isEmpty(etMail)
                || Util.isEmpty(etVille)
                || Util.isEmpty(etPass)
                || Util.isEmpty(etFc)
                || Util.isEmpty(etPseudo)
                || spLevel.getSelectedItemPosition() == 0) {
            Util.createDialog(MyResources.MISSING_FIELD_WARNING);
        } else {
            Util.setCurrentUser(new User(etFirstName.getText().toString(),
                    etSecondName.getText().toString(),
                    etBirth.getText().toString(),
                    spLevel.getSelectedItemPosition(),
                    Integer.parseInt(etFc.getText().toString()),
                    etVille.getText().toString(),
                    etMail.getText().toString(),
                    etPass.getText().toString(),
                    etPseudo.getText().toString()));

            MyServices.getSingleton().insertUser(Util.getCurrentUser());
            Util.createDialog(MyResources.SUCCESS_REGISTER);
            llRegister.setVisibility(View.GONE);
            isRegisterOpen = false;
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_accueil) {

            startFragment(Accueil.class);

        } else if (id == R.id.nav_choose_tag) {

            startFragment(ChooseTags.class);

        } else if (id == R.id.nav_map) {

            startFragment(MapsFragment.class);

        } else if (id == R.id.nav_desc) {

            startFragment(ChooseSensorFragment.class);

        } else if (id == R.id.nav_legende) {

            startFragment(ChooseSensorFragment.class);

        } else if (id == R.id.nav_inscription) {
            showRegisterLayout();
        } else if (id == R.id.nav_connexion) {
            startFragment(SeConnecter.class);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showRegisterLayout() {
        this.llRegister.setVisibility(View.VISIBLE);
        this.isRegisterOpen = true;
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
        Util.setMainActivity(this);
        MyServices.getSingleton().loadPaths();
    }


    public void startFragment(final Class<? extends MyFragment> fragmentClass) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (llRegister != null) {
                        llRegister.setVisibility(View.GONE);
                    }
                    MyFragment fragment = fragmentClass.newInstance();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_frame, fragment, FRAGMENT_TAG);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.addToBackStack(null);
                    ft.commit();

                    if (fragmentClass == MapsFragment.class) {
                        mapFragment = (MapsFragment) fragment;
                    }
                    if (homeFragment == null) {
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
        if (this.fragments.size() < 1) {
            return;
        }

        //On le retire de la liste
        fragments.remove(fragments.size() - 1);
    }

    public void closeCurrentFragment(Fragment fragmentToClose, boolean restoreMap) {
        fragments.remove(fragmentToClose);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (restoreMap && mapFragment != null) {
            Util.setCurrentFragment(mapFragment);
            ft.replace(R.id.fragment_frame, mapFragment, FRAGMENT_TAG);
        } else {
            Util.setCurrentFragment(homeFragment);
            ft.replace(R.id.fragment_frame, homeFragment, FRAGMENT_TAG);
        }


        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.remove(fragmentToClose);

        ft.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }

            if (isRegisterOpen) {
                llRegister.setVisibility(View.GONE);
                isRegisterOpen = false;
                return false;
            }

            if (fragments.size() > 0) {
                return super.onKeyDown(keyCode, event);
            } else {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
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

    public void setCurrentUser(User currentUser) {
        ivUser.setVisibility(View.VISIBLE);
        tvUserPseudo.setText(currentUser.getPseudo());
        tvUserName.setText(currentUser.getPrenom() + " " + currentUser.getNom());
    }
}
