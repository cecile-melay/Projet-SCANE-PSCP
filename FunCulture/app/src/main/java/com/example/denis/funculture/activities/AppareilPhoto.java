package com.example.denis.funculture.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.denis.funculture.R;
import com.example.denis.funculture.fragments.CameraSurface;
import com.example.denis.funculture.fragments.SavePhoto;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppareilPhoto extends Activity {

    private Context contexte;
    private Camera maCamera;
    private PictureCallback maPhoto;
    private CameraSurface cs;
    private LinearLayout llayout;
    private Button capturer, switcher;
    private boolean cf = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        contexte = this.getBaseContext();
        initialize();
    }


    // Demande l'autorisation d'utiliser l'appareil photo au 1er démarrage
    @Override
    protected void onStart() {
        super.onStart();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int hasCameraPermission = checkSelfPermission(Manifest.permission.CAMERA);
            int hasSDcardWritePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int hasSDcardReadPermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);

            List<String> permissions = new ArrayList<String>();

            if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.CAMERA);

            }


            if (hasSDcardWritePermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

            }

            if (hasSDcardReadPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);

            }

            if (!permissions.isEmpty()) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), 111);
            }

        }

    }

    // Rappel pour le résultat de la demande d'autorisations.
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 111: {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        System.out.println("Permissions --> " + "Permission Accepté: " + permissions[i]);


                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        System.out.println("Permissions --> " + "Permission Refusé: " + permissions[i]);

                    }
                }
            }
            break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (!hasCamera(contexte)) {
            Toast toast = Toast.makeText(contexte, "Aucun appareil photo détecté sur votre téléphone.", Toast.LENGTH_LONG);
            toast.show();
            finish();
        }
        if (maCamera == null) {
            if ( CameraFrontaleDispo() < 0) {
                Toast.makeText(this, "Aucun appareil photo frontal détecté.", Toast.LENGTH_LONG).show();
                //si aucune camera avant detecté, ne pas afficher le bouton switch
                switcher.setVisibility(View.GONE);
            }
            if (maCamera == null) {
                if (CameraArriereDispo() < 0) {
                    Toast.makeText(this, "Aucun appareil photo arrière détecté.", Toast.LENGTH_LONG).show();
                    //si aucune camera aRRIERE detecté, ne pas afficher le bouton switch
                    switcher.setVisibility(View.GONE);
                }
            }
            maCamera = Camera.open(CameraArriereDispo());
            maPhoto = getPictureCallback();
            cs.refreshCamera(maCamera);
        }
    }


    //Vérifier si le téléphone a un ou +sieurs appareil(s)
    private boolean hasCamera(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    /// Vérifie que l'appareil est doté d'une caméra avant
    private int CameraFrontaleDispo() {
        int dispo = -1;
        int nbCamera = Camera.getNumberOfCameras();
        for (int i = 0; i < nbCamera ; i++) {
            CameraInfo info = new CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
                dispo = i;
                cf = true;
            }
        }
        return dispo;
    }


    /// Vérifie que l'appareil est doté d'une caméra avant
    private int CameraArriereDispo() {
        int dispo = -1;
        int nbCamera = Camera.getNumberOfCameras();
        for (int i = 0; i < nbCamera; i++) {
            CameraInfo info = new CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == CameraInfo.CAMERA_FACING_BACK) {
                dispo = i;
                cf = false;
            }
        }
        return dispo;
    }


    private void releaseCamera() {
        if (maCamera != null) {
            maCamera.release();
            maCamera = null;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        //libèrer la camera pour qu'elle puisse etre utilisé dans d'autre application
        releaseCamera();
    }

    ///Initialisation
    public void initialize() {
        switcher = (Button) findViewById(R.id.button_change);
        switcher.setOnClickListener(switchCameraListener);
        capturer = (Button) findViewById(R.id.button_capture);
        capturer.setOnClickListener(captrureListener);
        llayout = (LinearLayout) findViewById(R.id.camera_surface);
        cs= new CameraSurface(contexte, maCamera);
        llayout.addView(cs);


    }

    public void choixCamera() {
        if (cf) {
            int camDefaut = CameraArriereDispo();
            if (camDefaut >= 0) {
                maCamera = Camera.open(camDefaut);
                maPhoto = getPictureCallback();
                cs.refreshCamera(maCamera);
            }
        } else {
            int camDefaut = CameraFrontaleDispo();
            if (camDefaut >= 0) {
                maCamera = Camera.open(camDefaut);
                maPhoto = getPictureCallback();
                cs.refreshCamera(maCamera);
            }
        }
    }

    OnClickListener switchCameraListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            //nb de camera
            int nbCamera = maCamera.getNumberOfCameras();
            if (nbCamera > 1) {
                releaseCamera();
                choixCamera();
                Toast toast = Toast.makeText(contexte, "Changement d'appareil photo", Toast.LENGTH_LONG);
                toast.show();
            } else {
                Toast toast = Toast.makeText(contexte, "Unique appareil photo du télphone.", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    };


    private static File getPhotoFile() {
        //créer un répertoire special pour les photos prise avec notre appli
        File repertoire = new File("/sdcard/", "Projet SCANE");
        if (!repertoire.exists()) {
            if (!repertoire.mkdirs()) {
                return null;

            }
        }
        //recuperer date + heure
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File photoFile;
        //donner un nom unique au fichier photo
        photoFile = new File(repertoire.getPath() + File.separator + "SCANE_" + timeStamp + ".jpg");

        return photoFile;
    }


    private PictureCallback getPictureCallback() {
        final PictureCallback picture = new PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                File photoFile = getPhotoFile();

                if (photoFile == null) {
                    Toast to = Toast.makeText(contexte,"Impossible d'acceder à la carte SD", Toast.LENGTH_LONG);
                    return;


                }
                try {
                    FileOutputStream fos = new FileOutputStream(photoFile);
                    fos.write(data);
                    fos.close();
                    new SavePhoto(contexte,photoFile);
                    Toast toast = Toast.makeText(contexte, "Photo engeristré : " + photoFile.getName(), Toast.LENGTH_LONG);
                    toast.show();

                } catch (FileNotFoundException e) {
                } catch (IOException e) {
                }

                cs.refreshCamera(maCamera);
            }
        };
        return picture;
    }

    OnClickListener captrureListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            maCamera.startPreview();
            maCamera.takePicture(null, null, maPhoto);
        }

    };

}
