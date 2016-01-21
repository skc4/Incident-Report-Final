package mobileproject.incidentreport.helpers;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.wallet.firstparty.InitializeBuyFlowRequest;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import mobileproject.incidentreport.Entities.Incident;
import mobileproject.incidentreport.R;

/**
 * Created by zthomas on 11/14/15.
 */
public class IncidentRenderer extends DefaultClusterRenderer<Incident> implements ClusterManager.OnClusterItemClickListener{
    private Context thisContext;
    public IncidentRenderer(Context context, GoogleMap map, ClusterManager clusterManager) {
        super(context, map, clusterManager);
        thisContext = context;
    }
    @Override
    protected void onBeforeClusterItemRendered(Incident incident,MarkerOptions options)
    {
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.criminal));
    }

    @Override
    public boolean onClusterItemClick(ClusterItem clusterItem) {
        Incident item = (Incident)clusterItem;
        Toast.makeText(thisContext,item.getCategory(),Toast.LENGTH_SHORT).show();
        return false;
    }
}
