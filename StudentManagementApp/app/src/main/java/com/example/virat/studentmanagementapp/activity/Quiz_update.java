package com.example.virat.studentmanagementapp.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.virat.studentmanagementapp.R;
import com.example.virat.studentmanagementapp.adapter.quiz_update_adapter;
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
import static com.example.virat.studentmanagementapp.other.AppConfig.URL_QUIZ_FETCH_MARKS;
import static com.example.virat.studentmanagementapp.other.AppConfig.URL_QUIZ_UPDATE_MARKS;

public class Quiz_update extends ActionBarActivity implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private String _faculty_id, _quiz_id, _lecture_id, _quiz_name, _sem, _classname, _subject;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private List<quiz_model> quiz_models = new ArrayList<quiz_model>();
    private quiz_update_adapter adapter;
    final int MY_PERMISSION = 0;
    private String _update_date;
    public boolean bn = true;
    public boolean generate = false;
    ArrayList<String> _name = new ArrayList<String>();
    ArrayList<String> _e_no = new ArrayList<String>();
    public static ArrayList<String> _marks = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_update);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        listView = (ListView) findViewById(R.id.list);
        //Adapter
        adapter = new quiz_update_adapter(this, quiz_models);
        listView.setAdapter(adapter);

        //fetch values From Intent
        Intent i = getIntent();
        _faculty_id = i.getExtras().getString("_faculty_id");
        _quiz_id = i.getExtras().getString("_quiz_id");
        _lecture_id = i.getExtras().getString("_lecture_id");
        _quiz_name = i.getExtras().getString("_quiz_name");
        _sem = i.getExtras().getString("_sem");
        _classname = i.getExtras().getString("_classname");
        _subject = i.getExtras().getString("_subject");

        //Set title
        getSupportActionBar().setTitle(_quiz_name);
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

        quiz_fetch_marks(_quiz_id);

    }

    private void quiz_fetch_marks(final String _quiz_id) {
        quiz_models.clear();
        _name.clear();
        _e_no.clear();
        _marks.clear();
        String tag_string_req = "req_fetch_quiz_marks";
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_QUIZ_FETCH_MARKS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "All Student Details: ");
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray result = jObj.getJSONArray("result");
                    Log.e(TAG, "Length: " + result.length() + "   Result: " + result.toString());
                    try {
                        for (int i = 0; i < result.length(); i++) {
                            quiz_model am = new quiz_model();
                            JSONObject collegeData = result.getJSONObject(i);

                            am.setName(collegeData.getString("name"));
                            _name.add(collegeData.getString("name"));

                            am.setE_no(collegeData.getString("e_no"));
                            _e_no.add(collegeData.getString("e_no"));

                            am.setMarks(collegeData.getString("marks"));
                            _marks.add(collegeData.getString("marks"));
                            quiz_models.add(am);
                        }
                        adapter.notifyDataSetChanged();
                        //     swipeRefreshLayout.setRefreshing(false);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    adapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                    if (generate) {
                        generatereport();
                        generate = false;
                        Log.e(TAG, "generate");
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
                params.put("_quiz_id", _quiz_id);
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void generatereport() {

        //Environment.getExternalStorageDirectory()

        Log.e(TAG, "Generate Report method");
        File savedir = Environment.getExternalStorageDirectory();
        File dir = new File(savedir.getAbsolutePath(), "Student Management");
        String date = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                Log.e(TAG, "created Directory----------------");
            } else {
                Log.e(TAG, "Failed to create Directory----------------");
            }
        }
        if (dir.exists()) {
            //File file = new File(dir, "" + _subject + "_" + _sem + "_" + _classname + "_" + date.toString() + "" + ".csv");
            File file = new File(dir, ""+_quiz_name+"" + _subject + "_" + _sem + "_" + _classname + "_" + _update_date.toString() + "" + ".csv");
            try {
                file.createNewFile();
                Log.e(TAG, _name.toString());
                String[] s = new String[3];
                CSVWriter writer = new CSVWriter(new FileWriter(file, true), CSVWriter.DEFAULT_SEPARATOR);
                for (int i = 0; i < _name.size(); i++) {
                    s[0] = _e_no.get(i);
                    s[1] = _name.get(i);
                    s[2] = _marks.get(i);
                    writer.writeNext(s);
                    writer.flush();
                }
                writer.close();
                Snackbar.make(getWindow().getDecorView().getRootView(), "Report Saved in Storage", Snackbar.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater m1 = getMenuInflater();
        m1.inflate(R.menu.actionbar_quiz_update, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem menu) {
        switch (menu.getItemId()) {
            case android.R.id.home:
                //   NavUtils.navigateUpFromSameTask(Quiz_Add.this);
                Quiz_update.this.finish();
                return true;
            case R.id.action_bar_update:

                Log.e(TAG, _marks.toString());
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Update Marks?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
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
                    this.action_generate_report();
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
                        this.action_generate_report();
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

    public void action_generate_report() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datepicker = new DatePickerDialog(this, R.style.CustomDatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //
                Log.e(TAG, " action Update  attendance");
                StringBuilder sb = new StringBuilder();
                sb.append(year + "-");
                sb.append((month + 1) + "-");
                sb.append(dayOfMonth);
                _update_date = sb.toString();
                if (bn) {
                    Log.e(TAG, "E");
                    quiz_fetch_marks(_quiz_id);
                    Log.e(TAG, "x");
                    bn = false;
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

    //dIALOG YES NO FOR SAVE ATTENDANCE
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int choice) {
            switch (choice) {
                case DialogInterface.BUTTON_POSITIVE:
                    quiz_update_marks(_quiz_id, _name, _e_no, _marks);
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    Log.e(TAG, "Cancel");
                    break;
            }
        }
    };

    private void quiz_update_marks(final String _quiz_id, final ArrayList<String> _name, final ArrayList<String> _e_no, final ArrayList<String> _marks) {
        String tag_string_req = "req_quiz_update_marks";
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_QUIZ_UPDATE_MARKS, new Response.Listener<String>() {
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
                        Snackbar.make(getWindow().getDecorView().getRootView(), "Problem in Updating Marks", Snackbar.LENGTH_SHORT).show();
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
                    params.put("quiz_id", _quiz_id);
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
        quiz_models.clear();
        swipeRefreshLayout.setRefreshing(true);
        quiz_fetch_marks(_quiz_id);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
