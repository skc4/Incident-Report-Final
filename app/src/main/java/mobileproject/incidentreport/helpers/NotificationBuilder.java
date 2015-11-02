package mobileproject.incidentreport.helpers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;


import mobileproject.incidentreport.Activities.Officer_Menu;

/**
 * Created by rettwalker on 11/2/15.
 */
public class NotificationBuilder  {
    private final String TAG = NotificationBuilder.class.getSimpleName();
    private Context incomingContext;

    public NotificationBuilder(){

    }

    public NotificationBuilder(Context context){
        this.incomingContext = context;
    }

    public void displayNotification(String title, String message, Intent intent) {

        Log.i(TAG,"Incoming Intent = "+intent.getExtras().toString());

        String type = intent.getExtras().getString("type");

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        incomingContext,
                        0,
                        intent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );

        NotificationCompat.Builder noteBuilder = new NotificationCompat.Builder(incomingContext);

        noteBuilder.setContentIntent(resultPendingIntent)
                .setContentText(type)
                .setContentTitle(title);

        NotificationManager noteMan = (NotificationManager) incomingContext
                .getSystemService(Context.NOTIFICATION_SERVICE);

        noteMan.notify(001,noteBuilder.build());
    }

}
