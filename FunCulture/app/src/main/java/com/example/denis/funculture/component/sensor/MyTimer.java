package com.example.denis.funculture.component.sensor;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

import com.example.denis.funculture.R;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by denis on 11/04/2017.
 */

public class MyTimer implements Chronometer.OnChronometerTickListener {
    Activity activity;
    TextView tvTimer;
    Chronometer chronometer;
    View view;

    public View getView() {
        if(this.view == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            this.view = inflater.inflate(R.layout.timer_view, null, false);
            this.tvTimer = (TextView) view.findViewById(R.id.tv_timer);
        }

        return view;
    }

    public MyTimer(Activity activity) {
        chronometer = new Chronometer(activity);
        chronometer.setOnChronometerTickListener(this);
        this.activity = activity;
    }

    @Override
    public void onChronometerTick(Chronometer chronometer) {
        Long time = chronometer.getBase();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(time));

        String text = String.format("02%d : 02%d", calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
        tvTimer.setText(text);
    }

    public void start() {
        chronometer.start();
    }

    public void stop() {
        chronometer.stop();
    }
}
