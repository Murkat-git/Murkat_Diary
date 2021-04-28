package com.garifullin_timur.testing.ui.teacher;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.garifullin_timur.testing.Database.Subject;
import com.garifullin_timur.testing.Database.SubjectDao;
import com.garifullin_timur.testing.Database.SubjectsDB;
import com.garifullin_timur.testing.Database.Teacher;
import com.garifullin_timur.testing.Database.TeacherDB;
import com.garifullin_timur.testing.Database.TeacherDao;
import com.garifullin_timur.testing.R;
import com.garifullin_timur.testing.ui.home.RVsubjectAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class TeacherFragment extends Fragment {

    private TeacherViewModel galleryViewModel;
    TeacherDB db;
    TeacherDao teacherDao;
    RecyclerView rv;
    RVteacherAdapter rVteacherAdapter;
    FloatingActionButton fab;
    RVteacherAdapter.OnItemClickListener onItemClickListener;
    AlertDialog.Builder addTeacher, openTeacher, deleteTeacher;
    LayoutInflater inflater1;
    EditText addTeacherName, addteacherEmail, addTeacherTel, addTeacherSubjects;
    Teacher clicked;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(TeacherViewModel.class);
        View root = inflater.inflate(R.layout.fragment_teacher, container, false);
        db = TeacherDB.create(getContext(), false);
        teacherDao = db.teacherDao();
        inflater1 = getActivity().getLayoutInflater();
        setDialogs();
        onItemClickListener = new RVteacherAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                clicked = rVteacherAdapter.getItem(position);
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View root3 = inflater.inflate(R.layout.teacher_open_dialog, null);
                EditText tx1 = root3.findViewById(R.id.openTeacherName);
                EditText tx2 = root3.findViewById(R.id.openTeacherEmail);
                EditText tx3 = root3.findViewById(R.id.openTeacherTel);
                EditText tx4 = root3.findViewById(R.id.openTeacherSubjects);
                tx1.setText(clicked.getName());
                tx2.setText(clicked.getEmail());
                tx3.setText(clicked.getTel());
                tx4.setText(clicked.getSubjects());
                openTeacher.setView(root3);
                openTeacher.show();

            }
        };
        rv = root.findViewById(R.id.teacher_Rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(linearLayoutManager);
        fab = getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View root2 = inflater1.inflate(R.layout.add_teacher_dialog, null);
                addTeacherName = root2.findViewById(R.id.addTeacherName);
                addteacherEmail = root2.findViewById(R.id.addTeacherEmail);
                addTeacherTel = root2.findViewById(R.id.addTeacherTel);
                addTeacherSubjects = root2.findViewById(R.id.addTeacherSubjects);
                addTeacher.setView(root2);
                addTeacher.show();
            }
        });
        new Thread(){
            @Override
            public void run() {
                List<Teacher> c = teacherDao.selectAll();
                setInUIThread(c);
            }
        }.start();
        return root;
    }
    public void setInUIThread(List<Teacher> c) {
        Context ctx = getContext();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("mytag", "" + c.size());
                rVteacherAdapter = new RVteacherAdapter(c, onItemClickListener);
                rv.setAdapter(rVteacherAdapter);
            }
        });

    }
    public void setDialogs(){
        addTeacher = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogTheme);
        View root2 = inflater1.inflate(R.layout.add_teacher_dialog, null);
        addTeacherName = root2.findViewById(R.id.addTeacherName);
        addteacherEmail = root2.findViewById(R.id.addTeacherEmail);
        addTeacherTel = root2.findViewById(R.id.addTeacherTel);
        addTeacherSubjects = root2.findViewById(R.id.addTeacherSubjects);
        addTeacher.setView(root2)
                .setTitle("Добавление учителя")
                .setNegativeButton("Закрыть", null)
                .setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Teacher added = new Teacher(addTeacherName.getText().toString(), addTeacherSubjects.getText().toString(), addTeacherTel.getText().toString(), addteacherEmail.getText().toString());
                        new Thread(){
                            @Override
                            public void run() {
                                teacherDao.insert(added);
                                List<Teacher> c = teacherDao.selectAll();
                                setInUIThread(c);
                            }
                        }.start();
                    }
                });



        deleteTeacher = new AlertDialog.Builder(getContext())
                .setNegativeButton("Нет", null)
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(){
                            @Override
                            public void run() {
                                teacherDao.delete(clicked);
                                List<Teacher> c = teacherDao.selectAll();
                                setInUIThread(c);
                            }
                        }.start();
                    }
                });

        openTeacher = new AlertDialog.Builder(getContext())
                .setTitle("Информация об учителе")
                .setView(root2)
                .setNeutralButton("Удалить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        View root3 = inflater1.inflate(R.layout.delete_dialog, null);
                        TextView delete_info = root3.findViewById(R.id.del_info);
                        TextView delete_name = root3.findViewById(R.id.deleteSubjectName);
                        String del_info = getResources().getString(R.string.del_teach);
                        delete_info.setText(del_info);
                        delete_name.setText(clicked.getName());
                        deleteTeacher.setView(root3);
                        deleteTeacher.show();
                    }
                })
                .setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Teacher newTeacher = new Teacher(clicked.get_id(), addTeacherName.getText().toString(), addTeacherSubjects.getText().toString(), addTeacherTel.getText().toString(), addteacherEmail.getText().toString());
                        new Thread(){
                            @Override
                            public void run() {
                                teacherDao.update(newTeacher);
                                List<Teacher> c = teacherDao.selectAll();
                                setInUIThread(c);
                            }
                        }.start();
                    }
                })
                .setNegativeButton("Закрыть", null);



    }
}