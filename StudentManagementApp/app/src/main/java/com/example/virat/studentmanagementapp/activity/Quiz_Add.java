package com.example.virat.studentmanagementapp.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.virat.studentmanagementapp.R;
import com.example.virat.studentmanagementapp.other.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static com.example.virat.studentmanagementapp.other.AppConfig.URL_QUIZ_ADD;

public class Quiz_Add extends ActionBarActivity {
    private String _id;
    private String _faculty_id;
    private String _lecture_id;
    private String _sem;
    private String _classname;
    private String _subject;
    private String _status;
    TextView _title, _quiz_date, _description;
    Button _upload, _cancel;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz__add);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Quiz");

        //Fetch Values From Intent
        Intent i = getIntent();
        _faculty_id = i.getExtras().getString("_faculty_id");
        _lecture_id = i.getExtras().getString("_lecture_id");
        _sem = i.getExtras().getString("_sem");
        _classname = i.getExtras().getString("_classname");
        _subject = i.getExtras().getString("_subject");

        //Get data from UI
        _title = (TextView) findViewById(R.id.title);
        _quiz_date = (TextView) findViewById(R.id.quiz_date);
        _description = (TextView) findViewById(R.id.description);
        _upload = (Button) findViewById(R.id.upload);
        _cancel = (Button) findViewById(R.id.cancel);


        _quiz_date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    _status = "quiz_date";
                    date_picker(_status);
                }
                return false;
            }
        });

        _upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, _title.getText().toString());
                Log.e(TAG, _quiz_date.getText().toString());
                if (!_title.getText().toString().trim().isEmpty() && !_quiz_date.getText().toString().trim().isEmpty()) {
                    Quiz_add(_faculty_id, _lecture_id, _sem, _classname, _subject, _title.getText().toString().trim(), _quiz_date.getText().toString().trim(), _description.getText().toString().trim());
                } else {
                    Snackbar.make(getWindow().getDecorView().getRootView(), "Please fill required Fields", Snackbar.LENGTH_SHORT).show();
                    /*Toast.makeText(getApplicationContext(), "Please fill required Fields", Toast.LENGTH_SHORT).show();*/
                }
            }
        });
        _cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Quiz_Add.this.finish();

            }
        });
    }

    private void Quiz_add(final String _faculty_id, final String _lecture_id, final String _sem, final String _classname, final String _subject, final String _title, final String _quiz_date, final String _description) {
        String tag_req_str = "Quiz_update";
        StringRequest streq = new StringRequest(Request.Method.POST, URL_QUIZ_ADD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Quiz Add response" + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    try {
                        Log.e(TAG, jObj.getString("success_msg"));
                        Quiz_Add.this.finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e(TAG, jObj.getString("error_msg"));
                        Snackbar.make(getWindow().getDecorView().getRootView(), "Problem in adding Quiz", Snackbar.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();


                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("_faculty_id", _faculty_id);
                params.put("_lecture_id", _lecture_id);
                params.put("_sem", _sem);
                params.put("_classname", _classname);
                params.put("_subject", _subject);
                params.put("_quiz_name", _title);
                params.put("_quiz_date", _quiz_date);
                params.put("_description", _description);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(streq, tag_req_str);
    }

    private void date_picker(final String _status) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datepicker = new DatePickerDialog(this, R.style.CustomDatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.e(TAG, "date Set");
                StringBuilder sb = new StringBuilder();
                sb.append(dayOfMonth + "-");
                sb.append((month + 1) + "-");
                sb.append(year);
                if (_status.equals("quiz_date")) {
                    _quiz_date.setText(sb.toString());
                }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datepicker.setTitle("Select Date");
        datepicker.show();

    }

    public boolean onOptionsItemSelected(MenuItem menu) {
        switch (menu.getItemId()) {
            case android.R.id.home:
                //   NavUtils.navigateUpFromSameTask(Quiz_Add.this);
                Quiz_Add.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(menu);
    }
}
