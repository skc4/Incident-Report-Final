package mobileproject.incidentreport.helpers;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import mobileproject.incidentreport.Activities.IncidentList;
import mobileproject.incidentreport.Activities.Officer_Menu;
import mobileproject.incidentreport.Activities.Officer_Report;
import mobileproject.incidentreport.Activities.User_Menu;

/**
 * Created by rettwalker on 11/1/15.
 */
public class IncidentReportPushReceiver extends ParsePushBroadcastReceiver {
    private final String TAG = IncidentReportPushReceiver.class.getSimpleName();
    JSONObject info = null;




    public IncidentReportPushReceiver() {
        super();
    }

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        super.onPushReceive(context, intent);
        Log.i(TAG, "Message Received " + intent.getExtras().toString());
        NotificationBuilder notBuilder = new NotificationBuilder(context);
        ToOfficerNoti officerNoti = new ToOfficerNoti(context);
        ToUserNoti toUserNoti = new ToUserNoti(context);

        Intent outGoing;
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        String type = null;
        try {
            parseJSON(intent);
            type = info.getString("type");
            Log.i(TAG,"type = "+type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        switch(type){
            case "report":
                outGoing = new Intent(context, IncidentList.class);
                outGoing.putExtra("GroupName","reporting");
                notBuilder.displayNotification("Alert!!","An Incident Has Occurred, Please Respond!",outGoing);
                break;
            case "toOfficer":
                outGoing = new Intent(context, Officer_Report.class);
                outGoing.putExtras(intent.getExtras());
                try {
                    officerNoti.displayNotification("Alert!!","An Incident Has Occurred, Please Respond!",outGoing);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "toUser":
                outGoing = new Intent(context, User_Menu.class);
                outGoing.putExtra("GroupName","respondToUser");
                toUserNoti.displayNotification("Alert!!", "An Officer Is Responding",outGoing);
                break;
            default:break;
        }
    }

    private void parseJSON(Intent intent) throws JSONException {
        info = new JSONObject(intent.getExtras().getString("com.parse.Data"));
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
