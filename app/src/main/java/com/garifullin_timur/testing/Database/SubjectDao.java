package com.garifullin_timur.testing.Database;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SubjectDao {
    @Query("SELECT * FROM subjects ORDER BY name")
    List<Subject> selectAll();

    @Query("SELECT * FROM subjects WHERE _id=:id")
    Subject findById(int id);

//    @Query("SELECT * FROM subjects WHERE dayIndex=:dayIndex")
//    List<Subject> findByDay(int dayIndex);
    @Query("SELECT * FROM subjects WHERE teach_id=:id")
    List<Subject> findByTeach(int id);


    @Insert
    void insert(Subject... subjects);

    @Delete
    void delete(Subject... subjects);

    @Update
    void update(Subject... subjects);
}
