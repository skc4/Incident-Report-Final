package mobileproject.incidentreport.helpers;

/**
 * Created by zthomas on 11/10/15.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.content.SharedPreferences;

import mobileproject.incidentreport.Activities.LoginActivity;
import mobileproject.incidentreport.Activities.OLoginActivity;
public class LogOut extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public Context theContext;
    private String type;
    private Activity activity;
    public void logMeOut() {
        sharedPreferences = theContext.getSharedPreferences("UserLoginPref", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        type = theContext.getSharedPreferences("UserLoginPref", Context.MODE_PRIVATE).getString("TYPE", "user");

        editor.remove("USERNAME");
        editor.remove("isLoggedIn");
        editor.remove("USER_ID");
        editor.remove("TYPE");
        editor.commit();
        returnToLoginScreen();
        finish();
    }
    private void returnToLoginScreen()
    {
       if (type.equals("user"))
       {  try {
           Intent intent = new Intent(activity, LoginActivity.class);
           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           theContext
           .startActivity(intent);
       }
       catch(Exception ex)
       {
           ex.printStackTrace();
       }
       }
        else
       {
           Intent intent = new Intent(activity, OLoginActivity.class);
           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           theContext.startActivity(intent);
       }
    }

    public void  setTheContext(Context context)
    {
        this.theContext = context;
    }
    public void setActivity(Activity active)
    {
        this.activity = active;
    }
}
