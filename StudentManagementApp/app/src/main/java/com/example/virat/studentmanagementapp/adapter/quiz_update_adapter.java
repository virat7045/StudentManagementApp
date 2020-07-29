package com.example.virat.studentmanagementapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.example.virat.studentmanagementapp.R;
import com.example.virat.studentmanagementapp.model.quiz_model;

import java.util.List;

import static com.example.virat.studentmanagementapp.activity.Quiz_update._marks;

/**
 * Created by virat on 4/11/2017.
 */

public class quiz_update_adapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<quiz_model> quiz_models;

    public quiz_update_adapter(Activity activity, List<quiz_model> quiz_models) {
        this.activity = activity;
        this.quiz_models = quiz_models;
    }

    @Override
    public int getCount() {
        return quiz_models.size();
    }

    @Override
    public Object getItem(int position) {
        return quiz_models.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_row_quiz_update, null);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.e_no = (TextView) convertView.findViewById(R.id.e_no);
            holder.marks = (EditText) convertView.findViewById(R.id.marks);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        quiz_model am = quiz_models.get(position);
        holder.name.setText(" Name     :" + am.getName());
        holder.e_no.setText(" Number    :" + am.getE_no());
        holder.marks.setText(am.getMarks().toString());
        holder.marks.setId(position);
        holder.i = position;
        holder.marks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Log.e(TAG, "before----------" + s.toString());

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Log.e(TAG, "On----------" + s.toString());
                _marks.set(holder.i, s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Log.e(TAG, "after----------" + s.toString());
            }
        });
        return convertView;
    }

    public class ViewHolder {
        TextView name, e_no;
        EditText marks;
        int i;
    }
}

/*
    final ViewHolder holder;
if (inflater == null)
        inflater = (LayoutInflater) activity
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
        convertView = inflater.inflate(R.layout.list_row_quiz_update, null);
        holder = new ViewHolder();
        holder.name = (TextView) convertView.findViewById(R.id.name);
        holder.e_no = (TextView) convertView.findViewById(R.id.e_no);
        holder.marks = (EditText) convertView.findViewById(R.id.marks);
        convertView.setTag(holder);
        } else {
        holder = (ViewHolder) convertView.getTag();
        }
        quiz_model am = quiz_models.get(position);
        holder.name.setText(" Name     :" + am.getName());
        holder.e_no.setText(" Number    :" + am.getE_no());
        holder.marks.setText(am.getMarks().toString());
        holder.marks.setId(position);
*/

