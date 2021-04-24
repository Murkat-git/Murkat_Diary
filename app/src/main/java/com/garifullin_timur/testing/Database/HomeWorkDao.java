package com.garifullin_timur.testing.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface HomeWorkDao {
    @Query("SELECT * FROM homeworks WHERE dayInd=:dayInd")
    List<HomeWork> findByDay(int dayInd);

    @Insert
    void insert(HomeWork... homeWork);

    @Delete
    void delete(HomeWork... homeWork);

    @Update
    void update(HomeWork... homeWork);
}
