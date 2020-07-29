package com.example.virat.studentmanagementapp.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import static android.content.ContentValues.TAG;
import static com.example.virat.studentmanagementapp.other.AppConfig.URL_ASSIGNMENT_FETCH_MARKS;
import static com.example.virat.studentmanagementapp.other.AppConfig.URL_ASSIGNMENT_UPDATE_MARKS;
import static com.example.virat.studentmanagementapp.other.AppConfig.URL_STUDENT_LECTURE_DETAILS;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.virat.studentmanagementapp.R;
import com.example.virat.studentmanagementapp.adapter.assignment_update_adapter;
import com.example.virat.studentmanagementapp.adapter.attendance_adapter;
import com.example.virat.studentmanagementapp.model.assignment_model;
import com.example.virat.studentmanagementapp.model.attendance_model;
import com.example.virat.studentmanagementapp.other.AppController;
import com.opencsv.CSVWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Assignment_update extends ActionBarActivity implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private String _faculty_id, _assignment_id, _lecture_id, _assignment_name, _sem, _classname, _subject;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private List<assignment_model> assignment_models = new ArrayList<assignment_model>();
    private assignment_update_adapter adapter;
    ArrayList<String> _name = new ArrayList<String>();
    ArrayList<String> _e_no = new ArrayList<String>();
    public static ArrayList<String> _marks = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_update);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        listView = (ListView) findViewById(R.id.list);
        //Adapter
        adapter = new assignment_update_adapter(this, assignment_models);
        listView.setAdapter(adapter);

        //fetch values From Intent
        Intent i = getIntent();
        _faculty_id = i.getExtras().getString("_faculty_id");
        _assignment_id = i.getExtras().getString("_assignment_id");
        _lecture_id = i.getExtras().getString("_lecture_id");
        _assignment_name = i.getExtras().getString("_assignment_name");
        _sem = i.getExtras().getString("_sem");
        _classname = i.getExtras().getString("_classname");
        _subject = i.getExtras().getString("_subject");

        //Set title
        getSupportActionBar().setTitle(_assignment_name);
        getSupportActionBar().setSubtitle(_subject + " " + _sem + " " + _classname);

        //Clear Array List Oncreate
        _name.clear();
        _e_no.clear();
        _marks.clear();
        //SwipeRefresh layout
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        assignment_fetch_marks(_assignment_id);


    }

    private void assignment_fetch_marks(final String _assignment_id) {
        assignment_models.clear();
        _name.clear();
        _e_no.clear();
        _marks.clear();
        String tag_string_req = "req_fetch_assignmet_marks";
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_ASSIGNMENT_FETCH_MARKS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "All Student Details: ");
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray result = jObj.getJSONArray("result");
                    Log.e(TAG, "Length: " + result.length() + "   Result: " + result.toString());
                    try {
                        for (int i = 0; i < result.length(); i++) {
                            assignment_model am = new assignment_model();
                            JSONObject collegeData = result.getJSONObject(i);

                            am.setName(collegeData.getString("name"));
                            _name.add(collegeData.getString("name"));

                            am.setE_no(collegeData.getString("e_no"));
                            _e_no.add(collegeData.getString("e_no"));

                            am.setMarks(collegeData.getString("marks"));
                            _marks.add(collegeData.getString("marks"));
                            assignment_models.add(am);
                        }
                        adapter.notifyDataSetChanged();
                        //     swipeRefreshLayout.setRefreshing(false);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    adapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, " Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("_assignment_id", _assignment_id);
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater m1 = getMenuInflater();
        m1.inflate(R.menu.actionbar_assignment_update, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem menu) {
        switch (menu.getItemId()) {
            case android.R.id.home:
                //   NavUtils.navigateUpFromSameTask(Assignment_Add.this);
                Assignment_update.this.finish();
                return true;
            case R.id.action_bar_update:

                Log.e(TAG, _marks.toString());
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Update Marks?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                return true;

        }
        return super.onOptionsItemSelected(menu);
    }

    //dIALOG YES NO FOR SAVE ATTENDANCE
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int choice) {
            switch (choice) {
                case DialogInterface.BUTTON_POSITIVE:
                    assignment_update_marks(_assignment_id, _name, _e_no, _marks);
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    Log.e(TAG, "Cancel");
                    break;
            }
        }
    };

    private void assignment_update_marks(final String _assignment_id, final ArrayList<String> _name, final ArrayList<String> _e_no, final ArrayList<String> _marks) {
        String tag_string_req = "req_assignment_update_marks";
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_ASSIGNMENT_UPDATE_MARKS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Response " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    try {
                        Log.e(TAG, jObj.getString("success"));
                        Snackbar.make(getWindow().getDecorView().getRootView(), "Marks Updated Successfully", Snackbar.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        Log.e(TAG, jObj.getString("error_msg"));
                        Snackbar.make(getWindow().getDecorView().getRootView(), "Error in Updating", Snackbar.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, " Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                for (int i = 0; i < _e_no.size(); i++) {
                    params.put("assignment_id", _assignment_id);
                    params.put("name[" + i + "]", _name.get(i));
                    params.put("e_no[" + i + "]", _e_no.get(i));
                    params.put("marks[" + i + "]", _marks.get(i));
                }
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    @Override
    public void onRefresh() {
        assignment_models.clear();
        swipeRefreshLayout.setRefreshing(true);
        assignment_fetch_marks(_assignment_id);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
