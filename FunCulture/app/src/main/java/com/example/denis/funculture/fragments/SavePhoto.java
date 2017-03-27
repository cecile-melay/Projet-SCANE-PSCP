package com.example.denis.funculture.fragments;

/**
 * Created by Rihab on 16/03/17.
 */
import android.content.Context;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;

import java.io.File;

public class SavePhoto implements MediaScannerConnectionClient {

    private MediaScannerConnection mMs;
    private File mFile;

    public SavePhoto(Context context, File f) {
        mFile = f;
        mMs = new MediaScannerConnection(context, this);
        mMs.connect();
    }

    @Override
    public void onMediaScannerConnected() {
        mMs.scanFile(mFile.getAbsolutePath(), null);
    }

    @Override
    public void onScanCompleted(String path, Uri uri) {
        mMs.disconnect();
    }

}