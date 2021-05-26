package com.garifullin_timur.testing.Database;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "subjects"
//        ,
//        foreignKeys = @ForeignKey(entity = Teacher.class,
//                parentColumns = "_id",
//                childColumns = "teach_id",
//                onDelete = ForeignKey.CASCADE)
)
public class Subject {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    int _id;
    String name;
    String cabinet;
    int teach_id;
    public Subject(String name, String cabinet) {
        this.name = name;
        this.cabinet = cabinet;
    }
    @Ignore
    public Subject(int _id, String name, String cabinet) {
        this._id = _id;
        this.name = name;
        this.cabinet = cabinet;
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

    public int getTeach_id() {
        return teach_id;
    }

    public void setTeach_id(int teach_id) {
        this.teach_id = teach_id;
    }
}
