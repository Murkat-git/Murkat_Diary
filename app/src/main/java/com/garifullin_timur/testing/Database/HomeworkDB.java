package com.garifullin_timur.testing.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {HomeWork.class}, version = 1)
public abstract class HomeworkDB extends RoomDatabase {
    public abstract HomeWorkDao homeWorkDao();

    private static final String DB_NAME="homeworks.db";
    private static volatile HomeworkDB INSTANCE = null;

    synchronized static HomeworkDB get(Context ctxt){
        if (INSTANCE == null){
            INSTANCE = create(ctxt, false);
        }
        return INSTANCE;
    }
    public static HomeworkDB create(Context ctxt, boolean memoryOnly){
        RoomDatabase.Builder<HomeworkDB> b;
        if (memoryOnly){
            b = Room.inMemoryDatabaseBuilder(ctxt.getApplicationContext(), HomeworkDB.class);
        }
        else {
            b = Room.databaseBuilder(ctxt.getApplicationContext(),HomeworkDB.class, DB_NAME);
        }
        return b.build();
    }
}