package com.example.heinzraja.activitytracker;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = Activity.class,version = 1)
public abstract class ActivityRoomDatabase extends RoomDatabase {
    public abstract ActivityDao activityDao();

    private static ActivityRoomDatabase INSTANCE;

    static ActivityRoomDatabase getDatabase(final Context context){
        if(INSTANCE==null){
            synchronized (ActivityRoomDatabase.class){
                if(INSTANCE==null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),ActivityRoomDatabase.class,"activity_database").build();
                }
            }
        }
        return INSTANCE;
    }
}
