package com.example.denis.funculture.fragments;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.denis.funculture.R;
import com.example.denis.funculture.main.App;
import com.google.android.gms.location.DetectedActivity;

import java.util.List;

public class RecognitionActivityFragment extends Fragment {
    private View contentView;
    private TextView textActivity;
    private TextView textVehicule;
    private TextView textBicycle;
    private TextView textOnfoot;
    private TextView textRunning;
    private TextView textWalking;
    private TextView textStill;
    private TextView textTitling;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        this.contentView = inflater.inflate(R.layout.activity_service_view, null);

        this.textActivity = (TextView) this.contentView.findViewById(R.id.activity);
        this.textVehicule = (TextView) this.contentView.findViewById(R.id.vehicule);
        this.textBicycle = (TextView) this.contentView.findViewById(R.id.bicycle);
        this.textOnfoot = (TextView) this.contentView.findViewById(R.id.onfoot);
        this.textRunning = (TextView) this.contentView.findViewById(R.id.running);
        this.textWalking = (TextView) this.contentView.findViewById(R.id.walking);
        this.textStill = (TextView) this.contentView.findViewById(R.id.still);
        this.textTitling = (TextView) this.contentView.findViewById(R.id.titling);

        return this.contentView;
    }

    @Override
    public View getView() {
        return this.contentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        App.getSingleton().getSingleton().setCurrentFragment(this);
        App.getSingleton().getRecognitionActivity().initClient();
    }

    @Override
    public void onDestroy() {
        App.getSingleton().getRecognitionActivity().stopService();

        super.onDestroy();
    }

    public void handleServiceConnected() {
        this.textActivity.setText("Service connecté");
    }

    public void handleDetectedActivities(final List<DetectedActivity> probableActivities) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for( DetectedActivity activity : probableActivities ) {
                    switch( activity.getType() ) {
                        case DetectedActivity.IN_VEHICLE: {
                            textVehicule.setText("In Vehicle : " + activity.getConfidence() );
                            break;
                        }
                        case DetectedActivity.ON_BICYCLE: {
                            textBicycle.setText("On Bicycle : " + activity.getConfidence() );
                            break;
                        }
                        case DetectedActivity.ON_FOOT: {
                            textOnfoot.setText("On Foot : " + activity.getConfidence() );
                            break;
                        }
                        case DetectedActivity.RUNNING: {
                            textRunning.setText("Running : " + activity.getConfidence() );
                            break;
                        }
                        case DetectedActivity.STILL: {
                            textStill.setText("Still : " + activity.getConfidence() );
                            break;
                        }
                        case DetectedActivity.TILTING: {
                            textTitling.setText("Tilting : " + activity.getConfidence() );
                            break;
                        }
                        case DetectedActivity.WALKING: {
                            textWalking.setText("Walking : " + activity.getConfidence() );
                            if( activity.getConfidence() >= 75 ) {
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity());
                                builder.setContentText( "Are you walking?" );
                                builder.setSmallIcon( R.mipmap.ic_launcher );
                                builder.setContentTitle( getString( R.string.app_name ) );
                                NotificationManagerCompat.from(getActivity()).notify(0, builder.build());
                            }
                            break;
                        }
                        case DetectedActivity.UNKNOWN: {
                            Log.e( "ActivityRecogition", "Unknown: " + activity.getConfidence() );
                            break;
                        }
                    }
                }
            }
        });
    }

    public void handleServiceSuspended() {
        this.textActivity.setText("Service suspendu");
    }

    public void handleServiceConnectionFailed() {
        this.textActivity.setText("Service injoignable");
    }
}
