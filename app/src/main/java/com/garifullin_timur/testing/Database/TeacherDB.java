package com.garifullin_timur.testing.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
@Database(entities = {Teacher.class}, version = 1)
public abstract class TeacherDB extends RoomDatabase {
    public abstract TeacherDao teacherDao();

    private static final String DB_NAME="teachers.db";
    private static volatile TeacherDB INSTANCE = null;

    synchronized static TeacherDB get(Context ctxt){
        if (INSTANCE == null){
            INSTANCE = create(ctxt, false);
        }
        return INSTANCE;
    }
    public static TeacherDB create(Context ctxt, boolean memoryOnly){
        RoomDatabase.Builder<TeacherDB> b;
        if (memoryOnly){
            b = Room.inMemoryDatabaseBuilder(ctxt.getApplicationContext(), TeacherDB.class);
        }
        else {
            b = Room.databaseBuilder(ctxt.getApplicationContext(),TeacherDB.class, DB_NAME);
        }
        return b.build();
    }

}
