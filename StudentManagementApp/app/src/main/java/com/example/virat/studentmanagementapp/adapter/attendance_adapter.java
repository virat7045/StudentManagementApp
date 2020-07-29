package com.example.virat.studentmanagementapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.virat.studentmanagementapp.R;
import com.example.virat.studentmanagementapp.model.attendance_model;

import java.util.List;

import static android.content.ContentValues.TAG;
import static com.example.virat.studentmanagementapp.activity.Attendance._selected;
import static com.example.virat.studentmanagementapp.activity.Attendance.Button_checkall;
/**
 * Created by virat on 4/11/2017.
 */

public class attendance_adapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<attendance_model> lectureItems;

    public attendance_adapter(Activity activity, List<attendance_model> lectureItems) {
        this.activity = activity;
        this.lectureItems = lectureItems;
    }

    @Override
    public int getCount() {
        return lectureItems.size();
    }

    @Override
    public Object getItem(int position) {
        return lectureItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row_attendance, null);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView e_no = (TextView) convertView.findViewById(R.id.e_no);
        final CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkbox1);

        final attendance_model lm = lectureItems.get(position);
        name.setText(" Name     :" + lm.getName());
        e_no.setText(" Number    :" + lm.getE_no());
        cb.setChecked(lm.getCheckbox_value());

        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb.isChecked()) {
                    _selected.set(position, "P");
                    Log.e(TAG, String.valueOf(position));
                    //   lm.setCheckbox_value(false);
                } else if (!cb.isChecked()) {
                    // lectureItems.indexOf(cb);
                    //    lm.setCheckbox_value(true);
                    Button_checkall.setChecked(false);
                    _selected.set(position, "A");
                    // lm.setCheckbox_value(false);
                }
            }
        });
        return convertView;
    }
}
