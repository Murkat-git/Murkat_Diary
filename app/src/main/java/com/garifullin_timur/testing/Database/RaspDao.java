package com.garifullin_timur.testing.Database;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RaspDao {
    @Query("SELECT * FROM timetable ORDER BY id")
    List<Raspisan> selectAll();

    @Query("SELECT * FROM timetable WHERE id=:id")
    Raspisan findById(int id);

    @Query("SELECT * FROM timetable WHERE dayIndex=:dayIndex")
    List<Raspisan> findByDay(int dayIndex);

    @Query("SELECT * FROM timetable WHERE sub_id=:id")
    List<Raspisan> findBySubId(int id);

    @Insert
    void insert(Raspisan... raspisans);

    @Delete
    void delete(Raspisan... raspisans);

    @Update
    void update(Raspisan... raspisans);
}
