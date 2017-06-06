package com.example.denis.funculture.activities;

/**
 * Created by Rihab on 09/04/17.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.denis.funculture.R;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRCodeScanner extends Activity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView ScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        QRCScanner(this.ScannerView) ;
    }

    //initialise le scanner
    public  void QRCScanner (View view){
        ScannerView = new ZXingScannerView(this);   
        setContentView(ScannerView);
        ScannerView.setResultHandler(this); 
        ScannerView.startCamera();        

    }

    @Override
    public void onPause() {
        super.onPause();
        ScannerView.stopCamera();
    }

    // Récupération du contenu
    @Override
    public void handleResult(Result rawResult) {
        //Récupérer le résultat du scanner ici
        Log.e("handler", rawResult.getText()); // Prints scan results
        Log.e("handler", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode)
        // afficher le résultat dans une dialog box.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Contenu du code");
        builder.setMessage(rawResult.getText());
        AlertDialog alerte = builder.create();
        alerte.show();
        // Pour redémarrer le scanner.
        ScannerView.resumeCameraPreview(this);

    }
}
