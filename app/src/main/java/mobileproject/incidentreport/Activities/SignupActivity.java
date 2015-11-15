package mobileproject.incidentreport.Activities;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobileproject.incidentreport.R;
import mobileproject.incidentreport.helpers.ConfigApp;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private boolean isUser = false;

    @Bind(R.id.input_phonenumber) EditText _phonenumber;
    @Bind(R.id.input_username) EditText _usernameText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_signup) Button _signupButton;
    @Bind(R.id.link_login) TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        sharedPreferences = getApplicationContext().getSharedPreferences(ConfigApp.USER_LOGIN_PREF, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_AppBarOverlay);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String phonenumber = _phonenumber.getText().toString();
        String username = _usernameText.getText().toString();
        String password = _passwordText.getText().toString();

        // TODO: Implement your own signup logic here.
        new AuthUser(username,password,phonenumber).execute();
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        if(isUser){
                            editor.putString("USERNAME", _usernameText.getText().toString());
                            editor.putBoolean("isLoggedIn", true);
                            editor.putString("TYPE","user");
                            editor.commit();
                            onSignupSuccess();
                        }else{
                            onSignupFailed();
                        }
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success

                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        Intent intent = new Intent(this,User_Menu.class);
        startActivity(intent);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _phonenumber.getText().toString();
        String username = _usernameText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty() ) {
            _phonenumber.setError("enter a valid name");
            valid = false;
        } else {
            _phonenumber.setError(null);
        }

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
        private String phonenumber;
        public AuthUser(String user, String pass,String phonenumber){
            this.user = user;
            this.pass = pass;
            this.phonenumber=phonenumber;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(ConfigApp.database_url, ConfigApp.database_user, ConfigApp.database_pass);
                String queryString = "select username, user_id from tbl_users WHERE username='"+user+"' AND password='"+pass+"';";

                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(queryString);
                if(rs.next()){
                    onSignupFailed();
                    return null;
                }

                queryString = "INSERT INTO tbl_users (user_id,username, password,phonenumber) VALUES (NULL,'"+user+"','"+pass+"','"+phonenumber+"');";
                st.executeUpdate(queryString);

                queryString = "SELECT user_id FROM tbl_users WHERE username='"+user+"';";
                rs=st.executeQuery(queryString);

                if(rs.next()){
                    editor.putInt("USER_ID", rs.getInt("user_id"));
                    editor.commit();
                    isUser = true;
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