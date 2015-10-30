package mobileproject.incidentreport.Entities;

import android.net.Uri;

/**
 * Created by rettwalker on 10/29/15.
 */
public class Incident {
    private double lat;
    private double longit;
    private String timestamp;
    private String type;
    private String description;
    private Uri fileUri;
    private String username;

    public Incident(){

    }
    public void setLat(double lat){
        this.lat = lat;
    }
    public void setLongit(double longit){
        this.longit = longit;
    }
    public void setTimestamp(String timestamp){
        this.timestamp = timestamp;
    }
    public void setType(String type){
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFileUri(Uri fileUri) {
        this.fileUri = this.fileUri;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
