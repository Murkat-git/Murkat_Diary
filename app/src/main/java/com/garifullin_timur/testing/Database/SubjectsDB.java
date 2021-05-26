package com.garifullin_timur.testing.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Subject.class, Teacher.class}, version = 6)
public abstract class SubjectsDB extends RoomDatabase {
    public abstract SubjectDao subjectDao();

    private static final String DB_NAME="subjects.db";
    private static volatile SubjectsDB INSTANCE = null;

    synchronized static SubjectsDB get(Context ctxt){
        if (INSTANCE == null){
            INSTANCE = create(ctxt, false);
        }
        return INSTANCE;
    }
    public static SubjectsDB create(Context ctxt, boolean memoryOnly){
        RoomDatabase.Builder<SubjectsDB> b;
        if (memoryOnly){
            b = Room.inMemoryDatabaseBuilder(ctxt.getApplicationContext(), SubjectsDB.class);
        }
        else {
            b = Room.databaseBuilder(ctxt.getApplicationContext(),SubjectsDB.class, DB_NAME);
        }
        return b.build();
    }
}
