package mobileproject.incidentreport.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParsePush;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobileproject.incidentreport.R;
import mobileproject.incidentreport.helpers.ConfigApp;

public class OLoginActivity extends AppCompatActivity {
    private static final String TAG = "OLoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private boolean isUser = false;


    @Bind(R.id.input_username) EditText _usernameText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_login) Button _loginButton;
    @Bind(R.id.link_back) TextView _backLink;
    @Bind(R.id.link_signup) TextView _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ologin);
        ButterKnife.bind(this);
        sharedPreferences = getApplicationContext().getSharedPreferences(ConfigApp.USER_LOGIN_PREF, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _backLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Go back to normal login page
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Please call 800-911-9111", Toast.LENGTH_LONG).show();

                /*// Go back to normal login page
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                */
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(OLoginActivity.this,
                R.style.AppTheme_AppBarOverlay);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        final String username = _usernameText.getText().toString();
        final String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.
        new AuthUser(username,password).execute();
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        if(isUser){
                            ParsePush.subscribeInBackground(username);
                            editor.putString("USERNAME", _usernameText.getText().toString());
                            editor.putBoolean("isLoggedIn",true);
                            editor.commit();
                            if (username.equalsIgnoreCase("dispatch")) {
                                editor.putString("TYPE","dispatch");
                                editor.commit();
                                onDispatchLoginSuccess();

                            }else {
                                editor.putString("TYPE","officer");
                                editor.commit();
                                onLoginSuccess();
                            }

                        }else{
                            onLoginFailed();
                        }
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onDispatchLoginSuccess(){
        _loginButton.setEnabled(true);
        Intent intent = new Intent(this,IncidentList.class);
        startActivity(intent);
        finish();
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        Intent intent = new Intent(this,Officer_Menu.class);
        startActivity(intent);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String username = _usernameText.getText().toString();
        String password = _passwordText.getText().toString();

        if (username.isEmpty() ) {
            _usernameText.setError("enter a valid username");
            valid = false;
        } else {
            _usernameText.setError(null);
        }

        if (password.isEmpty() ) {
            _passwordText.setError("enter a valid password");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
    private class AuthUser extends AsyncTask<Void, Void, Void> {
        private final String user;
        private final String pass;
        public AuthUser(String user, String pass){
            this.user = user;
            this.pass = pass;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(ConfigApp.database_url, ConfigApp.database_user, ConfigApp.database_pass);
                String queryString = "SELECT username, officer_id FROM tbl_officers WHERE username='"+user+"' AND password='"+pass+"';";

                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(queryString);
                if(rs.next()){
                    rs.getInt("officer_id");
                    if(!rs.wasNull()){
                        editor.putInt("USER_ID", rs.getInt("officer_id"));
                        editor.commit();
                        isUser = true;
                    }


                }
                con.close();

            }catch (Exception e){
                Log.e(TAG, e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Log.d(TAG,"DID the stuff");


        }
    }
}
