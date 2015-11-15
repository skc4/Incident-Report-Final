package mobileproject.incidentreport.helpers;

import android.content.Context;
import android.location.Address;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mobileproject.incidentreport.Entities.Incident;
import mobileproject.incidentreport.R;

/**
 * Created by rettwalker on 11/8/15.
 */
public class IncidentAdapter extends ArrayAdapter<Incident> {
    private final String TAG = IncidentAdapter.class.getSimpleName();

    public IncidentAdapter(Context context, ArrayList<Incident> incidents){
        super(context,0,incidents);
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent){
        Incident incident = getItem(pos);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_incident, parent, false);
        }


        TextView type = (TextView) convertView.findViewById(R.id.type);
        TextView address = (TextView) convertView.findViewById(R.id.incident_address);
        TextView responded = (TextView) convertView.findViewById(R.id.responded);

        type.setText(incident.getType());
        address.setText(incident.getStreetAddress());

        if(incident.getRespondedTo()){
            responded.setText("Officer has responded");
            convertView.setBackgroundResource(R.color.iron);
        }else{
            responded.setText("Officer needed!");
            convertView.setBackgroundResource(R.color.primary);
        }

        return convertView;
    }

}
