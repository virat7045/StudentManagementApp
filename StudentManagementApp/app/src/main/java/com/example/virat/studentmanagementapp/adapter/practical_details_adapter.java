package com.example.virat.studentmanagementapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.virat.studentmanagementapp.R;
import com.example.virat.studentmanagementapp.model.practical_model;

import java.util.List;

/**
 * Created by virat on 4/19/2017.
 */

public class practical_details_adapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<practical_model> practical_model;

    public practical_details_adapter(Activity activity, List<practical_model> practical_model) {
        this.activity = activity;
        this.practical_model = practical_model;
    }

    @Override
    public int getCount() {
        return practical_model.size();
    }

    @Override
    public Object getItem(int position) {
        return practical_model.get(position);
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
            convertView = inflater.inflate(R.layout.list_row_practical_details, null);

        TextView practical_name = (TextView) convertView.findViewById(R.id.Practical_name);
        TextView practical_date = (TextView) convertView.findViewById(R.id.practical_date);
        TextView description = (TextView) convertView.findViewById(R.id.description);

        //Set Text in Listvies
        practical_model am = practical_model.get(position);
        practical_name.setText("Title                  :"+am.getPractical_name());
        practical_date.setText("Practical Date     :"+am.getPractical_date());
        description.setText("Description          :"+am.getDescription());



        return convertView;
    }
}
