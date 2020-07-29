package com.example.virat.studentmanagementapp.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.virat.studentmanagementapp.R;
import com.example.virat.studentmanagementapp.adapter.assignment_details_adapter;
import com.example.virat.studentmanagementapp.model.assignment_model;
import com.example.virat.studentmanagementapp.other.AppController;
import com.opencsv.CSVWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;
import static com.example.virat.studentmanagementapp.other.AppConfig.URL_ASSIGNMENT_DETAILS;
import static com.example.virat.studentmanagementapp.other.AppConfig.URL_GENERATE_FETCH_ASSIGNMENT_MARKS;
import static com.example.virat.studentmanagementapp.other.AppConfig.URL_STUDENT_LECTURE_DETAILS;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Assignment_Details extends ActionBarActivity implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private String _id;
    private String _faculty_id;
    private String _sem;
    private String _classname;
    private String _subject;
    private String status;
    private List<assignment_model> assignment_model = new ArrayList<assignment_model>();
    ArrayList<String> _Assignment_id = new ArrayList<String>();
    ArrayList<String> _Assignment_name = new ArrayList<String>();
    ArrayList<String> _start_date = new ArrayList<String>();
    ArrayList<String> _due_date = new ArrayList<String>();
    ListView listView;

    ArrayList<String> _name = new ArrayList<String>();
    ArrayList<String> _e_no = new ArrayList<String>();

    ArrayList<String> name_ = new ArrayList<String>();
    ArrayList<String> e_no_ = new ArrayList<String>();
    ArrayList<String> marks_ = new ArrayList<String>();
    ArrayList<String> assignment_name_ = new ArrayList<String>();
    ArrayList<String> assignment_id_ = new ArrayList<String>();
    public boolean bn = true;
    public boolean generate = false;
    final int MY_PERMISSION = 0;


    SwipeRefreshLayout swipeRefreshLayout;
    private assignment_details_adapter adapter;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Set Adapter to listview
        listView = (ListView) findViewById(R.id.list);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        adapter = new assignment_details_adapter(this, assignment_model);
        listView.setAdapter(adapter);

        //Swipe Layout
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
                //   lacturedetails(f_id);
            }
        });


        //Fetch Values From Intent
        Intent i = getIntent();
        _id = i.getExtras().getString("_id");
        _faculty_id = i.getExtras().getString("_faculty_id");
        _sem = i.getExtras().getString("_sem");
        _classname = i.getExtras().getString("_classname");
        _subject = i.getExtras().getString("_subject");
        status = i.getExtras().getString("status");

        //Set title
        getSupportActionBar().setTitle(_subject + " " + _sem + " " + _classname);

        //Flowing Action Button
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Assignment_Details.this, Assignment_Add.class);
                i.putExtra("_faculty_id", _faculty_id);
                i.putExtra("_lecture_id", _id);
                i.putExtra("_sem", _sem);
                i.putExtra("_classname", _classname);
                i.putExtra("_subject", _subject);
                startActivity(i);
            }
        });

        //Listview item click
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(Assignment_Details.this, Assignment_update.class);
                i.putExtra("_faculty_id", _faculty_id);
                i.putExtra("_assignment_id", _Assignment_id.get(position));
                i.putExtra("_lecture_id", _id);
                i.putExtra("_assignment_name", _Assignment_name.get(position));
                i.putExtra("_sem", _sem);
                i.putExtra("_classname", _classname);
                i.putExtra("_subject", _subject);
                startActivity(i);
            }
        });
        //featch Assignment Details
        Assignmentdetails(_faculty_id, _sem, _classname, _subject);

    }

    private void Assignmentdetails(final String _faculty_id, final String _sem, final String _classname, final String _subject) {
        String tag_string_req = "fetch_assignment_details";
        _Assignment_id.clear();
        _due_date.clear();
        _start_date.clear();
        _Assignment_name.clear();
        StringRequest strreq = new StringRequest(Request.Method.POST, URL_ASSIGNMENT_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jobj = new JSONObject(response);
//                    Log.e(TAG, "response" + jobj.toString());
                    JSONArray result = jobj.getJSONArray("result");
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject data = result.getJSONObject(i);
                        assignment_model am = new assignment_model();
                        _Assignment_id.add(data.getString("assignment_id"));
                        am.setAssignment_name(data.getString("assignment_name"));
                        _Assignment_name.add(data.getString("assignment_name"));
                        _start_date.add(data.getString("start_date"));
                        am.setStart_date(data.getString("start_date"));
                        _due_date.add(data.getString("due_date"));
                        am.setDue_date(data.getString("due_date"));

                        assignment_model.add(am);
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
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.e(TAG, _faculty_id + "" + _sem + "" + _classname + "" + _subject);
                params.put("_faculty_id", _faculty_id);
                params.put("_lacture_id", _id);
                params.put("_sem", _sem);
                params.put("_classname", _classname);
                params.put("_subject", _subject);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strreq, tag_string_req);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onRefresh() {
        assignment_model.clear();
        swipeRefreshLayout.setRefreshing(true);
        Assignmentdetails(_faculty_id, _sem, _classname, _subject);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater m1 = getMenuInflater();
        m1.inflate(R.menu.actionbar_assignment_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem menu) {
        switch (menu.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_bar_generate_report:
                bn = true;
                // Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    } else {
                        // No explanation needed, we can request the permission.
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION);
                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else {
                    studentDetailslecture(_sem, _classname);
                 //   this.generate_fetch_assignment_marks(_faculty_id, _id);
                    /*this.action_generate_report();*/
                    Log.e(TAG, "1");
                    generate = true;
                }
                return true;
        }
        return super.onOptionsItemSelected(menu);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (!generate) {
                        studentDetailslecture(_sem, _classname);
                        //this.generate_fetch_assignment_marks(_faculty_id, _id);
                        //   this.action_generate_report();
                        Log.e(TAG, "2");
                        generate = true;
                    }
                } else {

                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void studentDetailslecture(final String _sem, final String _classname) {
        _name.clear();
        _e_no.clear();

        String tag_string_req = "req_lacture_details";
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_STUDENT_LECTURE_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray result = jObj.getJSONArray("result");
                    try {
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject collegeData = result.getJSONObject(i);
                            _name.add(collegeData.getString("name"));
                            _e_no.add(collegeData.getString("e_no"));
                        }
                        generate_fetch_assignment_marks(_faculty_id, _id);
                        //     swipeRefreshLayout.setRefreshing(false);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

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
                params.put("sem", _sem);
                params.put("classname", _classname);
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void generate_fetch_assignment_marks(final String _faculty_id, final String _lecture_id) {

        String tag_string_req = "req_generate_ fetch_assignment_marks";
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_GENERATE_FETCH_ASSIGNMENT_MARKS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "All Student Details: ");
                try {
                    generate = false;
                    JSONObject jObj = new JSONObject(response);
                    JSONArray result = jObj.getJSONArray("result");
//                    Log.e(TAG, "Length: " + result.length() + "   Result: " + result.toString());
                    String[] ss = new String[_Assignment_name.size() + 3];
                    try {
                        Log.e(TAG, "Generate Report method");
                        File savedir = Environment.getExternalStorageDirectory();
                        File dir = new File(savedir.getAbsolutePath(), "Student Management");
                        //String date = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
                        if (!dir.exists()) {
                            if (dir.mkdirs()) {
                                Log.e(TAG, "created Directory----------------");
                            } else {
                                Log.e(TAG, "Failed to create Directory----------------");
                            }
                        }
                        if (dir.exists()) {
                            //File file = new File(dir, "" + _subject + "_" + _sem + "_" + _classname + "_" + date.toString() + "" + ".csv");
                            File file = new File(dir, "Assignment Marks_" + _subject + "_" + _sem + "_" + _classname + "" + ".csv");
                            try {
                                if (file.exists()) {
                                    file.delete();
                                }
                                file.createNewFile();
                                Log.e(TAG, assignment_id_.toString());
                                Log.e(TAG, name_.toString());
                                CSVWriter writer = new CSVWriter(new FileWriter(file, true), CSVWriter.DEFAULT_SEPARATOR);
                                //Heading
                                ss[0] = "Index";
                                ss[1] = "Enrollment No";
                                ss[2] = "Name";
                                for (int i = 0; i < _Assignment_name.size(); i++) {
                                    ss[3 + i] = _Assignment_name.get(i);
                                }
                                writer.writeNext(ss);
                                writer.flush();

                                for (int j = 0; j < _e_no.size(); j++) {
                                    ss[0] = String.valueOf(j + 1);
                                    ss[1] = _e_no.get(j);
                                    ss[2] = _name.get(j);

                                    for (int i = 0; i < result.length(); i++) {
                                        JSONObject collegeData = result.getJSONObject(i);

                                        if (_e_no.get(j).equals(collegeData.getString("e_no"))) {
                                            for (int k = 0; k < _Assignment_id.size(); k++) {
                                                if (_Assignment_id.get(k).equals(collegeData.getString("assignment_id")))
                                                    ss[3 + k] = collegeData.getString("marks");
                                            }
                                        }
                                    }
                                    writer.writeNext(ss);
                                    writer.flush();
                                    /*for (int i = 0; i < ss.length; i++) {
                                        Log.e(TAG, ss[i]);
                                    }*/
                                    //Code Here For GENERATE Report
                                }
                                writer.close();
                                Snackbar.make(getWindow().getDecorView().getRootView(), "Report Saved in Storage", Snackbar.LENGTH_LONG).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } catch (
                        JSONException e
                        )

                {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }

                , new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, " Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        )

        {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("_faculty_id", _faculty_id);
                params.put("_lecture_id", _lecture_id);
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().

                addToRequestQueue(strReq, tag_string_req);

    }

}
