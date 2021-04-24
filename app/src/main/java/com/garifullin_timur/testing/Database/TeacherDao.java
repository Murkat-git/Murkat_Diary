package com.garifullin_timur.testing.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface TeacherDao {
    @Query("SELECT * FROM teachers ORDER BY name")
    List<Teacher> selectAll();

    @Query("SELECT * FROM teachers WHERE _id=:id")
    Teacher findById(int id);

    @Insert
    void insert(Teacher... teachers);

    @Delete
    void delete(Teacher... teachers);

    @Update
    void update(Teacher... teachers);
}