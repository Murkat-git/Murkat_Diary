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
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(TeacherViewModel.class);
        View root = inflater.inflate(R.layout.fragment_teacher, container, false);
        db = TeacherDB.create(getContext(), false);
        teacherDao = db.teacherDao();
        onItemClickListener = new RVteacherAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Teacher clicked = rVteacherAdapter.getItem(position);
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View root2 = inflater.inflate(R.layout.teacher_open_dialog, null);
                EditText tx1 = root2.findViewById(R.id.openTeacherName);
                EditText tx2 = root2.findViewById(R.id.openTeacherEmail);
                EditText tx3 = root2.findViewById(R.id.openTeacherTel);
                EditText tx4 = root2.findViewById(R.id.openTeacherSubjects);
                tx1.setText(clicked.getName());
                tx2.setText(clicked.getEmail());
                tx3.setText(clicked.getTel());
                tx4.setText(clicked.getSubjects());
                ImageView img = root2.findViewById(R.id.openTeacherTrash);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                        .setView(root2)
                        .setNeutralButton("Закрыть", null)
                        .setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Teacher newTeacher = new Teacher(clicked.get_id(), tx1.getText().toString(), tx4.getText().toString(), tx3.getText().toString(), tx2.getText().toString());
                                new Thread(){
                                    @Override
                                    public void run() {
                                        teacherDao.update(newTeacher);
                                        List<Teacher> c = teacherDao.selectAll();
                                        setInUIThread(c);
                                    }
                                }.start();
                            }
                        });
                AlertDialog dialog = builder.create();
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Thread(){
                            @Override
                            public void run() {
                                dialog.dismiss();
                                teacherDao.delete(clicked);
                                List<Teacher> c = teacherDao.selectAll();
                                setInUIThread(c);
                            }
                        }.start();
                    }
                });
                dialog.show();


            }
        };
        rv = root.findViewById(R.id.teacher_Rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(linearLayoutManager);
        fab = getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(this::onAddTeacherClick);
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
    public void onAddTeacherClick(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogTheme);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View root2 = inflater.inflate(R.layout.add_teacher_dialog, null);
        EditText tx1 = root2.findViewById(R.id.addTeacherName);
        EditText tx2 = root2.findViewById(R.id.addTeacherEmail);
        EditText tx3 = root2.findViewById(R.id.addTeacherTel);
        EditText tx4 = root2.findViewById(R.id.addTeacherSubjects);
        builder.setView(root2)
                .setTitle("Добавление учителя")
                .setNeutralButton("Закрыть", null)
                .setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Teacher added = new Teacher(tx1.getText().toString(), tx4.getText().toString(), tx3.getText().toString(), tx2.getText().toString());
                        new Thread(){
                            @Override
                            public void run() {
                                teacherDao.insert(added);
                                List<Teacher> c = teacherDao.selectAll();
                                setInUIThread(c);
                            }
                        }.start();
                    }
                }).show();
    }
}