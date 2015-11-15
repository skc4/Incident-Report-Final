package mobileproject.incidentreport.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.parse.ParsePush;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import mobileproject.incidentreport.Entities.Incident;
import mobileproject.incidentreport.R;
import mobileproject.incidentreport.helpers.ConfigApp;
import mobileproject.incidentreport.helpers.LogOut;

public class Report_A_Incident extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener{
    public static final String TAG = Report_A_Incident.class.getSimpleName();
    private static GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    private Uri fileUri;
    private Incident current_incident = new Incident();
    static final int REQUEST_IMAGE_CAPTURE = 1;
    SharedPreferences prefs;
    private String username;
    private int user_id;
    private Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report__a__incident);
        buildGoogleApiClient();
        geocoder = new Geocoder(this, Locale.getDefault());
        prefs = getSharedPreferences(ConfigApp.USER_LOGIN_PREF, MODE_PRIVATE);
        username = prefs.getString("USERNAME", null);
        user_id = prefs.getInt("USER_ID",0);
        mGoogleApiClient.connect();
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
        Spinner reportCat = (Spinner)findViewById(R.id.cat_choices);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.incident_cato, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reportCat.setAdapter(adapter);
        reportCat.setOnItemSelectedListener(new TypeSelector());


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


    public void takePicture(View view){
        dispatchTakePictureIntent();
    }

    private void dispatchTakePictureIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            fileUri = getOutputMediaFileUri(); // create a file to save the image

            Log.d(TAG, "File URI is "+ fileUri);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Log.d(TAG, "Photo Taken Successfully");


        }
    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Location services connected.");
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        else {
            handleNewLocation(location);
        };

    }

    /** Create a file Uri for saving an image or video */
    private  Uri getOutputMediaFileUri(){
        try {
            return Uri.fromFile(getOutputMediaFile());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** Create a File for saving an image or video */
    private File getOutputMediaFile() throws ParseException {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(getExternalFilesDir(
                null),"IncidentReport");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name

        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_"+ user_id + ".jpg");


        return mediaFile;
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection Failed!");

    }
    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());
        current_incident.setLat(location.getLatitude());
        current_incident.setLongit(location.getLongitude());
        try {
            findStrAddr();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    public void reportIncident(View view) throws JSONException {
        Calendar calendar = Calendar.getInstance();
        Timestamp timeStamp = new java.sql.Timestamp(calendar.getTime().getTime());
        current_incident.setTimestamp(timeStamp);
        EditText descrip = (EditText) findViewById(R.id.incident_description);
        String description = descrip.getText().toString();
        current_incident.setDescription(description);
        current_incident.setUsername(username);
        JSONObject incidentTo = new JSONObject()
                .put("type","report");

        Toast all_clear = Toast.makeText(this, "Incident Reported, Thank you!" , Toast.LENGTH_LONG);
        all_clear.show();

        ParsePush push = new ParsePush();
        push.setChannel("dispatch");
        push.setData(incidentTo);
        push.sendInBackground();
        new insertDB().execute();
        finish();
    }
    private class insertDB extends AsyncTask<Void, Void, Void> {
        final ProgressDialog progressDialog = new ProgressDialog(Report_A_Incident.this,
                R.style.AppTheme_AppBarOverlay);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            final ProgressDialog progressDialog = new ProgressDialog(Report_A_Incident.this,
                    R.style.AppTheme_AppBarOverlay);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Reporting Incident...");
            progressDialog.show();
        }



        @Override
        protected Void doInBackground(Void... params) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(ConfigApp.database_url, ConfigApp.database_user, ConfigApp.database_pass);
                Statement st = con.createStatement();
                PreparedStatement imageSt;
                ResultSet rs;
                int check;
                int typeID=0;
                switch (current_incident.getType().toLowerCase()) {
                    case "fire":
                        typeID = 4;
                        break;
                    case "homocide":
                        typeID = 1;
                        break;
                    case "battery":
                        typeID = 3;
                        break;
                    case "robbery":
                        typeID = 2;
                        break;
                    case "sexual assault":
                        typeID = 5;
                        break;
                    case "drugs":
                        typeID = 6;
                    default:
                        break;
                }
                //Get the Top ID
                String query = "SELECT MAX(incident_id) FROM tbl_incidents;";



                rs = st.executeQuery(query);

                if(rs.next()){
                    Log.i(TAG,"Incident ID = "+rs.getInt("MAX(incident_id)"));
                    current_incident.setId(rs.getInt("MAX(incident_id)")+1);
                }

                //Insert incident into tbl_incident
                query= "INSERT INTO tbl_incidents (incident_id,longitude,latitude,description,strAddr)" +
                        " VALUES('"+current_incident.getId()+"','"+current_incident.getLat()
                        +"', '"+current_incident.getLongit()+"', '"+current_incident.getDescription()+"','"+current_incident.getStreetAddress()+"');";
                st.executeUpdate(query);

                query=" INSERT INTO tbl_incident_cat (incident_id,catogories_id)" +
                        " VALUES ('"+current_incident.getId()+"','"+typeID+"');";
                st.executeUpdate(query);


                query = " INSERT INTO tbl_user_reports_incident (report_id,user_id,incident_id,reportTime)" +
                        " VALUES (NULL,'"+user_id+"','"+current_incident.getId()+"'," +
                        "'"+current_incident.getTimestamp()+"');";
                st.executeUpdate(query);



                if(fileUri!=null){
                    File pic = new File(fileUri.getPath());
                    FileInputStream fileInputStream = new FileInputStream(pic);
                    imageSt=con.prepareStatement("INSERT INTO tbl_photos(photo_id,photo,incident_id) VALUES (NULL,?,?);");
                    imageSt.setBinaryStream(1,fileInputStream);
                    imageSt.setInt(2,current_incident.getId());
                    imageSt.executeUpdate();
                }
                con.close();

            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }
    public class TypeSelector extends Activity implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            current_incident.setType(parent.getItemAtPosition(pos).toString());
        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
        }
    }
    public void findStrAddr() throws IOException {
        List<Address> addresses = geocoder.getFromLocation(current_incident.getLat(), current_incident.getLongit(), 1);
        //Log.i(TAG,"incident lat1 = "+lat+"incident lng = "+lng);
        String addr = addresses.get(0).getAddressLine(0)+"\n"+
                addresses.get(0).getLocality()+" "+addresses.get(0).getAdminArea()+
                " "+addresses.get(0).getPostalCode();
        current_incident.setStreetAddress(addr);
    }
}

