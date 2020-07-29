package com.example.virat.studentmanagementapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.virat.studentmanagementapp.R;
import com.example.virat.studentmanagementapp.activity.Practical_Details;
import com.example.virat.studentmanagementapp.adapter.attendance_fragment_adapter;
import com.example.virat.studentmanagementapp.model.attendance_model;
import com.example.virat.studentmanagementapp.other.AppController;
import com.example.virat.studentmanagementapp.other.SQLiteHandler;

import android.widget.AdapterView.OnItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static com.example.virat.studentmanagementapp.other.AppConfig.URL_LACTURE_DETAILS;

public class LabFragment extends Fragment implements OnRefreshListener, OnItemClickListener {
    private OnFragmentInteractionListener mListener;
    private SQLiteHandler db;
    private List<attendance_model> lectureList = new ArrayList<attendance_model>();
    private ListView listView;
    private attendance_fragment_adapter adapter;
    private String f_id;
    private int n;
    private List<attendance_model> lectureItems;
    ArrayList<String> _id = new ArrayList<String>();
    ArrayList<String> _sem = new ArrayList<String>();
    ArrayList<String> _classname = new ArrayList<String>();
    ArrayList<String> _subject = new ArrayList<String>();
    private SwipeRefreshLayout swipeRefreshLayout;
    int No_lecture = 0;

    public LabFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_lab, container, false);
        listView = (ListView) rootView.findViewById(R.id.list);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        adapter = new attendance_fragment_adapter(getActivity(), lectureList);
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

        Log.e(TAG, _classname.toString());
        //Listview OnClick
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG, String.valueOf(position));
                //  Log.e(TAG, "position: " + position + "_id:" + _id.get(position));
                // Log.e(TAG, "position: " + position + "_id:" + _classname.get(position));
                Intent i = new Intent(getActivity(), Practical_Details.class);
                i.putExtra("_id", String.valueOf(_id.get(position)));
                i.putExtra("_faculty_id", f_id);
                i.putExtra("_sem", String.valueOf(_sem.get(position)));
                i.putExtra("_batch", String.valueOf(_classname.get(position)));
                i.putExtra("_subject", String.valueOf(_subject.get(position)));
                i.putExtra("status", "lab");
                startActivity(i);

            }
        });
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new SQLiteHandler(getActivity());
        HashMap<String, String> user = db.getUserDetails();
        f_id = user.get("faculty_id");
        // TODO: Fetch Lecture details From MySQl DB
        lacturedetails(f_id);

    }

    private void lacturedetails(final String f_id) {
        String tag_string_req = "req_lacture_details";
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_LACTURE_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Log.e(TAG, "Login Responce: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray result = jObj.getJSONArray("results");
                    Log.e(TAG, "Length: " + result.length() + "   Result: " + result.toString());
                    No_lecture = result.length();
                    //Count Number Of Rows in JSON Response Array
                    try {
                        for (int i = 0; i < result.length(); i++) {
                            attendance_model lm = new attendance_model();
                            JSONObject collegeData = result.getJSONObject(i);
                            lm.setClassname("");
                            lm.setSubject("");

                            _id.add(collegeData.getString("lab_id"));

                            lm.setSem(collegeData.getString("sem"));
                            _sem.add(collegeData.getString("sem"));

                            lm.setBatch(collegeData.getString("batch"));
                            _classname.add(collegeData.getString("batch"));

                            lm.setLabname(collegeData.getString("subject"));
                            _subject.add(collegeData.getString("subject"));

                            lectureList.add(lm);
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
                    Toast.makeText(getActivity(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, " Error: " + error.getMessage());
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("faculty_id", f_id);
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRefresh() {
        lectureList.clear();
        swipeRefreshLayout.setRefreshing(true);
        lacturedetails(f_id);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
