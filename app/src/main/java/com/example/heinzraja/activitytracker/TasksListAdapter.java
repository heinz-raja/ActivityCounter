package com.example.heinzraja.activitytracker;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Delete;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Matrix;
import android.media.Image;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TasksListAdapter extends BaseAdapter {

    private List<Activity> currTasks;
    private Context context;
    private Context baseContext;
    private ViewGroup viewGroup;
    private Toast DelToast=null;

    TasksListAdapter(Context context, Context baseContext, ViewGroup viewGroup){
        this.context = context;
        this.baseContext = baseContext;
        this.viewGroup = viewGroup;
    }

    @Override
    public int getCount() {
        if(currTasks==null)
            return 0;
        else{
            return currTasks.size();
        }
    }

    @Override
    public Object getItem(int i) {
        if(currTasks!=null)
            return currTasks.get(i);
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    class RowButtons{
        FloatingActionButton incrButton;
        FloatingActionButton decrButton;
    }
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View jobRow;
        RowButtons mButtons;


        if(view==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            jobRow = inflater.inflate(R.layout.custom_job_row,viewGroup,false);
            mButtons = new RowButtons();
            mButtons.incrButton = jobRow.findViewById(R.id.increment_button);
            mButtons.decrButton = jobRow.findViewById(R.id.decrement_button);
            jobRow.setTag(mButtons);
        }
        else{
            jobRow = view;
            mButtons = (RowButtons) jobRow.getTag();
        }
        final TextView GroupTextView = jobRow.findViewById(R.id.job_row);
        GroupTextView.setText(currTasks.get(i).getTheJob());

        final ImageButton DelButton = jobRow.findViewById(R.id.delete_icon);

        TextView CountView = jobRow.findViewById(R.id.counter_for_job);
        CountView.setText(Integer.toString(currTasks.get(i).getCount()));

        final ActivityViewModel activityViewModel = ViewModelProviders.of((FragmentActivity) context).get(ActivityViewModel.class);
        mButtons.incrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Increment button pressed
                activityViewModel.incrementcount(currTasks.get(i).getGroupName(),currTasks.get(i).getTheJob());

            }
        });

        mButtons.decrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Decrement button pressed
                activityViewModel.decrementcount(currTasks.get(i).getGroupName(),currTasks.get(i).getTheJob());

            }
        });

        DelButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //Delete the row/job
                DeleteJob(activityViewModel,i);
                return true;
            }
        });

        DelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Prompts user to hold the delete button
                if(DelToast==null){
                    DelToast = Toast.makeText(context,"Hold to delete",Toast.LENGTH_SHORT);
                }
                else
                    DelToast.setText("Hold to delete");
                DelToast.show();
            }
        });

        CountView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Option to change count
                ChangeCount(activityViewModel,i);
            }
        });

        mButtons.incrButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //Add to existing count
                AddtoCount(activityViewModel,i);
                return true;
            }
        });

        mButtons.decrButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //Sub form existing count
                SubFromCount(activityViewModel,i);
                return true;
            }
        });
        GroupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Option to change the job name
                ChangeName(activityViewModel,i);
            }
        });



        return jobRow;
    }

    private void DeleteJob(final ActivityViewModel activityViewModel, final int pos){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure you want to delete "+currTasks.get(pos).getTheJob()+"?");
        builder.setCancelable(true);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                activityViewModel.DeleteActivity(currTasks.get(pos).getGroupName(),currTasks.get(pos).getTheJob());
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }

    private void ChangeName(final ActivityViewModel activityViewModel, final int pos){
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        builder.setTitle("Change Name");

        View viewInflated = LayoutInflater.from(baseContext).inflate(R.layout.create_subject,viewGroup,false);

        final EditText input = viewInflated.findViewById(R.id.subjectinput);

        input.setText(currTasks.get(pos).getTheJob());

        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String newName = input.getText().toString();
                if(!newName.trim().isEmpty()){
                    boolean flag = true;
                    for(Activity activity:currTasks){
                        if(activity.getTheJob().equals(newName)&&!activity.getTheJob().equalsIgnoreCase(currTasks.get(pos).getTheJob())){
                            flag = false;
                            break;
                        }
                    }
                    if(flag){
                        activityViewModel.ChangeActivityName(currTasks.get(pos).getGroupName(),currTasks.get(pos).getTheJob(),newName);
                    }
                    else {
                        Toast.makeText(context,"Name already taken",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }
    private void ChangeCount(final ActivityViewModel activityViewModel, final int pos){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        builder.setTitle("Change count");

        View viewInflated = LayoutInflater.from(baseContext).inflate(R.layout.input_number,viewGroup,false);

        final EditText input = viewInflated.findViewById(R.id.number_input);

        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String newCount = input.getText().toString();
                try{
                    int number = Integer.parseInt(newCount);
                    activityViewModel.changecount(currTasks.get(pos).getGroupName(),currTasks.get(pos).getTheJob(),number);
                }
                catch (NumberFormatException e){
                    Toast.makeText(context,"Invalid Input",Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }

    private void AddtoCount(final ActivityViewModel activityViewModel, final int pos){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        builder.setTitle("Number to add");

        View viewInflated = LayoutInflater.from(baseContext).inflate(R.layout.input_number,viewGroup,false);

        final EditText input = viewInflated.findViewById(R.id.number_input);

        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String newCount = input.getText().toString();
                try{
                    int number = Integer.parseInt(newCount);
                    activityViewModel.addtocount(currTasks.get(pos).getGroupName(),currTasks.get(pos).getTheJob(),number);
                }
                catch (NumberFormatException e){
                    Toast.makeText(context,"Invalid Input",Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }

    private void SubFromCount(final ActivityViewModel activityViewModel, final int pos){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        builder.setTitle("Subtract by");

        View viewInflated = LayoutInflater.from(baseContext).inflate(R.layout.input_number,viewGroup,false);

        final EditText input = viewInflated.findViewById(R.id.number_input);

        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String newCount = input.getText().toString();
                try{
                    int number = Integer.parseInt(newCount);
                    activityViewModel.subfromcount(currTasks.get(pos).getGroupName(),currTasks.get(pos).getTheJob(),number);
                }
                catch (NumberFormatException e){
                    Toast.makeText(context,"Invalid Input",Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }


    void setmTasks(List<Activity> activities){
        currTasks = activities;
        for(int i=0;i<activities.size();i++)
            if(activities.get(i).getTheJob().equals("")||activities.get(i).getGroupName().equals(""))
                currTasks.remove(activities.get(i));
        notifyDataSetChanged();
    }
}

