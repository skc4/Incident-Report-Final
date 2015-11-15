package mobileproject.incidentreport.Entities;

/**
 * Created by rettwalker on 11/7/15.
 */
public class Officer {
    private int officer_id;
    private String username;

    public Officer(){

    }

    public String getUsername() {
        return username;
    }

    public int getOfficer_id() {
        return officer_id;
    }

    public void setOfficer_id(int officer_id) {
        this.officer_id = officer_id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
