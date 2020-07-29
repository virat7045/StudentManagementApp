package com.example.virat.studentmanagementapp.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.virat.studentmanagementapp.R;
import com.example.virat.studentmanagementapp.adapter.quiz_details_adapter;
import com.example.virat.studentmanagementapp.model.attendance_model;
import com.example.virat.studentmanagementapp.model.quiz_model;
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
import static com.example.virat.studentmanagementapp.other.AppConfig.URL_GENERATE_FETCH_QUIZ_MARKS;
import static com.example.virat.studentmanagementapp.other.AppConfig.URL_QUIZ_DETAILS;
import static com.example.virat.studentmanagementapp.other.AppConfig.URL_QUIZ_FETCH_MARKS;
import static com.example.virat.studentmanagementapp.other.AppConfig.URL_STUDENT_LECTURE_DETAILS;

public class Quiz_Details extends ActionBarActivity implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private String _id;
    private String _faculty_id;
    private String _sem;
    private String _classname;
    private String _subject;
    private String status;
    private List<quiz_model> quiz_model = new ArrayList<quiz_model>();
    ArrayList<String> _Quiz_id = new ArrayList<String>();
    ArrayList<String> _Quiz_name = new ArrayList<String>();
    ArrayList<String> _quiz_date = new ArrayList<String>();
    ArrayList<String> _description = new ArrayList<String>();

    ArrayList<String> _name = new ArrayList<String>();
    ArrayList<String> _e_no = new ArrayList<String>();

    ArrayList<String> name_ = new ArrayList<String>();
    ArrayList<String> e_no_ = new ArrayList<String>();
    ArrayList<String> marks_ = new ArrayList<String>();
    ArrayList<String> quiz_name_ = new ArrayList<String>();
    ArrayList<String> quiz_id_ = new ArrayList<String>();
    ListView listView;
    public boolean bn = true;
    public boolean generate = false;
    final int MY_PERMISSION = 0;

    SwipeRefreshLayout swipeRefreshLayout;
    private quiz_details_adapter adapter;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Set Adapter to listview
        listView = (ListView) findViewById(R.id.list);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        adapter = new quiz_details_adapter(this, quiz_model);
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
                Intent i = new Intent(Quiz_Details.this, Quiz_Add.class);
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
                Intent i = new Intent(Quiz_Details.this, Quiz_update.class);
                i.putExtra("_faculty_id", _faculty_id);
                i.putExtra("_quiz_id", _Quiz_id.get(position));
                i.putExtra("_lecture_id", _id);
                i.putExtra("_quiz_name", _Quiz_name.get(position));
                i.putExtra("_sem", _sem);
                i.putExtra("_classname", _classname);
                i.putExtra("_subject", _subject);
                startActivity(i);
            }
        });
        //featch Quiz Details
        Quizdetails(_faculty_id, _sem, _classname, _subject);

    }

    private void Quizdetails(final String _faculty_id, final String _sem, final String _classname, final String _subject) {
        String tag_string_req = "fetch_quiz_details";
        _Quiz_id.clear();
        _description.clear();
        _quiz_date.clear();
        _Quiz_name.clear();
        StringRequest strreq = new StringRequest(Request.Method.POST, URL_QUIZ_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jobj = new JSONObject(response);
//                    Log.e(TAG, "response" + jobj.toString());
                    JSONArray result = jobj.getJSONArray("result");
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject data = result.getJSONObject(i);
                        quiz_model am = new quiz_model();
                        _Quiz_id.add(data.getString("quiz_id"));
                        am.setQuiz_name(data.getString("quiz_name"));
                        _Quiz_name.add(data.getString("quiz_name"));
                        _quiz_date.add(data.getString("quiz_date"));
                        am.setQuiz_date(data.getString("quiz_date"));
                        _description.add(data.getString("description"));
                        am.setDescription(data.getString("description"));

                        quiz_model.add(am);
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
    public void onRefresh() {
        quiz_model.clear();
        swipeRefreshLayout.setRefreshing(true);
        Quizdetails(_faculty_id, _sem, _classname, _subject);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater m1 = getMenuInflater();
        m1.inflate(R.menu.actionbar_quiz_details, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public boolean onOptionsItemSelected(MenuItem menu) {
        switch (menu.getItemId()) {
            case android.R.id.home:
                Quiz_Details.this.finish();
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
                    //this.generate_fetch_quiz_marks(_faculty_id, _id);
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
                        this.studentDetailslecture(_sem, _classname);
                        // this.generate_fetch_quiz_marks(_faculty_id, _id);
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
                        generate_fetch_quiz_marks(_faculty_id, _id);
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

    private void generate_fetch_quiz_marks(final String _faculty_id, final String _lecture_id) {

        String tag_string_req = "req_generate_ fetch_quiz_marks";
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_GENERATE_FETCH_QUIZ_MARKS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "All Student Details: ");
                try {
                    generate = false;
                    JSONObject jObj = new JSONObject(response);
                    JSONArray result = jObj.getJSONArray("result");
                    Log.e(TAG, "Length: " + result.length() + "   Result: " + result.toString());
                    String[] ss = new String[_Quiz_name.size() + 3];
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
                            File file = new File(dir, "Quiz Marks_" + _subject + "_" + _sem + "_" + _classname + "" + ".csv");
                            try {
                                if (file.exists()) {
                                    file.delete();
                                }
                                file.createNewFile();
                                Log.e(TAG, quiz_id_.toString());
                                Log.e(TAG, name_.toString());
                                CSVWriter writer = new CSVWriter(new FileWriter(file, true), CSVWriter.DEFAULT_SEPARATOR);
                                //Heading
                                ss[0] = "Index";
                                ss[1] = "Enrollment No";
                                ss[2] = "Name";
                                for (int i = 0; i < _Quiz_name.size(); i++) {
                                    ss[3 + i] = _Quiz_name.get(i);
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
                                            for (int k = 0; k < _Quiz_id.size(); k++) {
                                                if (_Quiz_id.get(k).equals(collegeData.getString("quiz_id")))
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
        }, new Response.ErrorListener()

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

    private void generatereport() {

        //Environment.getExternalStorageDirectory()

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
            File file = new File(dir, "Quiz Marks_" + _subject + "_" + _sem + "_" + _classname + "" + ".csv");
            try {
                if (file.exists()) {
                    file.delete();
                }
                file.createNewFile();
                Log.e(TAG, quiz_id_.toString());
                Log.e(TAG, name_.toString());
                CSVWriter writer = new CSVWriter(new FileWriter(file, true), CSVWriter.DEFAULT_SEPARATOR);
                //  writer.writeNext(s);
                writer.flush();
                writer.close();
                Snackbar.make(getWindow().getDecorView().getRootView(), "Report Saved in Storage", Snackbar.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
