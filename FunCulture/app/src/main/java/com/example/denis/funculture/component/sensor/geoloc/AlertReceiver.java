package com.example.denis.funculture.component.sensor.geoloc;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by jux on 23/02/2017.
 */

public class AlertReceiver extends BroadcastReceiver {

    private Vibrator v;

    private final int NOTIFICATIONTIMER = 1000;

        @Override
        public void onReceive(Context context, Intent intent) {

            //Variable  Key pour déterminer si l'utilisateur quitte ou entre
            String key = LocationManager.KEY_PROXIMITY_ENTERING;

            //Variable booléenne pour savoir si l'utilisateur entre ou sort
            boolean state = intent.getBooleanExtra(key, false);

            if(state)
            {
                for(int i=0 ; i<3 ; i++)
                {
                    Toast.makeText(context, "ENTREE "+intent.getStringExtra("name"), Toast.LENGTH_SHORT).show();
                }

                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                //Faire une sonnerie de message ou vibreur (suivant le mode par défaut)
                //lors de l'entré de l'utilisateur
                AudioManager audiomanager = (AudioManager)
                        context.getSystemService(Context.AUDIO_SERVICE);

                switch (audiomanager.getRingerMode()) {
                    case AudioManager.RINGER_MODE_SILENT:
                        Log.i("Mode","mode silencieux");
                        break;
                    case AudioManager.RINGER_MODE_VIBRATE:
                        Log.i("Mode"," mode vibreur");
                        v = (Vibrator) MyLocationListener.MA.getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                        //v.vibrate(NOTIFICATIONTIMER);
                        break;
                    case AudioManager.RINGER_MODE_NORMAL:
                        Log.i("Mode"," mode normale");
                        v = (Vibrator) MyLocationListener.MA.getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                        //v.vibrate(NOTIFICATIONTIMER);
                        break;
                }
                //PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, null, 0);

                //Notification notification = createNotification();
                /*notification.setLatestEventInfo(context,
                        "Alerte de bienvenue","Bienvenue dans notre zone!", pendingIntent);*/

                //notificationManager.notify(1000, notification);
            }
            else
            {
                for(int i=0 ; i<3 ; i++)
                {
                    Toast.makeText(context, "SORTIE "+intent.getStringExtra("name"), Toast.LENGTH_SHORT).show();
                }


                //NotificationManager notificationManager =
                        //(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                //PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, null, 0);

                //Notification notification = createNotification();
                /*notification.setLatestEventInfo(context,
                        "Alerte de quitter","Merci d'avoir nous visiter! Au revoir!", pendingIntent);*/

                //notificationManager.notify(1000, notification);
                //Faire une sonnerie de message ou vibreur (suivant le mode par défaut)
                //lors de la sortie de l'utilisateur
                AudioManager audiomanager = (AudioManager)
                        context.getSystemService(Context.AUDIO_SERVICE);

                switch (audiomanager.getRingerMode()) {
                    case AudioManager.RINGER_MODE_SILENT:
                        Log.i("Mode","mode silencieux");
                        break;
                    case AudioManager.RINGER_MODE_VIBRATE:
                        Log.i("Mode"," mode vibreur");
                        v = (Vibrator) MyLocationListener.MA.getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                        //v.vibrate(NOTIFICATIONTIMER);
                        break;
                    case AudioManager.RINGER_MODE_NORMAL:
                        Log.i("Mode"," mode normale");
                        v = (Vibrator) MyLocationListener.MA.getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                        //v.vibrate(NOTIFICATIONTIMER);
                        break;
                }
            }
        }



        //créer une notification
        /*private Notification createNotification() {
            Notification notification = new Notification();

            notification.icon = R.drawable.ic_launcher;
            notification.when = System.currentTimeMillis();

            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notification.flags |= Notification.FLAG_SHOW_LIGHTS;

            notification.defaults|= Notification.DEFAULT_SOUND;
            notification.defaults |= Notification.DEFAULT_VIBRATE;
            notification.defaults |= Notification.DEFAULT_LIGHTS;

            notification.ledARGB = Color.WHITE;
            notification.ledOnMS = 1500;
            notification.ledOffMS = 1500;

            return notification;
        }*/

}
