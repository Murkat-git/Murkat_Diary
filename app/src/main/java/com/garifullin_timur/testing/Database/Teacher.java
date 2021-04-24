package com.garifullin_timur.testing.Database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "teachers")
public class Teacher {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    int _id;
    String name;
    String subjects;
    String tel;
    String email;

    public Teacher(String name,String subjects, String tel, String email) {
        this.name = name;
        this.subjects = subjects;
        this.tel = tel;
        this.email = email;

    }
    @Ignore
    public Teacher(int _id, String name, String subjects, String tel, String email) {
        this._id = _id;
        this.name = name;
        this.subjects = subjects;
        this.tel = tel;
        this.email = email;
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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubjects() {
        return subjects;
    }

    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }
}
