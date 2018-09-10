package com.example.heinzraja.activitytracker;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ActivityDao {

    @Insert
    void insert(Activity activity);

    @Query("SELECT * FROM activity_table")
    LiveData<List<Activity>> getEveryActivity();

    @Query("UPDATE activity_table " +
            "SET activity = :NewActivityName " +
            "WHERE `group` LIKE :GroupName AND activity LIKE :ActivityName")
    void ChangeActivityName(String GroupName,String ActivityName,String NewActivityName);

    @Query("DELETE FROM activity_table WHERE `group` LIKE :GroupName")
    void DeleteGroup(String GroupName);

    @Query("DELETE FROM activity_table " +
            "WHERE `group` LIKE :GroupName " +
            "AND activity LIKE :job")
    void DeleteActivity(String GroupName,String job);

    @Query("UPDATE activity_table" +
            " SET count=count+1 " +
            "WHERE `group` LIKE :Group AND activity LIKE :Activity")
    void incrementcount(String Group, String Activity);

    @Query("UPDATE activity_table" +
            " SET count=count-1" +
            " WHERE `group` LIKE :Group AND activity LIKE :Activity AND count > 0")
    void decrementcount(String Group, String Activity);

    @Query("UPDATE activity_table " +
            "SET count = :newcount " +
            "WHERE `group` LIKE :Group AND activity LIKE :Activity")
    void changecount(String Group, String Activity, int newcount);

    @Query("SELECT DISTINCT `group` FROM activity_table")
    LiveData<List<String>> getgroups();

    @Query("UPDATE activity_table" +
            " SET `group` = :newGroupName " +
            "WHERE `group` LIKE :GroupName")
    void ChangeGroupName(String GroupName,String newGroupName);

    @Query("UPDATE activity_table " +
            "SET count = count+:newcount " +
            "WHERE `group` LIKE :Group AND activity LIKE :Activity")
    void addtocount(String Group,String Activity,int newcount);

    @Query("UPDATE activity_table " +
            "SET count = count-:newcount " +
            "WHERE `group` LIKE :Group AND activity LIKE :Activity AND count-:newcount >= 0")
    void subfromcount(String Group,String Activity,int newcount);


}
