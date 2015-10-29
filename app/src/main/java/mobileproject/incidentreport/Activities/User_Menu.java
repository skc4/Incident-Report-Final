package mobileproject.incidentreport.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParsePushBroadcastReceiver;

import mobileproject.incidentreport.R;

public class User_Menu extends AppCompatActivity {

    ParsePushBroadcastReceiver incoming;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__menu);
        Parse.initialize(this, "Me5CJw5LT408QL8QGUDdXBMHLeahkSfIujqwlkhR", "dlPUdYIwkU3E2nmvJhpvf5LlQWusToPjkX369V77");
        ParseInstallation.getCurrentInstallation().saveInBackground();
        incoming = new ParsePushBroadcastReceiver();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user__menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void reportIncident(View view){
        Intent intent = new Intent(this, Report_A_Incident.class);
        startActivity(intent);
    }
}
