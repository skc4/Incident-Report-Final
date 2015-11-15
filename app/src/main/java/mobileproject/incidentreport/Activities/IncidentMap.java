package mobileproject.incidentreport.Activities;

import android.app.PendingIntent;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import mobileproject.incidentreport.Entities.Incident;
import mobileproject.incidentreport.R;
import mobileproject.incidentreport.helpers.ConfigApp;

public class IncidentMap extends FragmentActivity implements LocationListener, AdapterView.OnItemSelectedListener {

    private Spinner categoryDropDown;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    ArrayList<Incident> report = new ArrayList<>();
    String locationType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident_map);
        categoryDropDown = (Spinner) findViewById(R.id.categorySpin);
        categoryDropDown.setOnItemSelectedListener(this);

        new getDataFromDatabase().execute();
    }


    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String cat = categoryDropDown.getSelectedItem().toString();
        filterCategory(cat);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void filterCategory(String category) {
        try {
            if (!category.equals("Select Category")) {
                mMap.clear();

                if(locationType.equals("network")) {
                    try {
                        LocationManager mng = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        mng.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
                        Location location = mng.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        double lat = location.getLatitude();
                        double lon = location.getLongitude();

                        mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title("YOU").icon(BitmapDescriptorFactory.fromResource(R.drawable.youmarker)));
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                else if (locationType.equals("gps"))
                {
                    try {
                        LocationManager mng = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        mng.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
                        Location location = mng.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        double lat = location.getLatitude();
                        double lon = location.getLongitude();

                        mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title("YOU").icon(BitmapDescriptorFactory.fromResource(R.drawable.youmarker)));
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                     }
                }

                for (int i = 0; i < report.size(); i++) {
                    if (report.get(i).getType().equals(category)) {
                        mMap.addMarker(new MarkerOptions().position(new LatLng(report.get(i).getLat(), report.get(i).getLongit())).title(report.get(i).getType()).icon(BitmapDescriptorFactory.fromResource(R.drawable.criminal)));

                    }
                }
            }
            else
            {
                for (int i = 0; i < report.size(); i++) {
                    mMap.addMarker(new MarkerOptions().position(new LatLng(report.get(i).getLat(), report.get(i).getLongit())).title(report.get(i).getType()).icon(BitmapDescriptorFactory.fromResource(R.drawable.criminal)));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private class getDataFromDatabase extends AsyncTask<Void, Void, Void> {
        private String queryResult;

        protected Void doInBackground(Void... arg0) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(ConfigApp.database_url, ConfigApp.database_user, ConfigApp.database_pass);
                String queryString = "SELECT tbl_incidents.*, catogories_id " +
                        "             FROM tbl_incidents" +
                        "             INNER JOIN tbl_incident_cat" +
                        "             ON tbl_incidents.incident_id=tbl_incident_cat.incident_id;";

                Statement st = con.createStatement();
                final ResultSet rs = st.executeQuery(queryString);

                while (rs.next()) {
                    Incident newReport = new Incident();
                    newReport.setLongit(rs.getFloat("longitude"));
                    newReport.setLat(rs.getFloat("latitude"));
                    newReport.setDescription(rs.getString("description"));
                    newReport.setId(rs.getInt("incident_id"));
                    newReport.setCatId(rs.getInt("catogories_id"));
                    report.add(newReport);
                }
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Void result) {
            setUpMapIfNeeded();
        }
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.incidentMap))
                    .getMap();
            setUpMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        } else {
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        try {


            LocationManager mng = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            boolean isGPSEnabled = mng.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = mng.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled)
            {

            }
            else
            {
                if (isNetworkEnabled)
                {
                    mng.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000,0,this);
                    if (mng != null)
                    {
                        Location location = mng.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null)
                        {
                            double lat = location.getLatitude();
                            double lon = location.getLongitude();

                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 10);
                            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title("YOU").icon(BitmapDescriptorFactory.fromResource(R.drawable.youmarker)));
                            mMap.animateCamera(cameraUpdate);

                            locationType = "network";
                        }

                    }
                }
                else if(isGPSEnabled)
                {
                    mng.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0,this);
                    if (mng != null)
                    {
                        Location location = mng.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null)
                        {
                            double lat = location.getLatitude();
                            double lon = location.getLongitude();

                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 10);
                            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title("YOU").icon(BitmapDescriptorFactory.fromResource(R.drawable.youmarker)));
                            mMap.animateCamera(cameraUpdate);

                            locationType = "gps";
                        }

                    }

                }
            }
            for (int i = 0; i < report.size(); i++) {
                mMap.addMarker(new MarkerOptions().position(new LatLng(report.get(i).getLat(), report.get(i).getLongit())).title(report.get(i).getType()).icon(BitmapDescriptorFactory.fromResource(R.drawable.criminal)));
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}