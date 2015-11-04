package mobileproject.incidentreport.helpers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import mobileproject.incidentreport.Activities.Officer_Menu;

/**
 * Created by rettwalker on 11/1/15.
 */
public class IncidentReportPushReceiver extends ParsePushBroadcastReceiver {
    private final String TAG = IncidentReportPushReceiver.class.getSimpleName();



    public IncidentReportPushReceiver() {
        super();
    }

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        super.onPushReceive(context, intent);
        Log.i(TAG, "Message Received " + intent.getExtras().toString());

        NotificationBuilder notBuilder = new NotificationBuilder(context);

        Intent outGoing = new Intent(context,Officer_Menu.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        outGoing.putExtras(intent.getExtras());


        notBuilder.displayNotification("Alert!!","An Incident Has Occurred, Please Respond!",outGoing);


    }

    @Override
    protected void onPushDismiss(Context context, Intent intent) {
        super.onPushDismiss(context, intent);
    }

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        super.onPushOpen(context, intent);
        

    }




}
