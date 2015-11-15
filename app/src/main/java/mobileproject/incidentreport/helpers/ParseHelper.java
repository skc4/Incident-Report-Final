package mobileproject.incidentreport.helpers;

import android.content.Context;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.SaveCallback;

/**
 * Created by rettwalker on 10/29/15.
 */
public class ParseHelper {
    private static String TAG = "ParseHelper";
    public static void registerParse(Context context) {
        // initializing parse library
        Parse.initialize(context, ConfigApp.PARSE_APPLICATION_ID, ConfigApp.PARSE_CLIENT_KEY);
        ParseInstallation.getCurrentInstallation().saveInBackground();
        /*
        ParsePush.subscribeInBackground(ConfigApp.PARSE_CHANNEL, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.e(TAG, "Successfully subscribed to Parse!");
            }
        });
        */
    }
}
