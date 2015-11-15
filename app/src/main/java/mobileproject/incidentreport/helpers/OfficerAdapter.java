package mobileproject.incidentreport.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import mobileproject.incidentreport.Entities.Officer;
import mobileproject.incidentreport.R;

/**
 * Created by rettwalker on 11/8/15.
 */
public class OfficerAdapter extends ArrayAdapter<Officer> {
    public OfficerAdapter(Context context, ArrayList<Officer> officers){
        super(context,0,officers);
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent){
        Officer officer = getItem(pos);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_officer, parent, false);
        }

        TextView username = (TextView) convertView.findViewById(R.id.firstLine);
        username.setText(officer.getUsername());
        return convertView;
    }


}
