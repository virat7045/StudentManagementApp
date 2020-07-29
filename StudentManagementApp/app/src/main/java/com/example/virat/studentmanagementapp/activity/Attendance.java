package com.example.virat.studentmanagementapp.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.virat.studentmanagementapp.R;
import com.example.virat.studentmanagementapp.adapter.attendance_adapter;
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

import static android.content.ContentValues.TAG;
import static com.example.virat.studentmanagementapp.other.AppConfig.URL_GENERATE_FETCH_LAB_MARKS;
import static com.example.virat.studentmanagementapp.other.AppConfig.URL_GENERATE_FETCH_LECTURE_MARKS;
import static com.example.virat.studentmanagementapp.other.AppConfig.URL_SAVE_LAB_ATTENDANCE;
import static com.example.virat.studentmanagementapp.other.AppConfig.URL_SAVE_LECTURE_ATTENDANCE;
import static com.example.virat.studentmanagementapp.other.AppConfig.URL_STUDENT_LAB_DETAILS;
import static com.example.virat.studentmanagementapp.other.AppConfig.URL_STUDENT_LECTURE_DETAILS;
import static com.example.virat.studentmanagementapp.other.AppConfig.URL_UPDATE_ATTENDANCE_DETAILS;
import static com.example.virat.studentmanagementapp.other.AppConfig.URL_UPDATE_LAB_ATTENDANCE;
import static com.example.virat.studentmanagementapp.other.AppConfig.URL_UPDATE_LAB_ATTENDANCE_DETAILS;
import static com.example.virat.studentmanagementapp.other.AppConfig.URL_UPDATE_LECTURE_ATTENDANCE;

public class Attendance extends ActionBarActivity implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private String _id;
    private String _faculty_id;
    private String _sem;
    private String _classname;
    private String _subject;
    private String status;
    final int MY_PERMISSION = 0;
    private String _activity;
    private String _update_date;
    private ListView listView;
    public boolean generate = false;
    private List<attendance_model> lectureList = new ArrayList<attendance_model>();
    private attendance_adapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    public static CheckBox Button_checkall;
    ArrayList<String> _name = new ArrayList<String>();
    ArrayList<String> _e_no = new ArrayList<String>();
    ArrayList<String> _u_id = new ArrayList<String>();
    ArrayList<String> _date = new ArrayList<String>();
    public static ArrayList<String> _selected = new ArrayList<String>();
    private String Check_Status;
    int myear;
    int mmonth;
    int mday;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        //Back Butoon in Action Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        listView = (ListView) findViewById(R.id.list);
        Button_checkall = (CheckBox) findViewById(R.id.check_all);
        Button_checkall.setChecked(false);
        //Adapter
        adapter = new attendance_adapter(this, lectureList);
        listView.setAdapter(adapter);
        Check_Status = "unselect";

        //date current
        //Get Intent
        Intent i = getIntent();
        //  Log.e(TAG, "Log");

        //Clear Array List Oncreate
        _selected.clear();
        _name.clear();
        _e_no.clear();
        _u_id.clear();

        //Fetch Values From Intent
        _id = i.getExtras().getString("_id");
        _faculty_id = i.getExtras().getString("_faculty_id");
        _sem = i.getExtras().getString("_sem");
        _classname = i.getExtras().getString("_classname");
        _subject = i.getExtras().getString("_subject");
        status = i.getExtras().getString("status");

        //Set title
        getSupportActionBar().setTitle(_subject + " " + _sem + " " + _classname);
        //  Log.e(TAG, _id + " " + status);
        //Fetch students  DATA From DB+
        if (status.equals("lecture")) {
            Log.e(TAG, _sem + " " + _classname);
            studentDetailslecture(_sem, _classname);
        } else if (status.equals("lab")) {
            Log.e(TAG, _sem + " " + _classname);
            studentDetailslab(_sem, _classname);
        }

        //SwipeLayout Listener
        //Onrefresh
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
                //   lacturedetails(f_id);
            }
        });

        Button_checkall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attendance_model lm;
                Log.e(TAG,_selected.toString());
                if (Button_checkall.isChecked()) {
                    for (int i = 0; i < _selected.size(); i++) {
                        lm = lectureList.get(i);
                        lm.setCheckbox_value(true);
                        _selected.set(i, "P");

                    }
                    Check_Status = "select";
                } else if (!Button_checkall.isChecked()) {
                    for (int i = 0; i < _selected.size(); i++) {
                        lm = lectureList.get(i);
                        lm.setCheckbox_value(false);
                        _selected.set(i, "A");
                    }
                    Check_Status = "unselect";
                }

                adapter.notifyDataSetChanged();
            }
        });

    }

    //dIALOG YES NO FOR SAVE ATTENDANCE
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int choice) {
            switch (choice) {
                case DialogInterface.BUTTON_POSITIVE:
                    if (_activity.equals("save")) {
                        Log.e(TAG, "Save");
                        if (status.equals("lecture")) {
                            save_lecture_attendance(_faculty_id, _id, _e_no, _name, _sem, _classname, _subject, _selected);
                        } else if (status.equals("lab")) {
                            save_lab_attendance(_faculty_id, _id, _e_no, _name, _sem, _classname, _subject, _selected);
                        }
                    } else if (_activity.equals("update")) {
                        Log.e(TAG, "update");
                        if (status.equals("lecture")) {
                            update_lecture_attendance(_u_id, _selected);
                        } else if (status.equals("lab")) {
                            update_lab_attendance(_u_id, _selected);
                        }
                    }
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    Log.e(TAG, "Cancel");
                    break;
            }
        }
    };


    public void action_update_attendance() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datepicker = new DatePickerDialog(this, R.style.CustomDatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //
                Log.e(TAG, " action Update  attendance");
                Log.e(TAG, _e_no.toString());
                myear = year;
                mmonth = month + 1;
                mday = dayOfMonth;
                StringBuilder sb = new StringBuilder();
                sb.append(mday + "-");
                sb.append(mmonth + "-");
                sb.append(myear);
                _update_date = sb.toString();
                Log.e(TAG, sb.toString());
                if (status.equals("lecture")) {
                    Log.e(TAG, "....");
                    update_lecture_details(_faculty_id, _id, _sem, _classname, _subject, _update_date);
                    Log.e(TAG, "....");
                } else if (status.equals("lab")) {
                    update_lab_details(_faculty_id, _id, _sem, _classname, _subject, _update_date);

                }


            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datepicker.setTitle("Select Date");
        datepicker.show();
        datepicker.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                Log.e(TAG, "Onclick");
                return false;
            }
        });
        datepicker.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Log.e(TAG, "cancel click");
            }
        });

    }

    private void studentDetailslecture(final String _sem, final String _classname) {
        String tag_string_req = "req_lacture_details";
        _activity = "save";
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_STUDENT_LECTURE_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //   Log.e(TAG, "Response");
                // Log.e(TAG, "All Student Details: " + response.toString());
                try {
                    lectureList.clear();
                    _selected.clear();
                    _name.clear();
                    _e_no.clear();
                    JSONObject jObj = new JSONObject(response);
                    //       Log.e(TAG, "jObj: " + jObj.toString());
                    JSONArray result = jObj.getJSONArray("result");

                    //  Log.e(TAG, "Length: " + result.length() + "   Result: " + result.toString());
                    try {
                        for (int i = 0; i < result.length(); i++) {
                            attendance_model lm = new attendance_model();
                            JSONObject collegeData = result.getJSONObject(i);

                            lm.setName(collegeData.getString("name"));
                            _name.add(collegeData.getString("name"));

                            lm.setE_no(collegeData.getString("e_no"));
                            _e_no.add(collegeData.getString("e_no"));

                            lm.setCheckbox_value(false);
                            _selected.add("A");

                            lectureList.add(lm);
                        }
                        adapter.notifyDataSetChanged();
                        //     swipeRefreshLayout.setRefreshing(false);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    adapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                    if(generate){
                        generate_fetch_lecture_marks(_faculty_id, _id);
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

    private void studentDetailslab(final String _sem, final String _classname) {
        String tag_string_req = "req_lacture_details";
        _activity = "save";
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_STUDENT_LAB_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //   Log.e(TAG, "Response");
                // Log.e(TAG, "All Student Details: " + response.toString());
                try {
                    lectureList.clear();
                    _selected.clear();
                    _name.clear();
                    _e_no.clear();
                    JSONObject jObj = new JSONObject(response);
                    //       Log.e(TAG, "jObj: " + jObj.toString());
                    JSONArray result = jObj.getJSONArray("result");

                    // Log.e(TAG, "Length: " + result.length() + "   Result: " + result.toString());
                    try {
                        for (int i = 0; i < result.length(); i++) {
                            attendance_model lm = new attendance_model();
                            JSONObject collegeData = result.getJSONObject(i);

                            lm.setName(collegeData.getString("name"));
                            _name.add(collegeData.getString("name"));

                            lm.setE_no(collegeData.getString("e_no"));
                            _e_no.add(collegeData.getString("e_no"));

                            lm.setCheckbox_value(false);
                            _selected.add("A");

                            lectureList.add(lm);
                        }
                        adapter.notifyDataSetChanged();
                        //     swipeRefreshLayout.setRefreshing(false);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    adapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                    if (generate){
                        generate_fetch_lab_marks(_faculty_id, _id);
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


    private void save_lecture_attendance(final String faculty_id, final String id, final ArrayList<String> e_no, final ArrayList<String> name, final String sem, final String classname, final String subject, final ArrayList<String> selected) {
        String tag_string_req = "req_save_lecture_attendance";
        // Log.e(TAG, "Inside Save");
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_SAVE_LECTURE_ATTENDANCE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    try {
                        Log.e(TAG, jObj.getString("success"));
                        Snackbar.make(getWindow().getDecorView().getRootView(), "Attendance Saved Successfully", Snackbar.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        Log.e(TAG, jObj.getString("error_msg"));
                        Snackbar.make(getWindow().getDecorView().getRootView(), "Attendance Already Saved Try Update", Snackbar.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, " Error: " + error.getMessage());
            }
        }
        ) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                for (int i = 0; i < e_no.size(); i++) {
                    params.put("faculty_id", faculty_id);
                    params.put("lecture_id", id);
                    params.put("e_no[" + i + "]", e_no.get(i));
                    params.put("name[" + i + "]", name.get(i));
                    params.put("sem", sem);
                    params.put("classname", classname);
                    params.put("subject", subject);
                    params.put("status[" + i + "]", selected.get(i));
                }
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void save_lab_attendance(final String faculty_id, final String id, final ArrayList<String> e_no, final ArrayList<String> name, final String sem, final String classname, final String subject, final ArrayList<String> selected) {
        String tag_string_req = "req_save_lab_attendance";
        Log.e(TAG, "Inside Save");
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_SAVE_LAB_ATTENDANCE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    try {
                        Log.e(TAG, jObj.getString("success"));
                        Snackbar.make(getWindow().getDecorView().getRootView(), "Attendance Saved Successfully", Snackbar.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e(TAG, jObj.getString("error_msg"));
                        Snackbar.make(getWindow().getDecorView().getRootView(), "Attendance Already Saved Try Update", Snackbar.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();


                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, " Error: " + error.getMessage());
            }
        }
        ) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                for (int i = 0; i < e_no.size(); i++) {
                    params.put("faculty_id", faculty_id);
                    params.put("lab_id", id);
                    params.put("e_no[" + i + "]", e_no.get(i));
                    params.put("name[" + i + "]", name.get(i));
                    params.put("sem", sem);
                    params.put("batch", classname);
                    params.put("subject", subject);
                    params.put("status[" + i + "]", selected.get(i));
                }
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void update_lecture_details(final String _faculty_id, final String _id, final String _sem, final String _classname, final String _subject, final String _date) {
        String tag_string_req = "update_lacture_details";
        Log.e(TAG, "Update lecture details");
        Log.e(TAG, _e_no.toString());
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_UPDATE_ATTENDANCE_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    _activity = "update";
                    _selected.clear();
                    _name.clear();
                    _e_no.clear();
                    _u_id.clear();
                    Button_checkall.setChecked(false);
                    lectureList.clear();
                    JSONObject jObj = new JSONObject(response);
                    JSONArray result = jObj.getJSONArray("result");
                    //  Log.e(TAG, result.toString());
                    try {
                        for (int i = 0; i < result.length(); i++) {
                            attendance_model lm = new attendance_model();
                            _selected.add("A");
                            JSONObject collegeData = result.getJSONObject(i);
                            _u_id.add(i, collegeData.getString("u_id"));
                            lm.setName(collegeData.getString("name"));
                            _name.add(collegeData.getString("name"));
                            lm.setE_no(collegeData.getString("e_no"));
                            _e_no.add(collegeData.getString("e_no"));

                            if (collegeData.getString("status").equals("P")) {
                                lm.setCheckbox_value(true);
                                _selected.set(i, "P");
                            }
                            else if (collegeData.getString("status").equals("A")) {
                                lm.setCheckbox_value(false);
                                _selected.set(i, "A");
                            }
                            lectureList.add(lm);
                        }
                        Log.e(TAG, "1111111");
                        adapter.notifyDataSetChanged();
                        //     swipeRefreshLayout.setRefreshing(false);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e(TAG, "errors");
                    }
                    Log.e(TAG, "22222");
                    adapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                    Log.e(TAG, "Updated lecture details");
                    Log.e(TAG, _e_no.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "error");
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
                Log.e(TAG, _faculty_id + " " + _id + " " + _sem + " " + _classname + " " + _subject + " " + _date);
                params.put("faculty_id", _faculty_id);
                params.put("lecture_id", _id);
                params.put("sem", _sem);
                params.put("classname", _classname);
                params.put("subject", _subject);
                params.put("date", _date);
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void update_lecture_attendance(final ArrayList<String> _u_id, final ArrayList<String> _selected) {
        String tag_string_req = "req_save_lecture_attendance";
        // Log.e(TAG, "Inside Save");
        Log.e(TAG, _u_id.toString());
        Log.e(TAG, _selected.toString());
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_UPDATE_LECTURE_ATTENDANCE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.e(TAG, jObj.getString("success"));
                    Snackbar.make(getWindow().getDecorView().getRootView(), "Attendance Updated Successfully", Snackbar.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, " Error: " + error.getMessage());
            }
        }
        ) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                for (int i = 0; i < _selected.size(); i++) {
                    params.put("u_id[" + i + "]", _u_id.get(i));
                    params.put("status[" + i + "]", _selected.get(i));
                }
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void update_lab_details(final String _faculty_id, final String _id, final String _sem, final String _classname, final String _subject, final String _date) {
        String tag_string_req = "update_lacture_details";

        StringRequest strReq = new StringRequest(Request.Method.POST, URL_UPDATE_LAB_ATTENDANCE_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    _activity = "update";
                    _selected.clear();
                    _name.clear();
                    _e_no.clear();
                    _u_id.clear();
                    Button_checkall.setChecked(false);
                    lectureList.clear();                 JSONObject jObj = new JSONObject(response);
                    JSONArray result = jObj.getJSONArray("result");
                    Log.e(TAG, result.toString());
                    try {
                        for (int i = 0; i < result.length(); i++) {
                            attendance_model lm = new attendance_model();
                            _selected.add("A");
                            lm.setCheckbox_value(false);
                            JSONObject collegeData = result.getJSONObject(i);
                            _u_id.add(i, collegeData.getString("u_id"));
                            lm.setName(collegeData.getString("name"));
                            _name.add(collegeData.getString("name"));
                            lm.setE_no(collegeData.getString("e_no"));
                            _e_no.add(collegeData.getString("e_no"));
                            if (collegeData.getString("status").equals("P")) {
                                lm.setCheckbox_value(true);
                                _selected.set(i, "P");
                            }
                            lectureList.add(lm);
                        }
                        adapter.notifyDataSetChanged();
                        //     swipeRefreshLayout.setRefreshing(false);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e(TAG, "errors");
                    }
                    adapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                    /*if (generate) {
                        generatereport();
                        generate = false;
                    }*/

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "error");
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
                Log.e(TAG, _faculty_id + " " + _id + " " + _sem + " " + _classname + " " + _subject + " " + _date);
                params.put("faculty_id", _faculty_id);
                params.put("lab_id", _id);
                params.put("sem", _sem);
                params.put("batch", _classname);
                params.put("subject", _subject);
                params.put("date", _date);
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void update_lab_attendance(final ArrayList<String> _u_id, final ArrayList<String> _selected) {
        String tag_string_req = "req_save_lecture_attendance";
        // Log.e(TAG, "Inside Save");
        Log.e(TAG, _u_id.toString());
        Log.e(TAG, _selected.toString());
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_UPDATE_LAB_ATTENDANCE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.e(TAG, jObj.getString("success"));
                    Snackbar.make(getWindow().getDecorView().getRootView(), "Attendance Updated Successfully", Snackbar.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, " Error: " + error.getMessage());
            }
        }
        ) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                for (int i = 0; i < _selected.size(); i++) {
                    params.put("u_id[" + i + "]", _u_id.get(i));
                    params.put("status[" + i + "]", _selected.get(i));
                }
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onRefresh() {
        lectureList.clear();
        _selected.clear();
        _name.clear();
        _e_no.clear();
        _u_id.clear();
        Button_checkall.setChecked(false);
        swipeRefreshLayout.setRefreshing(true);
        if (_activity.equals("update")) {
            if (status.equals("lecture")) {
                update_lecture_details(_faculty_id, _id, _sem, _classname, _subject, _update_date);
            } else if (status.equals("lab")) {
                update_lab_details(_faculty_id, _id, _sem, _classname, _subject, _update_date);
            }
        } else if (_activity.equals("save")) {
            if (status.equals("lecture")) {
                studentDetailslecture(_sem, _classname);
            } else if (status.equals("lab")) {
                studentDetailslab(_sem, _classname);
            }
        }

    }

    public void OnBackPressed() {
        lectureList.clear();
        _selected.clear();
        _u_id.clear();
        _name.clear();
        _e_no.clear();

        Log.e(TAG, "Back Pressed");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater m1 = getMenuInflater();
        m1.inflate(R.menu.actionbar_attendance, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //TITLE BAR SELECT OPTION
    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;

            case R.id.action_bar_save:
                //   Log.e(TAG, "save Button");
                //   Log.e(TAG, status.toString());
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Save Attendance?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                Log.e(TAG, "Dialog Builder");
                //   save_lecture_attendance(_faculty_id, _id, _e_no, _name, _sem, _classname, _subject, _selected);

                return true;

            case R.id.action_bar_generate_report:
                Log.e(TAG, "Generate report");
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
                    Log.e(TAG, "generate 1");
                    if (status.equals("lecture")) {
                        studentDetailslecture(_sem, _classname);
                    } else if (status.equals("lab")) {
                        studentDetailslab(_sem, _classname);
                    }
                    generate = true;
                }
                return true;
            case R.id.action_bar_update_attendance:
                Log.e(TAG, "Update Attendance");
                Log.e(TAG, "..............");
                this.action_update_attendance();
                Log.e(TAG, "..............");
                return true;
        }
        return super.onOptionsItemSelected(item);
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
                        if (status.equals("lecture")) {
                            studentDetailslecture(_sem, _classname);
                        } else if (status.equals("lab")) {
                            studentDetailslab(_sem, _classname);
                        }
                        generate = true;
                    }
                } else {

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void generate_fetch_lecture_marks(final String _faculty_id, final String _lecture_id) {
        String tag_string_req = "update_lacture_details";
        Log.e(TAG, "Update lecture details");
        Log.e(TAG, _e_no.toString());

        StringRequest strReq = new StringRequest(Request.Method.POST, URL_GENERATE_FETCH_LECTURE_MARKS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    _date.clear();
                    generate = false;
                    JSONObject jObj = new JSONObject(response);
                    JSONArray result = jObj.getJSONArray("result");
                    JSONObject j1 = result.getJSONObject(0);
                    _date.add(j1.getString("date"));
                    for (int i = 0; i < result.length(); i++) {
                        int flag = 0;
                        JSONObject collegeData = result.getJSONObject(i);
                        for (int j = 0; j < _date.size(); j++) {

                            if (collegeData.getString("date").equals(_date.get(j))) {
                                flag = 1;
                            }

                        }
                        if (flag == 0) {
                            _date.add(collegeData.getString("date"));

                        }


                    }


                    Log.e(TAG, "Length: " + result.length() + "   Result: " + result.toString());
                    String[] ss = new String[_date.size() + 3];
                    try {
                        Log.e(TAG, "Generate Report method");
                        File savedir = Environment.getExternalStorageDirectory();
                        File dir = new File(savedir.getAbsolutePath(), "Student Management");
                        //String date = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
                        Log.e(TAG, "1nd");
                        if (!dir.exists()) {
                            if (dir.mkdirs()) {
                                Log.e(TAG, "3nd");
                                Log.e(TAG, "created Directory----------------");
                            } else {
                                Log.e(TAG, "Failed to create Directory----------------");
                            }
                        }
                        if (dir.exists()) {
                            Log.e(TAG, "2nd");
                            //File file = new File(dir, "" + _subject + "_" + _sem + "_" + _classname + "_" + date.toString() + "" + ".csv");
                            File file = new File(dir, "Lecture Attendance _" + _subject + "_" + _sem + "_" + _classname + "" + ".csv");
                            try {
                                Log.e(TAG, "4th");
                                Log.e(TAG, _date.toString());
                                Log.e(TAG, _e_no.toString());
                                if (file.exists()) {
                                    file.delete();
                                }
                                file.createNewFile();
                                CSVWriter writer = new CSVWriter(new FileWriter(file, true), CSVWriter.DEFAULT_SEPARATOR);
                                //Heading
                                ss[0] = "Index";
                                ss[1] = "Enrollment No";
                                ss[2] = "Name";
                                for (int i = 0; i < _date.size(); i++) {
                                    ss[3 + i] = _date.get(i);
                                }
                                writer.writeNext(ss);
                                writer.flush();

                                for (int j = 0; j < _e_no.size(); j++) {
                                    Log.e(TAG, _e_no.get(j));
                                    ss[0] = String.valueOf(j + 1);
                                    ss[1] = _e_no.get(j);
                                    Log.e(TAG, _e_no.get(j));
                                    ss[2] = _name.get(j);
                                    Log.e(TAG, _name.get(j));

                                    for (int i = 0; i< result.length(); i++) {
                                        JSONObject collegeData = result.getJSONObject(i);

                                        if (_e_no.get(j).equals(collegeData.getString("e_no"))) {
                                            for (int k = 0; k < _date.size(); k++) {
                                                if (_date.get(k).equals(collegeData.getString("date")))
                                                {
                                                    ss[3 + k] = collegeData.getString("status");
                                                    Log.e(TAG, _date.get(k) + "  date " + collegeData.getString("date")+" status"+collegeData.getString("status"));
                                                }
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
                                Log.e(TAG, "Done...........");
                                Snackbar.make(getWindow().getDecorView().getRootView(), "Report Saved in Storage", Snackbar.LENGTH_LONG).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "error");
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
                params.put("_faculty_id", _faculty_id);
                params.put("_lecture_id", _lecture_id);
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    //private void generate_fetch_lab_marks(final String _faculty_id, final String _lab_id) {

    private void generate_fetch_lab_marks(final String _faculty_id, final String _lab_id) {
        String tag_string_req = "update_lacture_details";
        Log.e(TAG, "Update lecture details");
        Log.e(TAG, _e_no.toString());

        StringRequest strReq = new StringRequest(Request.Method.POST, URL_GENERATE_FETCH_LAB_MARKS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    _date.clear();
                    generate = false;
                    JSONObject jObj = new JSONObject(response);
                    JSONArray result = jObj.getJSONArray("result");
                    JSONObject j1 = result.getJSONObject(0);
                    _date.add(j1.getString("date"));
                    for (int i = 0; i < result.length(); i++) {
                        int flag = 0;
                        JSONObject collegeData = result.getJSONObject(i);
                        for (int j = 0; j < _date.size(); j++) {

                            if (collegeData.getString("date").equals(_date.get(j))) {
                                flag = 1;
                            }

                        }
                        if (flag == 0) {
                            _date.add(collegeData.getString("date"));

                        }


                    }


                    Log.e(TAG, "Length: " + result.length() + "   Result: " + result.toString());
                    String[] ss = new String[_date.size() + 3];
                    try {
                        Log.e(TAG, "Generate Report method");
                        File savedir = Environment.getExternalStorageDirectory();
                        File dir = new File(savedir.getAbsolutePath(), "Student Management");
                        //String date = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
                        Log.e(TAG, "1nd");
                        if (!dir.exists()) {
                            if (dir.mkdirs()) {
                                Log.e(TAG, "3nd");
                                Log.e(TAG, "created Directory----------------");
                            } else {
                                Log.e(TAG, "Failed to create Directory----------------");
                            }
                        }
                        if (dir.exists()) {
                            Log.e(TAG, "2nd");
                            //File file = new File(dir, "" + _subject + "_" + _sem + "_" + _classname + "_" + date.toString() + "" + ".csv");
                            File file = new File(dir, "Lab Attendance _" + _subject + "_" + _sem + "_" + _classname + "" + ".csv");
                            try {
                                Log.e(TAG, "4th");
                                Log.e(TAG, _date.toString());
                                Log.e(TAG, _e_no.toString());
                                if (file.exists()) {
                                    file.delete();
                                }
                                file.createNewFile();
                                CSVWriter writer = new CSVWriter(new FileWriter(file, true), CSVWriter.DEFAULT_SEPARATOR);
                                //Heading
                                ss[0] = "Index";
                                ss[1] = "Enrollment No";
                                ss[2] = "Name";
                                for (int i = 0; i < _date.size(); i++) {
                                    ss[3 + i] = _date.get(i);
                                }
                                writer.writeNext(ss);
                                writer.flush();

                                for (int j = 0; j < _e_no.size(); j++) {
                                    Log.e(TAG, _e_no.get(j));
                                    ss[0] = String.valueOf(j + 1);
                                    ss[1] = _e_no.get(j);
                                    Log.e(TAG, _e_no.get(j));
                                    ss[2] = _name.get(j);
                                    Log.e(TAG, _name.get(j));

                                    for (int i = 0; i< result.length(); i++) {
                                        JSONObject collegeData = result.getJSONObject(i);

                                        if (_e_no.get(j).equals(collegeData.getString("e_no"))) {
                                            for (int k = 0; k < _date.size(); k++) {
                                                if (_date.get(k).equals(collegeData.getString("date")))
                                                {
                                                    ss[3 + k] = collegeData.getString("status");
                                                    Log.e(TAG, _date.get(k) + "  date " + collegeData.getString("date")+" status"+collegeData.getString("status"));
                                                }
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
                                Log.e(TAG, "Done...........");
                                Snackbar.make(getWindow().getDecorView().getRootView(), "Report Saved in Storage", Snackbar.LENGTH_LONG).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "error");
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
                params.put("_faculty_id", _faculty_id);
                params.put("_lab_id", _lab_id);
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }


}
