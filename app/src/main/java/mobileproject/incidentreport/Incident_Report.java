package mobileproject.incidentreport;

import android.app.Application;

import mobileproject.incidentreport.Entities.Incident;
import mobileproject.incidentreport.helpers.ParseHelper;

/**
 * Created by rettwalker on 10/29/15.
 */
public class Incident_Report extends android.support.multidex.MultiDexApplication {
    private static Incident_Report instance;


    public void onCreate() {
        super.onCreate();
        instance = this;

        // register with parse
       ParseHelper.registerParse(this);
    }
    public static synchronized Incident_Report getInstance() {
        return instance;
    }
}
