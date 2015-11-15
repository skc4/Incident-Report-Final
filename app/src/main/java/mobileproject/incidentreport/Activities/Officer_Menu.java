package mobileproject.incidentreport.Activities;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


import mobileproject.incidentreport.R;
import mobileproject.incidentreport.helpers.ConfigApp;
import mobileproject.incidentreport.helpers.LogOut;


public class Officer_Menu extends AppCompatActivity {
    private final String TAG = Officer_Menu.class.getSimpleName();
    private static SharedPreferences sharedPreferences;
    Animation translateAnim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_officer__menu);
        sharedPreferences = getApplicationContext().getSharedPreferences(ConfigApp.USER_LOGIN_PREF, Context.MODE_PRIVATE);
        /*Beginning Animation*/
        ImageView imageView =  (ImageView) findViewById(R.id.ologo);
        translateAnim = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.translate_bottomup);
        imageView.startAnimation(translateAnim);
        /*END Animation*/

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

    public void loadOfficerIncidents(View v) {

        Intent intent = new Intent(this, Officer_Report.class);
        intent.putExtra("incident_id",sharedPreferences.getInt("CURRENT_INCIDENT",0));
        startActivity(intent);
    }

}
