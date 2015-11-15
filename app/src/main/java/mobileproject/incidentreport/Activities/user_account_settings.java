package mobileproject.incidentreport.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.ToggleButton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import mobileproject.incidentreport.R;
import mobileproject.incidentreport.helpers.ConfigApp;
import mobileproject.incidentreport.helpers.LogOut;

public class user_account_settings extends AppCompatActivity {

    private  String username;
    private String firstname;
    private String lastname;
    private String phoneno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account_settings);
        initToggleButton();
        initSaveButton();
        setForEditing(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
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
    private void connect()
    {

    }
    private void setForEditing(boolean enabled) {
        EditText editName = (EditText) findViewById(R.id.editName);
        EditText editLastName = (EditText) findViewById(R.id.editLastName);
        EditText editPhone = (EditText) findViewById(R.id.editCell);
        EditText editUsername = (EditText) findViewById(R.id.editUsername);
        Button buttonSave = (Button) findViewById(R.id.buttonSave);

        editName.setEnabled(enabled);

        editPhone.setEnabled(enabled);
        editLastName.setEnabled(enabled);
        editPhone.setEnabled(enabled);
        editUsername.setEnabled(enabled);
        buttonSave.setEnabled(enabled);

        if (enabled) {
            editName.requestFocus();
        } else {
            ScrollView s = (ScrollView) findViewById(R.id.scrollView1);
            s.fullScroll(ScrollView.FOCUS_UP);
            s.clearFocus();
        }

    }

    private void initToggleButton() {
        final ToggleButton editToggle = (ToggleButton) findViewById(R.id.toggleButtonEdit);
        editToggle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                setForEditing(editToggle.isChecked());
            }

        });
    }
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        EditText editName = (EditText) findViewById(R.id.editName);
        imm.hideSoftInputFromWindow(editName.getWindowToken(), 0);

        EditText editLastName = (EditText) findViewById(R.id.editLastName);
        imm.hideSoftInputFromWindow(editLastName.getWindowToken(), 0);

        EditText editCell = (EditText) findViewById(R.id.editCell);
        imm.hideSoftInputFromWindow(editCell.getWindowToken(), 0);

        EditText editUsername = (EditText) findViewById(R.id.editUsername);
        imm.hideSoftInputFromWindow(editUsername.getWindowToken(), 0);
    }
    private void initSaveButton() {
        Button saveButton = (Button) findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hideKeyboard();
                EditText editName = (EditText)findViewById(R.id.editName);
                EditText editLastName = (EditText)findViewById(R.id.editLastName);
                EditText editPhone = (EditText)findViewById(R.id.editCell);
                EditText editUsername = (EditText)findViewById(R.id.editUsername);

                firstname = editName.getText().toString();
                lastname = editLastName.getText().toString();
                username = editUsername.getText().toString();
                phoneno = editPhone.getText().toString();
                ToggleButton editToggle = (ToggleButton) findViewById(R.id.toggleButtonEdit);
                    editToggle.toggle();
                    setForEditing(false);
                new UpdateUser().execute();
                }

        });
    }

    private class UpdateUser extends AsyncTask<Void,Void,Void>

    {
        private int userID;
        protected Void doInBackground(Void...params)
        {
            userID = getSharedPreferences(ConfigApp.USER_LOGIN_PREF, Context.MODE_PRIVATE).getInt("USER_ID",1);

            try
            {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(ConfigApp.database_url, ConfigApp.database_user, ConfigApp.database_pass);
                String queryString = "Update g04dbf15.tbl_users SET firstname = '"+firstname+ "', lastname ='" + lastname + "', phonenumber = '" + phoneno + "', username = '" + username + "' WHERE user_id = "+userID+";";


                Statement st = con.createStatement();
                st.executeUpdate(queryString);
                con.close();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
         return null;
        }
        @Override
        protected void onPostExecute(Void v)
        {

        }
    }
}