package com.example.virat.studentmanagementapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.virat.studentmanagementapp.R;
import com.example.virat.studentmanagementapp.model.quiz_model;

import java.util.List;

/**
 * Created by virat on 4/19/2017.
 */

public class quiz_details_adapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<quiz_model> quiz_model;

    public quiz_details_adapter(Activity activity, List<quiz_model> quiz_model) {
        this.activity = activity;
        this.quiz_model = quiz_model;
    }

    @Override
    public int getCount() {
        return quiz_model.size();
    }

    @Override
    public Object getItem(int position) {
        return quiz_model.get(position);
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
            convertView = inflater.inflate(R.layout.list_row_quiz_details, null);

        TextView quiz_name = (TextView) convertView.findViewById(R.id.Quiz_name);
        TextView quiz_date = (TextView) convertView.findViewById(R.id.quiz_date);
        TextView description = (TextView) convertView.findViewById(R.id.description);

        //Set Text in Listvies
        quiz_model am = quiz_model.get(position);
        quiz_name.setText("Title           :"+am.getQuiz_name());
        quiz_date.setText("Quiz Date     :"+am.getQuiz_date());
        description.setText("Description  :"+am.getDescription());



        return convertView;
    }
}
