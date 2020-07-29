package com.example.virat.studentmanagementapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.virat.studentmanagementapp.R;
import com.example.virat.studentmanagementapp.other.AppConfig;
import com.example.virat.studentmanagementapp.other.AppController;
import com.example.virat.studentmanagementapp.other.SQLiteHandler;
import com.example.virat.studentmanagementapp.other.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Button Btn_Login;
    private SessionManager session;
    private ProgressDialog pDialog;
    private SQLiteHandler db;
    private static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        Btn_Login = (Button) findViewById(R.id.btnLogin);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Login button
        Btn_Login.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String usr = String.valueOf(username.getText());
                String pwd = String.valueOf(password.getText());


                if (!usr.isEmpty() && !pwd.isEmpty()) {
                    // login user
                    Log.e(TAG, "Log in Click event");
                    checkLogin(usr, pwd);
                } else {
                    Snackbar.make(getWindow().getDecorView().getRootView(), "Please Enter Username and Password", Snackbar.LENGTH_SHORT).show();
                }
            }

        });
    }


    //Verify Login Details in DB
    private void checkLogin(final String username, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Login Response: " + response.toString());
                hideDialog();
                try {
                    // Log.e(TAG, String.valueOf(response.length()));
                    JSONObject JObj = new JSONObject(response);
                    boolean error = JObj.getBoolean("error");
                    // Check for error  in json
                    if (!error) {
                        // user successfully logged in

                        // Create login session
                        session.setLogin(true);
                        //Add to SQLite DB
                        JSONObject user = JObj.getJSONObject("user");
                        String uid = user.getString("faculty_id");
                        String username = user.getString("username");
                        String name = user.getString("name");
                        String profile = user.getString("profile");
                        // Inserting row in users table
                        db.addUser(uid, username, name,profile);
                        // Launch main activity
                        Log.e(TAG, "Divert to Main Activity");
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error in login

                        String errorMsg = JObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    Log.e(TAG, "Error Json");
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "---------Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
