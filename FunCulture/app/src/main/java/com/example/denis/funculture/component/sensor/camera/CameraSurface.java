package com.example.denis.funculture.component.sensor.camera;

/**
 * Created by Rihab on 08/03/17.
 */
import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

public class CameraSurface extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder sh;
    private Camera maCamera;

    public CameraSurface(Context context, Camera camera) {
        super(context);
        maCamera = camera;
        sh = getHolder();
        sh.addCallback(this);
        sh.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    //initialisation de la vue surface créée. definition  d'un aperçu de l'affichage d'une instance de Camera.
    @Override
    public void surfaceCreated(SurfaceHolder sholder) {
        try {
            if (maCamera == null) {
                maCamera.setPreviewDisplay(sholder);
                maCamera.startPreview();
            }
        } catch (IOException e) {
            Log.d(VIEW_LOG_TAG,e.getMessage());
        }
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        refreshCamera(maCamera);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        refreshCamera(maCamera);
    }


    public void refreshCamera(Camera camera) {
        // rien à faire à part arrêter
        if (sh.getSurface() == null) {
            return;
        }
        try {
            maCamera.stopPreview();
        } catch (Exception e) {
            Log.d(VIEW_LOG_TAG, e.getMessage());
        }
        // demarrer l'aperçu et definition des chgmt taille/ rotation ici
        setCamera(camera);
        try {
        //    int rotate = 90;
         //   Camera.Parameters params = maCamera.getParameters();
         //   params.setRotation(rotate);
        //    maCamera.setParameters(params);
            maCamera.setPreviewDisplay(sh);
            camera.setDisplayOrientation(90);
            maCamera.startPreview();
        } catch (Exception e) {
            Log.d(VIEW_LOG_TAG, e.getMessage());
        }
    }


    public void setCamera(Camera camera) {
        maCamera = camera;
    }

    public Camera getCamera() {
        return maCamera ;
    }

}
