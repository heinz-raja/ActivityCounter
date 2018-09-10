package com.example.heinzraja.activitytracker;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;


import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private String SubjectName="";
    private NavDrawListAdapter navDrawListAdapter;
    private  ActivityViewModel activityViewModel;
    private TasksListAdapter tasksListAdapter;
    List<Activity> mactivities ;
    String theGroup="";
    List<String> Groups;

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        activityViewModel = ViewModelProviders.of(this).get(ActivityViewModel.class);

        //First observer when starting the app, adds the 'ALL' group/division which cannot be deleted/edited
        activityViewModel.getAllGroups().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> strings) {
                Groups = new ArrayList<>();
                Groups.add("All");
                Groups.addAll(strings);
                navDrawListAdapter.setGroups(strings);
            }
        });

        //We get both the group and the activities from the same table,the groups have an empty activity/job attribute
        Observer Aobserver = new Observer<List<Activity>>() {
            @Override
            public void onChanged(@Nullable List<Activity> activities) {
                mactivities = new ArrayList<>();
                for(Activity activity:activities){
                    if(!activity.getTheJob().equals(""))
                        mactivities.add(activity);
                }
                tasksListAdapter.setmTasks(mactivities);
            }
        };
        activityViewModel.getAllActivites().observe(this,Aobserver);

        ListView listView = findViewById(R.id.Subjects);
        navDrawListAdapter=new NavDrawListAdapter(this);
        tasksListAdapter = new TasksListAdapter(this,getBaseContext(),(ViewGroup) findViewById(android.R.id.content));

        ListView taskview = findViewById(R.id.jobs);
        taskview.setItemsCanFocus(true);

        taskview.setAdapter(tasksListAdapter);
        listView.setAdapter(navDrawListAdapter);
        listView.setOnItemClickListener(this);

        registerForContextMenu(listView);

        drawerLayout = findViewById(R.id.drawerLayout);


        toggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageButton NavHeaderAddGroup = findViewById(R.id.NavHeaderAddGroup);
        NavHeaderAddGroup.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                CreateSubject();
            }
        });
    }


    //Closes drawer on backpress instead of quiting the app
    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }


    //Declaring Toast object here to avoid creating multiple toast objects
    Toast mToast = null;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(toggle.onOptionsItemSelected(item))
            return true;
        int id = item.getItemId();

        //Prompts user to select a group when trying to add in the 'ALL' group
        if(theGroup.equals("All")||theGroup.equals("")) {
            if(mToast==null)
                mToast=Toast.makeText(this, "Please select or add a Group", Toast.LENGTH_SHORT);
            else
                mToast.setText("Please select or add a Group");
            mToast.show();
        }
        else if(id==R.id.addgroup && !theGroup.equals("")){
            CreateActivity(theGroup);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void CreateSubject(){

        AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Create subject");

        View viewInflated = LayoutInflater.from(getBaseContext()).inflate(R.layout.create_subject,(ViewGroup) findViewById(android.R.id.content) ,false);

        final EditText input = (EditText) viewInflated.findViewById(R.id.subjectinput);

        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SubjectName = input.getText().toString();
                dialogInterface.dismiss();
                if(!SubjectName.trim().isEmpty()){
                    Boolean flag = Boolean.FALSE;
                    for(String words:Groups){
                        if(words.equalsIgnoreCase(SubjectName)) {
                            Toast.makeText(context, "This Group already exists", Toast.LENGTH_SHORT).show();
                            flag = Boolean.TRUE;
                        }
                    }
                    if(!flag){
                        Activity activity = new Activity(SubjectName,"");
                        activityViewModel.insert(activity);
                        SubjectName="";
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

    public void CreateActivity(String GroupName){
        AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Create Activity");

        View viewInflated = LayoutInflater.from(getBaseContext()).inflate(R.layout.create_subject,(ViewGroup) findViewById(android.R.id.content),false);

        final EditText input = viewInflated.findViewById(R.id.subjectinput);

        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String ActivityName = input.getText().toString();
                dialogInterface.dismiss();
                Boolean flag = Boolean.FALSE;
                if(!ActivityName.trim().isEmpty()) {
                    for (Activity activity : mactivities) {
                        if (activity.getTheJob().equalsIgnoreCase(ActivityName))
                            flag = Boolean.TRUE;
                    }
                    if(flag)
                        Toast.makeText(context,"This activity already exists",Toast.LENGTH_SHORT).show();
                    else {
                        Activity activity = new Activity(theGroup, ActivityName);
                        activityViewModel.insert(activity);
                    }
                    ActivityName = "";
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_group,menu);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //Switch frames here for subject clicked in the nav drawer

        if(adapterView.getId() == R.id.Subjects) {
            theGroup = Groups.get(i);

            Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle(Capitalizefirstletter(theGroup));
            activityViewModel.getAllActivites().removeObservers((LifecycleOwner) context);
            if(Groups.get(i).equals("All")){
                Observer Aobserver = new Observer<List<Activity>>() {
                    @Override
                    public void onChanged(@Nullable List<Activity> activities) {
                        mactivities = new ArrayList<>();
                        for(Activity activity:activities){
                            if(!activity.getTheJob().equals(""))
                                mactivities.add(activity);
                        }
                        tasksListAdapter.setmTasks(mactivities);
                    }
                };
                activityViewModel.getAllActivites().observe((LifecycleOwner) context,Aobserver);
            }
            else {
                Observer<List<Activity>> groupObserver = new Observer<List<Activity>>() {
                    @Override
                    public void onChanged(@Nullable List<Activity> activities) {
                        mactivities = new ArrayList<>();
                        for (Activity activity : activities)
                            if (activity.getGroupName().equals(theGroup))
                                mactivities.add(activity);
                        tasksListAdapter.setmTasks(mactivities);
                    }
                };


                activityViewModel.getAllActivites().observe((LifecycleOwner) context, groupObserver);
            }
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    String Capitalizefirstletter(String word){
        if(word!=null){
            String first = word.substring(0,1);
            String rest = word.substring(1);
            return first.toUpperCase()+rest;
        }
        return  null;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){
        if(view.getId()== R.id.Subjects){

            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            if(Groups.get(info.position).equals("All")){
                return;
            }
            String[] menuItems ={"Edit","Delete"};
            for(int i=0;i<menuItems.length;i++)
                menu.add(Menu.NONE,i,i,menuItems[i]);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int menuIndex = item.getItemId();
        switch (menuIndex){
            case 1:
                DeleteGroup(info);
                break;
            case 0:
                ChangeGroupName(info);
                break;
        }
        return true;
    }

    private void DeleteGroup(final AdapterView.AdapterContextMenuInfo info){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete "+Groups.get(info.position)+"?");
        builder.setCancelable(true);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                activityViewModel.DeleteGroup(Groups.get(info.position));
                AdapterView adapterView = findViewById(R.id.Subjects);
                onItemClick(adapterView,null,0,0);
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
    private void ChangeGroupName(final AdapterView.AdapterContextMenuInfo info){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change Group Name");

        View viewinflated  = LayoutInflater.from(getBaseContext()).inflate(R.layout.create_subject,(ViewGroup) findViewById(android.R.id.content),false);

        final EditText input = viewinflated.findViewById(R.id.subjectinput);

        input.setText(Groups.get(info.position));

        builder.setView(viewinflated);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String newName = input.getText().toString();
                if(!newName.trim().isEmpty()){
                    boolean flag = true;
                    for(String names:Groups){
                        if(names.equalsIgnoreCase(newName)){
                            flag = false;
                            break;
                        }
                    }
                    if(flag){
                        activityViewModel.ChangeGroupName(Groups.get(info.position),newName);
                    }
                    else
                        Toast.makeText(context,"This Group already exists",Toast.LENGTH_SHORT).show();
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
}
