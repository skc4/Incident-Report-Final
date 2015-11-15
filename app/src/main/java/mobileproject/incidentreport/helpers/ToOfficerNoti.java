package mobileproject.incidentreport.helpers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;


import mobileproject.incidentreport.R;

/**
 * Created by rettwalker on 11/9/15.
 */
public class ToOfficerNoti {
    private final String TAG = NotificationBuilder.class.getSimpleName();
    private Context incomingContext;

    public ToOfficerNoti(){

    }

    public ToOfficerNoti(Context context){
        this.incomingContext = context;
    }

    public void displayNotification(String title, String message, Intent intent) throws JSONException {
        JSONObject information = new JSONObject(intent.getExtras().getString("com.parse.Data"));

        String cat_type = information.getString("catType");
        String address = information.getString("strAddress");
        int incident_id = information.getInt("incident_id");

        Log.i(TAG, "Incoming Intent = " + intent.getExtras().toString());
        intent.putExtra("incident_id",incident_id);
        intent.putExtra("from","noti");


        if(information.getString("type").equals("toOfficer")){

        }

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        incomingContext,
                        0,
                        intent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );

        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle("Incident Information: ");
        inboxStyle.addLine("Type: " + cat_type);
        inboxStyle.addLine("Location: "+address);


        NotificationCompat.Builder noteBuilder = new NotificationCompat.Builder(incomingContext);

        noteBuilder.setSmallIcon(R.drawable.logo)
                .setContentIntent(resultPendingIntent)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setContentText(message)
                .setStyle(inboxStyle);

        NotificationManager noteMan = (NotificationManager) incomingContext
                .getSystemService(Context.NOTIFICATION_SERVICE);

        noteMan.notify(001,noteBuilder.build());
    }

}
