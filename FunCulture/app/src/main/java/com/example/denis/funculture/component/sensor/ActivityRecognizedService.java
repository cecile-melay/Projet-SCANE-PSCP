package com.example.denis.funculture.component.sensor;

import android.app.Activity;
import android.app.Dialog;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.denis.funculture.R;
import com.example.denis.funculture.activities.RecognitionActivity;
import com.example.denis.funculture.main.App;
import com.example.denis.funculture.utils.Util;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.List;

/**
 * Created by denis on 25/01/2017.
 */

public class ActivityRecognizedService extends IntentService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private GoogleApiClient mApiClient;

    public ActivityRecognizedService() {
        super("ActivityRecognizedService");
    }

    public void init(RecognitionActivity activity) {
        initClient();
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        if(ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            ((RecognitionActivity) App.getSingleton().getCurrentActivity()).handleDetectedActivities( result.getProbableActivities() );
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        ((RecognitionActivity) App.getSingleton().getCurrentActivity()).handleServiceConnected();
        Intent intent = new Intent( App.getSingleton().getCurrentActivity(), ActivityRecognizedService.class );
        PendingIntent pendingIntent = PendingIntent.getService( App.getSingleton().getCurrentActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT );
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates( mApiClient, 0, pendingIntent );
    }

    @Override
    public void onConnectionSuspended(int i)
    {
        ((RecognitionActivity) App.getSingleton().getCurrentActivity()).handleServiceSuspended();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {
        ((RecognitionActivity) App.getSingleton().getCurrentActivity()).handleServiceConnectionFailed();
    }
    private void initClient() {
        mApiClient = new GoogleApiClient.Builder(App.getSingleton().getCurrentActivity())
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mApiClient.connect();
    }
}
