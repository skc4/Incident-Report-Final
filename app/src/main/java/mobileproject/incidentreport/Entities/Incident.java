package mobileproject.incidentreport.Entities;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import org.json.JSONObject;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * Created by rettwalker on 10/29/15.
 */
public class Incident implements Serializable,ClusterItem {
    private double lat;
    private double longit;
    private Timestamp timestamp;
    private String type;
    private String description;
    private int id;
    private String streetAddress;
    private String username;
    private boolean respondedTo = false;
    private String category;

    public Incident(){

    }


    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLat(double lat){
        this.lat = lat;
    }

    public double getLat() {
        return lat;
    }
    public void setLongit(double longit){
        this.longit = longit;
    }

    public double getLongit() {
        return longit;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setTimestamp(Timestamp timestamp){
        this.timestamp = timestamp;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setType(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public boolean getRespondedTo() {
        return respondedTo;
    }

    public void setRespondedTo(boolean respondedTo) {
        this.respondedTo = respondedTo;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String catId) {
        this.category = catId;
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(lat,longit);
    }
}
