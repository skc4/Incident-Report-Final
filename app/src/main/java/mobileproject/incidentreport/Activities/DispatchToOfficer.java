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
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParsePush;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import mobileproject.incidentreport.Entities.Incident;
import mobileproject.incidentreport.Entities.Officer;
import mobileproject.incidentreport.R;
import mobileproject.incidentreport.helpers.ConfigApp;
import mobileproject.incidentreport.helpers.LogOut;
import mobileproject.incidentreport.helpers.OfficerAdapter;

public class DispatchToOfficer extends AppCompatActivity {
    private final String TAG = DispatchToOfficer.class.getSimpleName();
    private ArrayList<Officer> officers = new ArrayList<>();
    private OfficerAdapter officerArrayAdapter;
    private Incident incident;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatch_to_officer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setIncident();
        final ListView officerlist = (ListView) findViewById(R.id.disToOffList);
        new getOfficers().execute();

        officerArrayAdapter = new OfficerAdapter(this, officers);
        officerlist.setAdapter(officerArrayAdapter);
        officerArrayAdapter.notifyDataSetChanged();
        officerlist.setClickable(true);
        officerlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Officer officer = (Officer) officerlist.getItemAtPosition(position);
                //new assignOfficer(officer.getOfficer_id()).execute();
                ParsePush push = new ParsePush();
                push.setChannel(officer.getUsername());
                JSONObject incidentOb = null;

                try {
                    incidentOb = new JSONObject().put("incident_id", incident.getId())
                            .put("type", "toOfficer")
                            .put("strAddress", incident.getStreetAddress())
                            .put("catType", incident.getType());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                push.setData(incidentOb);
                push.sendInBackground();
                Toast notifyOfficer = Toast.makeText(getBaseContext(), "Officer Notified and User Notified", Toast.LENGTH_LONG);
                notifyOfficer.show();
                finish();

            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

    private void setIncident(){
        TextView type = (TextView) findViewById(R.id.type);
        TextView address = (TextView) findViewById(R.id.incident_address);
        Intent intent = getIntent();
        incident = (Incident) intent.getExtras().getSerializable("INCIDENT");
        address.setText(incident.getStreetAddress());
        type.setText(incident.getType());
        Log.i(TAG,"incident id = "+incident.getId()+" user = "+incident.getUsername());

    }

    private class getOfficers extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(ConfigApp.database_url, ConfigApp.database_user, ConfigApp.database_pass);
                String queryString = "select * from tbl_officers";

                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(queryString);

                while (rs.next()) {

                    Officer officer = new Officer();
                    officer.setOfficer_id(rs.getInt("officer_id"));
                    officer.setUsername(rs.getString("username"));
                    officers.add(officer);

                }
                //query=" INSERT INTO tbl_officer_responds_incident VALUES(NULL,NULL,'"+current_incident.getId()+",NULL);";
                //st.executeUpdate(query);
                con.close();

            }catch (Exception e){
                Log.e(TAG, e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            officerArrayAdapter.notifyDataSetChanged();
            Log.d(TAG,"DID the stuff");


        }
    }

    /*
    private class assignOfficer extends AsyncTask<Void, Void, Void> {
        private int id;
        public assignOfficer(int of_id){
            id=of_id;

        }

        @Override
        protected Void doInBackground(Void... params) {
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(ConfigApp.database_url, ConfigApp.database_user, ConfigApp.database_pass);
                String queryString = "INSERT INTO tbl_officer_responds_incident (respond_id,officer_id,incident_id,respondTime) " +
                        "VALUES (NULL,'"+id+"','"+incident.getId()+"',NULL);";

                Statement st = con.createStatement();
                st.executeUpdate(queryString);

                queryString = "UPDATE tbl_incidents SET responded='true' WHERE incident_id='"+incident.getId()+"';";
                st.executeUpdate(queryString);

                con.close();

            }catch (Exception e){
                Log.e(TAG, e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            officerArrayAdapter.notifyDataSetChanged();
            Log.d(TAG,"DID the stuff");


        }
    }
    */

}
