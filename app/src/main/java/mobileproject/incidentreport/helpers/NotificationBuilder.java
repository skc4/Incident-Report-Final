package mobileproject.incidentreport.helpers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import mobileproject.incidentreport.R;


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

    public void displayNotification(String title, String message, Intent intent){
        Log.i(TAG,"Incoming Intent");
        String GROUP_KEY = "respondToUser";
        if(intent.getExtras().getString("GroupName").equals("reporting")){
            GROUP_KEY = "reportToDispatch";
        }

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        incomingContext,
                        0,
                        intent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );

        NotificationCompat.Builder noteBuilder = new NotificationCompat.Builder(incomingContext);

        noteBuilder.setSmallIcon(R.drawable.logo)
                .setContentIntent(resultPendingIntent)
                .setContentTitle(title)
                .setContentText(message)
                .setGroup(GROUP_KEY)
                .setGroupSummary(true)
                .setAutoCancel(true);

        NotificationManager noteMan = (NotificationManager) incomingContext
                .getSystemService(Context.NOTIFICATION_SERVICE);

        noteMan.notify(001,noteBuilder.build());
    }

}
