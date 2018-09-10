package com.example.heinzraja.activitytracker;

import android.accessibilityservice.AccessibilityService;
import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.security.acl.Group;
import java.util.List;

public class ActivityRepo {
    private ActivityDao myActivityDao;
    private LiveData<List<Activity>> AllActivities;
    private LiveData<List<String>> AllSubjects;

    ActivityRepo(Application application){
        ActivityRoomDatabase db = ActivityRoomDatabase.getDatabase(application);
        myActivityDao = db.activityDao();
        AllSubjects = myActivityDao.getgroups();
        AllActivities = myActivityDao.getEveryActivity();
    }

    public void incrementcount(String group,String job){
        new IncrementCountAsyncTask(myActivityDao,group,job).execute();
    }

    public void ChangeGroupName(String GroupName,String NewGroupName){
        new ChangeGroupNameAsyncTask(myActivityDao,GroupName,NewGroupName).execute();
    }

    public void ChangeActivityName(String GroupName,String ActivityName,String NewAcitivyName){
        new ChangeJobNameAsyncTask(myActivityDao,GroupName,ActivityName,NewAcitivyName).execute();
    }

    public void DeleteGroup(String GroupName){
        new DeleteGroupAsyncTask(myActivityDao,GroupName).execute();
    }

    public void DeleteActivity(String GroupName,String job){
        new DeleteActivityAsyncTask(myActivityDao,GroupName,job).execute();
    }

    public void  decrementcount(String group,String job){
        new DecrementCountAsyncTask(myActivityDao,group,job).execute();
    }

    public void changecount(String group,String job,int count){
        new ChangeCountAsyncTask(myActivityDao, group,job,count).execute();
    }

    public void addtocount(String group,String job,int count){
        new AddToCountAsyncTask(myActivityDao,group,job,count).execute();
    }

    public void subfromcount(String group,String job,int count){
        new SubFromCountAsyncTask(myActivityDao,group,job,count).execute();
    }

    LiveData<List<Activity>> getAllActivities(){
        return AllActivities;
    }

    LiveData<List<String>> getAllSubject(){
        return AllSubjects;
    }

    public void insert (Activity activity){
        new insertAsyncTask(myActivityDao).execute(activity);
    }

    //Various Async Tasks
    private static class insertAsyncTask extends AsyncTask<Activity,Void,Void>{
        private ActivityDao mAsyncTaskDao;

        insertAsyncTask(ActivityDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Activity... params){
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class DeleteGroupAsyncTask extends AsyncTask<Void,Void, Void>{
        private ActivityDao mAsyncTaskDao;
        private String GroupName;

        DeleteGroupAsyncTask(ActivityDao dao,String GroupName){
            mAsyncTaskDao = dao;
            this.GroupName = GroupName;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.DeleteGroup(GroupName);
            return null;
        }
    }

    private static class DeleteActivityAsyncTask extends AsyncTask<Void,Void,Void>{
        private ActivityDao activityDao;
        private String GroupName;
        private String JobName;

        DeleteActivityAsyncTask(ActivityDao dao,String GroupName,String JobName){
            activityDao = dao;
            this.GroupName = GroupName;
            this.JobName = JobName;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            activityDao.DeleteActivity(GroupName,JobName);
            return null;
        }
    }

    private static class IncrementCountAsyncTask extends AsyncTask<Void,Void,Void>{
        private ActivityDao mAsyncTaskDao;
        private String GroupName;
        private String TaskName;

        IncrementCountAsyncTask(ActivityDao dao,String GroupName,String TaskName){
            this.mAsyncTaskDao = dao;
            this.GroupName = GroupName;
            this.TaskName = TaskName;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.incrementcount(GroupName,TaskName);
            return null;
        }
    }

    private static class DecrementCountAsyncTask extends AsyncTask<Void,Void,Void>{
        private ActivityDao mAsyncTaskDao;
        private String GroupName;
        private String TaskName;

        DecrementCountAsyncTask(ActivityDao dao,String GroupName,String TaskName){
            this.mAsyncTaskDao = dao;
            this.GroupName = GroupName;
            this.TaskName = TaskName;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.decrementcount(GroupName,TaskName);
            return null;
        }
    }

    private static class ChangeCountAsyncTask extends AsyncTask<Void,Void,Void>{
        private ActivityDao mAsyncTaskDao;
        private String GroupName;
        private String TaskName;
        private int newCount;

        ChangeCountAsyncTask(ActivityDao dao,String GroupName,String TaskName,int NewCount){
            mAsyncTaskDao = dao;
            this.GroupName = GroupName;
            this.TaskName = TaskName;
            this.newCount = NewCount;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.changecount(GroupName,TaskName,newCount);
            return null;
        }
    }

    private static class ChangeJobNameAsyncTask extends AsyncTask<Void,Void,Void>{
        private ActivityDao activityDao;
        private String GroupName;
        private String JobName;
        private String NewJobName;

        ChangeJobNameAsyncTask(ActivityDao dao,String GroupName,String JobName,String NewJobName){
            activityDao = dao;
            this.GroupName = GroupName;
            this.JobName = JobName;
            this.NewJobName = NewJobName;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            activityDao.ChangeActivityName(GroupName,JobName,NewJobName);
            return null;
        }
    }

    private static class ChangeGroupNameAsyncTask extends AsyncTask<Void,Void,Void>{
        private ActivityDao activityDao;
        private String GroupName;
        private String NewGroupName;

        ChangeGroupNameAsyncTask(ActivityDao dao,String GroupName,String NewGroupName){
            activityDao = dao;
            this.GroupName = GroupName;
            this.NewGroupName = NewGroupName;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            activityDao.ChangeGroupName(GroupName,NewGroupName);
            return null;
        }
    }

    private static class AddToCountAsyncTask extends AsyncTask<Void,Void,Void>{
        private ActivityDao activityDao;
        private String GroupName;
        private String JobName;
        private int newCount;

        AddToCountAsyncTask(ActivityDao dao,String GroupName,String JobName,int newCount){
            activityDao = dao;
            this.GroupName = GroupName;
            this.JobName = JobName;
            this.newCount = newCount;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            activityDao.addtocount(GroupName,JobName,newCount);
            return null;
        }
    }

    private static class SubFromCountAsyncTask extends AsyncTask<Void,Void,Void>{
        private ActivityDao activityDao;
        private String GroupName;
        private String JobName;
        private int newCount;

        SubFromCountAsyncTask(ActivityDao dao,String GroupName,String JobName,int newCount){
            activityDao = dao;
            this.GroupName = GroupName;
            this.JobName = JobName;
            this.newCount = newCount;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            activityDao.subfromcount(GroupName,JobName,newCount);
            return null;
        }
    }
}
