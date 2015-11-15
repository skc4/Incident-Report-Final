package mobileproject.incidentreport.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.parse.ParsePush;

import mobileproject.incidentreport.R;
import mobileproject.incidentreport.helpers.LogOut;

public class User_Menu extends AppCompatActivity {
    public static final String TAG = User_Menu.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__menu);
        //Log.i(TAG,ParsePush.)

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
    public void reportIncident(View view){
        Intent intent = new Intent(this, Report_A_Incident.class);
        startActivity(intent);
    }
    public void showMap(View view)
    {
        Intent intent = new Intent(this, IncidentMap.class);
        startActivity(intent);
    }
}
