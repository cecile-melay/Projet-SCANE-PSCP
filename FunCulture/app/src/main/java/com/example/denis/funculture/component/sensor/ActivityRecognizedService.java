package com.example.denis.funculture.component.sensor;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.denis.funculture.fragments.RecognitionActivityFragment;
import com.example.denis.funculture.main.App;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionResult;

/**
 * Created by denis on 25/01/2017.
 */

public class ActivityRecognizedService extends IntentService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private GoogleApiClient mApiClient;

    public ActivityRecognizedService() {
        super("ActivityRecognizedService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            ((RecognitionActivityFragment) App.getSingleton().getCurrentFragment()).handleDetectedActivities( result.getProbableActivities() );
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        ((RecognitionActivityFragment) App.getSingleton().getCurrentFragment()).handleServiceConnected();
        Intent intent = new Intent( App.getSingleton().getCurrentActivity(), ActivityRecognizedService.class );
        PendingIntent pendingIntent = PendingIntent.getService( App.getSingleton().getCurrentActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT );
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates( mApiClient, 0, pendingIntent );
    }

    @Override
    public void onConnectionSuspended(int i)
    {
        ((RecognitionActivityFragment) App.getSingleton().getCurrentFragment()).handleServiceSuspended();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {
        ((RecognitionActivityFragment) App.getSingleton().getCurrentFragment()).handleServiceConnectionFailed();
    }
    public void initClient() {
        mApiClient = new GoogleApiClient.Builder(App.getSingleton().getCurrentActivity())
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mApiClient.connect();
    }

    public void stopService() {
        mApiClient.disconnect();
    }
}
