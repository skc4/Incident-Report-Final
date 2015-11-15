package mobileproject.incidentreport.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import mobileproject.incidentreport.Entities.Incident;
import mobileproject.incidentreport.R;
import mobileproject.incidentreport.helpers.ConfigApp;
import mobileproject.incidentreport.helpers.IncidentAdapter;
import mobileproject.incidentreport.helpers.LogOut;


public class IncidentList extends AppCompatActivity {
    private final String TAG = IncidentList.class.getSimpleName();
    private ArrayList<Incident> incidents = new ArrayList<>();
    private IncidentAdapter incidentAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ListView incidentlist = (ListView) findViewById(R.id.incidentList);
        new getIncidents().execute();

        incidentAdapter = new IncidentAdapter(this, incidents);
        incidentlist.setAdapter(incidentAdapter);
        incidentAdapter.notifyDataSetChanged();
        incidentlist.setClickable(true);
        incidentlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Incident incident = (Incident) incidentlist.getItemAtPosition(position);
                Intent intent;
                intent = new Intent(getBaseContext(), DispatchToOfficer.class);
                intent.putExtra("INCIDENT", incident);
                startActivity(intent);

            }
        });

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

    private class getIncidents extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try{
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
                        "                        ORDER BY tbl_incidents.incident_id DESC;";

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
                    incident.setStreetAddress(rs.getString("strAddr"));
                    if(rs.getString("responded").equalsIgnoreCase("true")){
                        incident.setRespondedTo(true);
                    }
                    //Log.i(TAG,"incident lat = "+incident.getLat()+"incident long = "+incident.getLongit());
                    incidents.add(incident);
                }
                con.close();

            }catch (Exception e){
                Log.e(TAG, e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            incidentAdapter.notifyDataSetChanged();
            Log.d(TAG,"DID the stuff");


        }
    }
}
