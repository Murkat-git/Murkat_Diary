package com.garifullin_timur.testing.Database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "homeworks")
public class HomeWork {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    int _id;
    String subjectName;
    String dz;
    int dayInd;
    boolean done;
    public HomeWork(String subjectName, String dz, int dayInd) {
        this.subjectName = subjectName;
        this.dz = dz;
        this.dayInd = dayInd;
        done = false;
    }
    @Ignore
    public HomeWork(int _id, String subjectName, String dz, int dayInd, boolean done) {
        this._id = _id;
        this.subjectName = subjectName;
        this.dz = dz;
        this.dayInd = dayInd;
        this.done = done;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getDz() {
        return dz;
    }

    public void setDz(String dz) {
        this.dz = dz;
    }

    public int getDayInd() {
        return dayInd;
    }

    public void setDayInd(int dayInd) {
        this.dayInd = dayInd;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
