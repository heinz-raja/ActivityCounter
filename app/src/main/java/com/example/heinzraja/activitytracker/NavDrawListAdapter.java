package com.example.heinzraja.activitytracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NavDrawListAdapter extends BaseAdapter{
    private List<String> mSubjects;
    private Context context;

    NavDrawListAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        if(mSubjects!=null)
            return mSubjects.size();
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if(mSubjects!=null)
            return mSubjects.get(i);
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View row = null;
        if(view==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=inflater.inflate(R.layout.custom_nav_row,viewGroup,false);
        }
        else{
            row = view;
        }
        TextView GroupTextView = row.findViewById(R.id.nav_row);
        GroupTextView.setText(mSubjects.get(i));

        return row;
    }

    void setGroups(List<String> Groups){
        mSubjects = new ArrayList<>();
        mSubjects.add("All");
        mSubjects.addAll(Groups);
        notifyDataSetChanged();
    }
}
