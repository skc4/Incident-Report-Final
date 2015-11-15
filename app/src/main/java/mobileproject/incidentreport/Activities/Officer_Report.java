package mobileproject.incidentreport.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.parse.ParsePush;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;

import mobileproject.incidentreport.Entities.Incident;
import mobileproject.incidentreport.R;
import mobileproject.incidentreport.helpers.ConfigApp;
import mobileproject.incidentreport.helpers.LogOut;

public class Officer_Report extends AppCompatActivity {
    private final String TAG = Officer_Report.class.getSimpleName();
    private static int incident_id = 0;
    private Incident incident;
    private int phonenumber;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private Timestamp timeStamp;
    JSONObject incidentUs = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_officer__report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = getApplicationContext().getSharedPreferences(ConfigApp.USER_LOGIN_PREF, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        Intent intent = getIntent();
        incident_id = intent.getIntExtra("incident_id",0);
        new getIncidentInfo().execute();
        Calendar calendar = Calendar.getInstance();
        timeStamp = new java.sql.Timestamp(calendar.getTime().getTime());
        new assignOfficer(sharedPreferences.getInt("USER_ID",0)).execute();
        Log.i(TAG, "incident_id = " + incident_id);

        editor.putInt("CURRENT_INCIDENT", incident_id);
        editor.commit();

    }
    public void alertUser(View view){
        new assignOfficer(sharedPreferences.getInt("USER_ID",0)).execute();
        try {
            incidentUs = new JSONObject().put("type", "toUser");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ParsePush userPush = new ParsePush();
        userPush.setChannel(incident.getUsername());
        userPush.setData(incidentUs);
        userPush.sendInBackground();
    }

    public void openGallery(View view){
        Intent intent = new Intent(Officer_Report.this, Officer_Gallery.class);
        startActivity(intent);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id)
        {
            case R.id.accountSettings:
                Intent intent = new Intent(this, user_account_settings.class);
                startActivity(intent);
                break;
            case R.id.logout:
                LogOut exit = new LogOut();
                exit.setTheContext(getApplicationContext());
                exit.setActivity(this);
                exit.logMeOut();
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private class assignOfficer extends AsyncTask<Void, Void, Void> {
        private int id;

        public assignOfficer(int of_id) {
            id = of_id;

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(ConfigApp.database_url, ConfigApp.database_user, ConfigApp.database_pass);
                String queryString = "INSERT INTO tbl_officer_responds_incident (respond_id,officer_id,incident_id,respondTime) " +
                        "VALUES (NULL,'" + id + "','" + incident_id + "','"+timeStamp+"');";

                Statement st = con.createStatement();
                st.executeUpdate(queryString);

                queryString = "UPDATE tbl_incidents SET responded='true' WHERE incident_id='" + incident_id + "';";
                st.executeUpdate(queryString);

                con.close();

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Log.d(TAG, "DID the stuff");


        }
    }
    private class getIncidentInfo extends AsyncTask<Void, Void, Void> {
        private int id;


        @Override
        protected Void doInBackground(Void... params) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(ConfigApp.database_url, ConfigApp.database_user, ConfigApp.database_pass);
                String queryString = "SELECT tbl_incidents.*, cat_type, username\n" +
                        "                        FROM tbl_incidents\n" +
                        "                        INNER JOIN tbl_incident_cat\n" +
                        "                         ON tbl_incidents.incident_id=tbl_incident_cat.incident_id\n" +
                        "                         INNER JOIN tbl_catogories\n" +
                        "                        ON tbl_incident_cat.catogories_id=tbl_catogories.catogories_id\n" +
                        "                        INNER JOIN tbl_user_reports_incident\n" +
                        "                        ON tbl_incidents.incident_id = tbl_user_reports_incident.incident_id\n" +
                        "                        INNER JOIN tbl_users\n" +
                        "                        ON tbl_user_reports_incident.user_id=tbl_users.user_id\n" +
                        "                        WHERE tbl_incidents.incident_id='"+incident_id+"';";

                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(queryString);

                while (rs.next()) {
                    Incident incident = new Incident();
                    incident.setId(rs.getInt("incident_id"));
                    incident.setLongit(rs.getFloat("longitude"));
                    incident.setLat(rs.getFloat("latitude"));
                    incident.setDescription(rs.getString("description"));
                    incident.setType(rs.getString("cat_type"));
                    incident.setUsername(rs.getString("username"));
                    phonenumber=rs.getInt("phonenumber");
                    incident.setStreetAddress(rs.getString("strAddr"));


                }

                con.close();

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(TAG, "DID the stuff");


        }
    }
}
