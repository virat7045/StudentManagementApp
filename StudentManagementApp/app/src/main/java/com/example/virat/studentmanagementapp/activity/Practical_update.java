package com.example.virat.studentmanagementapp.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
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
import com.example.virat.studentmanagementapp.adapter.practical_update_adapter;
import com.example.virat.studentmanagementapp.model.practical_model;
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
import static com.example.virat.studentmanagementapp.other.AppConfig.URL_PRACTICAL_FETCH_MARKS;
import static com.example.virat.studentmanagementapp.other.AppConfig.URL_PRACTICAL_UPDATE_MARKS;

public class Practical_update extends ActionBarActivity implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private String _faculty_id, _practical_id, _lab_id, _practical_name, _sem, _batch, _subject;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private List<practical_model> practical_models = new ArrayList<practical_model>();
    private practical_update_adapter adapter;
    ArrayList<String> _name = new ArrayList<String>();
    ArrayList<String> _e_no = new ArrayList<String>();
    public static ArrayList<String> _marks = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_update);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        listView = (ListView) findViewById(R.id.list);
        //Adapter
        adapter = new practical_update_adapter(this, practical_models);
        listView.setAdapter(adapter);

        //fetch values From Intent
        Intent i = getIntent();
        _faculty_id = i.getExtras().getString("_faculty_id");
        _practical_id = i.getExtras().getString("_practical_id");
        _lab_id = i.getExtras().getString("_lab_id");
        _practical_name = i.getExtras().getString("_practical_name");
        _sem = i.getExtras().getString("_sem");
        _batch = i.getExtras().getString("_batch");
        _subject = i.getExtras().getString("_subject");

        //Set title
        getSupportActionBar().setTitle(_practical_name);
        getSupportActionBar().setSubtitle(_subject + " " + _sem + " " + _batch);

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

        practical_fetch_marks(_practical_id);

    }

    private void practical_fetch_marks(final String _practical_id) {
        practical_models.clear();
        _name.clear();
        _e_no.clear();
        _marks.clear();
        String tag_string_req = "req_fetch_practical_marks";
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_PRACTICAL_FETCH_MARKS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "All Student Details: ");
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray result = jObj.getJSONArray("result");
                    Log.e(TAG, "Length: " + result.length() + "   Result: " + result.toString());
                    try {
                        for (int i = 0; i < result.length(); i++) {
                            practical_model am = new practical_model();
                            JSONObject collegeData = result.getJSONObject(i);

                            am.setName(collegeData.getString("name"));
                            _name.add(collegeData.getString("name"));

                            am.setE_no(collegeData.getString("e_no"));
                            _e_no.add(collegeData.getString("e_no"));

                            am.setMarks(collegeData.getString("marks"));
                            _marks.add(collegeData.getString("marks"));
                            practical_models.add(am);
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
                params.put("_practical_id", _practical_id);
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater m1 = getMenuInflater();
        m1.inflate(R.menu.actionbar_practical_update, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem menu) {
        switch (menu.getItemId()) {
            case android.R.id.home:
                //   NavUtils.navigateUpFromSameTask(Practical_Add.this);
                Practical_update.this.finish();
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
                    practical_update_marks(_practical_id, _name, _e_no, _marks);
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    Log.e(TAG, "Cancel");
                    break;
            }
        }
    };

    private void practical_update_marks(final String _practical_id, final ArrayList<String> _name, final ArrayList<String> _e_no, final ArrayList<String> _marks) {
        String tag_string_req = "req_practical_update_marks";
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_PRACTICAL_UPDATE_MARKS, new Response.Listener<String>() {
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
                    params.put("practical_id", _practical_id);
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
        practical_models.clear();
        swipeRefreshLayout.setRefreshing(true);
        practical_fetch_marks(_practical_id);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
