package com.garifullin_timur.testing.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Raspisan.class}, version = 3)
public abstract class RaspDB extends RoomDatabase {
    public abstract RaspDao raspDao();

    private static final String DB_NAME="timetable.db";
    private static volatile RaspDB INSTANCE = null;

    synchronized static RaspDB get(Context ctxt){
        if (INSTANCE == null){
            INSTANCE = create(ctxt, false);
        }
        return INSTANCE;
    }
    public static RaspDB create(Context ctxt, boolean memoryOnly){
        RoomDatabase.Builder<RaspDB> b;
        if (memoryOnly){
            b = Room.inMemoryDatabaseBuilder(ctxt.getApplicationContext(), RaspDB.class);
        }
        else {
            b = Room.databaseBuilder(ctxt.getApplicationContext(),RaspDB.class, DB_NAME);
        }
        return b.build();
    }
}
