package com.example.heinzraja.activitytracker;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "activity_table")
public class Activity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "group")
    private String GroupName;

    @ColumnInfo(name = "activity")
    private String TheJob;

    //Counter for the activity/Job
    private int count = 0;

    public Activity(@NonNull String Gname, String Act){this.GroupName = Gname;this.TheJob= Act;}

    public Activity(){GroupName="";TheJob="";count=0;}

    //Getters
    public int getCount() {
        return this.count;
    }

    @NonNull
    public String getGroupName() {
        return this.GroupName;
    }

    public String getTheJob(){
        return this.TheJob;
    }

    public int getId(){
        return this.id;
    }

    //Setters
    public void setGroupName(@NonNull String groupName){
        this.GroupName = groupName;
    }

    public void setTheJob(String theJob){
        this.TheJob = theJob;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setCount(int count){
        this.count = count;
    }
}
