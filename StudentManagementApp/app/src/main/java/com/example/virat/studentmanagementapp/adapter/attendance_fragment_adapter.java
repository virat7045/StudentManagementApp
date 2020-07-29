package com.example.virat.studentmanagementapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.virat.studentmanagementapp.R;
import com.example.virat.studentmanagementapp.model.attendance_model;

import java.util.List;

public class attendance_fragment_adapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<attendance_model> lectureItems;

    public attendance_fragment_adapter(Activity activity, List<attendance_model> lectureItems) {
        this.activity = activity;
        this.lectureItems = lectureItems;
    }

    @Override
    public int getCount() {
        return lectureItems.size();
    }

    @Override
    public Object getItem(int location) {
        return lectureItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row_main_all_fragment, null);
        TextView subject = (TextView) convertView.findViewById(R.id.subject);
        TextView sem = (TextView)convertView.findViewById(R.id.sem);
        TextView classname = (TextView)convertView.findViewById(R.id.classname);


        attendance_model lm = lectureItems.get(position);
        subject.setText  (" Subject  :" +lm.getSubject());
        sem.setText      (" Semester :" +lm.getSem());
        classname.setText(" Class        :" +lm.getClassname());

       if (lm.getClassname()=="")
        {
            classname.setText(" Batch        :" +lm.getBatch());

        }
        if (lm.getSubject()=="")
        {
            subject.setText(" Lab         :" +lm.getLabname());

        }



        return convertView;
    }
}
