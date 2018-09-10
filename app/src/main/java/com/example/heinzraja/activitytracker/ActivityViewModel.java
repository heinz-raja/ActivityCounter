package com.example.heinzraja.activitytracker;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class ActivityViewModel extends AndroidViewModel {
    private ActivityRepo myRepo;
    private LiveData<List<Activity>> AllActivites;
    private LiveData<List<String>> AllGroups;

    public ActivityViewModel(Application application){
        super(application);
        myRepo = new ActivityRepo(application);
        AllGroups = myRepo.getAllSubject();
        AllActivites = myRepo.getAllActivities();

    }

    public void incrementcount(String group,String job){
        myRepo.incrementcount(group, job);
    }

    public void decrementcount(String group,String job){
        myRepo.decrementcount(group,job);
    }

    public void ChangeGroupName(String GroupName,String NewGroupName){ myRepo.ChangeGroupName(GroupName,NewGroupName);}

    public void ChangeActivityName(String GroupName,String ActivityName,String NewActivityName) {myRepo.ChangeActivityName(GroupName,ActivityName,NewActivityName);}

    public void DeleteGroup(String GroupName){myRepo.DeleteGroup(GroupName);}

    public void DeleteActivity(String GroupName,String job){myRepo.DeleteActivity(GroupName,job);}

    public void changecount(String group,String job,int count){
        myRepo.changecount(group, job, count);
    }

    public void addtocount(String group,String job,int count){myRepo.addtocount(group,job,count);}

    public void subfromcount(String group,String job,int count){myRepo.subfromcount(group,job,count);}

    LiveData<List<Activity>> getAllActivites(){return AllActivites;}

    LiveData<List<String>> getAllGroups(){return AllGroups;}

    public void insert(Activity activity){
        myRepo.insert(activity);
    }

}
