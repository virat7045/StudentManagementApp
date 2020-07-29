package com.example.virat.studentmanagementapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.virat.studentmanagementapp.R;
import com.example.virat.studentmanagementapp.model.assignment_model;

import java.util.List;

/**
 * Created by virat on 4/19/2017.
 */

public class assignment_details_adapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<assignment_model> assignment_model;

    public assignment_details_adapter(Activity activity, List<assignment_model> assignment_model) {
        this.activity = activity;
        this.assignment_model = assignment_model;
    }

    @Override
    public int getCount() {
        return assignment_model.size();
    }

    @Override
    public Object getItem(int position) {
        return assignment_model.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row_assignment_details, null);

        TextView assignment_name = (TextView) convertView.findViewById(R.id.Assignment_name);
        TextView start_date = (TextView) convertView.findViewById(R.id.start_date);
        TextView due_date = (TextView) convertView.findViewById(R.id.due_date);

        //Set Text in Listvies
        assignment_model am = assignment_model.get(position);
        assignment_name.setText("Title           :"+am.getAssignment_name());
        start_date.setText("Start Date    :"+am.getStart_date());
        due_date.setText("Due date      :"+am.getDue_date());


        return convertView;
    }
}
