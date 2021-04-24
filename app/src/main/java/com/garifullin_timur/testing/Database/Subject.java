package com.garifullin_timur.testing.Database;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "subjects")
public class Subject {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    int _id;
    String name;
    String cabinet;
    int dayIndex;
    public Subject(String name, String cabinet, int dayIndex) {
        this.name = name;
        this.cabinet = cabinet;
        this.dayIndex = dayIndex;
    }
    @Ignore
    public Subject(int _id, String name, String cabinet, int dayIndex) {
        this._id = _id;
        this.name = name;
        this.cabinet = cabinet;
        this.dayIndex = dayIndex;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCabinet() {
        return cabinet;
    }

    public void setCabinet(String cabinet) {
        this.cabinet = cabinet;
    }

    public int getDayIndex() {
        return dayIndex;
    }

    public void setDayIndex(int dayIndex) {
        this.dayIndex = dayIndex;
    }
}
