package com.garifullin_timur.testing.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;
@Dao
public interface TeacherDao {
    @Query("SELECT * FROM teachers ORDER BY name")
    List<Teacher> selectAll();

    @Query("SELECT * FROM teachers WHERE _id=:id")
    Teacher findById(int id);

//    @Transaction
//    @Query("SELECT * FROM teachers WHERE _id = :teachId")
//    TeacherWithSubjects getTeacherBy(int teachId);

    @Insert
    void insert(Teacher... teachers);

    @Delete
    void delete(Teacher... teachers);

    @Update
    void update(Teacher... teachers);
}