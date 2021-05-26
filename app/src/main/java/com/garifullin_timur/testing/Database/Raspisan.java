package com.garifullin_timur.testing.Database;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "timetable")
public class Raspisan {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    int id;
    int sub_id;
    int dayIndex;
    public Raspisan(int dayIndex, int sub_id) {
        this.sub_id = sub_id;
        this.dayIndex = dayIndex;
    }
    @Ignore
    public Raspisan(int _id, int dayIndex, int sub_id) {
        this.id = _id;
        this.sub_id = sub_id;
        this.dayIndex = dayIndex;
    }

    public int get_id() {
        return id;
    }

    public void set_id(int _id) {
        this.id = _id;
    }

    public int getSub_id() {
        return sub_id;
    }

    public void setSub_id(int sub_id) {
        this.sub_id = sub_id;
    }

    public int getDayIndex() {
        return dayIndex;
    }

    public void setDayIndex(int dayIndex) {
        this.dayIndex = dayIndex;
    }
}
