package com.garifullin_timur.testing.ui.home;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.garifullin_timur.testing.Database.HomeWork;
import com.garifullin_timur.testing.Database.HomeWorkDao;
import com.garifullin_timur.testing.Database.HomeworkDB;
import com.garifullin_timur.testing.Database.RaspDB;
import com.garifullin_timur.testing.Database.RaspDao;
import com.garifullin_timur.testing.Database.Raspisan;
import com.garifullin_timur.testing.Database.Subject;
import com.garifullin_timur.testing.Database.SubjectDao;
import com.garifullin_timur.testing.Database.SubjectsDB;
import com.garifullin_timur.testing.Database.Teacher;
import com.garifullin_timur.testing.Database.TeacherDB;
import com.garifullin_timur.testing.Database.TeacherDao;
import com.garifullin_timur.testing.MainActivity;
import com.garifullin_timur.testing.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.w3c.dom.Text;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    FloatingActionButton fab;
    View root, root2;
    SubjectsDB db;
    SubjectDao subjectDao;
    TeacherDB tdb;
    TeacherDao teacherDao;
    RaspDao raspDao;
    HomeWorkDao homeWorkDao;
    TabLayout tabLayout;
    RecyclerView recyclerView;
    RVsubjectAdapter rVsubjectAdapter;
    Subject clicked;
    EditText openDialogCabinet;
    EditText openDialogSubjectName;
    TextView deleteSubjectName;
    EditText addSubjectName;
    EditText addSubjectCabinet;
    AlertDialog.Builder addSubjectBuilder, openSubjectBuilder, deleteSubjectBuilder;
    RVsubjectAdapter.OnItemClickListener mOnClickListener;
    Spinner spinner;
    Teacher selectedTeacher;
//    AutoCompleteTextView autoCompleteTextView;
    List<Teacher> toSpinner;
//    SpinnerAdapter spinAdapter;
    private HomeViewModel homeViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(false);
        db = SubjectsDB.create(getContext(), false);
        subjectDao = db.subjectDao();
        tdb = TeacherDB.create(getContext(), false);
        teacherDao = tdb.teacherDao();
        raspDao = RaspDB.create(getContext(), false).raspDao();
        homeWorkDao = HomeworkDB.create(getContext(), false).homeWorkDao();
        setDialogs();

        mOnClickListener = new RVsubjectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                        clicked = rVsubjectAdapter.getItem(position);

                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        root2 = inflater.inflate(R.layout.subject_open_dialog, null);
                        View root3 = inflater.inflate(R.layout.see_subject_dialog, null);
                        TextView seeDialogSubjectName = root3.findViewById(R.id.subjectName);
                        TextView seeDialogSubjectCabinet = root3.findViewById(R.id.subjectCabinet);
                        TextView seeDialogSubjectTeach = root3.findViewById(R.id.subjectTeach);
                        new Thread(){
                            @Override
                            public void run() {
                                spinner = root2.findViewById(R.id.spinner);
                                TextView clicked_teach = root2.findViewById(R.id.subjectTeach);
                                toSpinner = teacherDao.selectAll();
                                List<String> names = new AbstractList<String>() {
                                    @Override
                                    public int size() { return toSpinner.size(); }

                                    @Override
                                    public String get(int location) {
                                        return toSpinner.get(location).getName();
                                    }
                                };
                                ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item);
                                spinAdapter.addAll(names);
                                int clickedTeachId = clicked.getTeach_id();
                                spinner.setAdapter(spinAdapter);
                                Log.d("mytag","" + clickedTeachId);
                                int pos;
                                if (clickedTeachId != -1){
                                    Teacher setted = teacherDao.findById(clickedTeachId);
                                    seeDialogSubjectTeach.setText(setted.getName());
                                }


                                Log.d("mytag","" + clicked.getTeach_id());
                                selectedTeacher = null;
                                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        selectedTeacher = toSpinner.get(position);

                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        openDialogSubjectName = root2.findViewById(R.id.subjectName);
                                        openDialogCabinet = root2.findViewById(R.id.subjectCabinet);
                                        openDialogSubjectName.setText(clicked.getName());
                                        openDialogCabinet.setText(clicked.getCabinet());
                                        seeDialogSubjectName.setText(clicked.getName());
                                        seeDialogSubjectCabinet.setText(clicked.getCabinet());


                                        openSubjectBuilder.setView(root3);
                                        openSubjectBuilder
                                                .setNegativeButton("Изменить", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                openSubjectBuilder.setView(root2)
                                                        .setNegativeButton("Закрыть", null)
                                                        .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                Subject NewSubject = new Subject(clicked.get_id(), openDialogSubjectName.getText().toString(), openDialogCabinet.getText().toString());
                                                                if (selectedTeacher != null){
                                                                    NewSubject.setTeach_id(selectedTeacher.get_id());
                                                                }
                                                                else if (clicked.getTeach_id() != -1){
                                                                    NewSubject.setTeach_id(clicked.getTeach_id());
                                                                }
                                                                new Thread(){
                                                                    @Override
                                                                    public void run() {
                                                                        subjectDao.update(NewSubject);
                                                                        List<Subject> c = subjectDao.selectAll();
                                                                        setInUIThread(c);
                                                                    }
                                                                }.start();

                                                            }
                                                        });
                                                openSubjectBuilder.show();
                                            }
                                        })
                                                .setPositiveButton("Ок", null);
                                        openSubjectBuilder.show();
                                    }
                                });
                            }
                        }.start();



            }
        };
        new Thread(){
            @Override
            public void run() {
                recyclerView = root.findViewById(R.id.rv);
                LinearLayoutManager llm = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(llm);


            }
        }.start();


        fab = getActivity().findViewById(R.id.fab);

        new Thread(){
            @Override
            public void run() {
                List<Subject> c = subjectDao.selectAll();
                setInUIThread(c);
            }
        }.start();
        fab.setOnClickListener(this::onAddSubjectClick);
        return root;
    }
    public void setInUIThread(List<Subject> c) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("mytag", "" + c.size());
                rVsubjectAdapter = new RVsubjectAdapter(c, mOnClickListener);
                recyclerView.setAdapter(rVsubjectAdapter);
            }
            });

}
    public void onAddSubjectClick(View v) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        root2 = inflater.inflate(R.layout.add_subject_dialog, null);
        spinner = root2.findViewById(R.id.spinner);
        new Thread(){
            @Override
            public void run() {
                toSpinner = teacherDao.selectAll();
                String choose = "Выберите учителя";
                List<String> names = new AbstractList<String>() {
                    @Override
                    public int size() { return toSpinner.size(); }

                    @Override
                    public String get(int location) {
                        return toSpinner.get(location).getName();
                    }
                };
                ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item);
//                spinAdapter.add(choose);
                spinAdapter.addAll(names);

                spinner.setAdapter(spinAdapter);
                selectedTeacher = null;
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedTeacher = toSpinner.get(position);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        }.start();
        addSubjectName = root2.findViewById(R.id.addSubjectName);
        addSubjectCabinet = root2.findViewById(R.id.addSubjectCab);
        addSubjectBuilder.setView(root2)
                .show();
            }
    public void setDialogs(){

        addSubjectBuilder = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogTheme);
        addSubjectBuilder
                .setTitle("Добавить урок")
                .setNegativeButton("Отмена", null)
                .setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread() {
                            @Override
                            public void run() {
                                Subject added = new Subject(addSubjectName.getText().toString(), addSubjectCabinet.getText().toString());
                                if (selectedTeacher != null){
                                    Log.d("mytag", selectedTeacher.getName());
                                    Log.d("mytag", "" + selectedTeacher.get_id());
                                    added.setTeach_id(selectedTeacher.get_id());
                                }
                                else {
                                    added.setTeach_id(-1);
                                }

                                subjectDao.insert(added);
                                List<Subject> c = subjectDao.selectAll();
                                setInUIThread(c);
                            }
                        }.start();
                    }
                });

        deleteSubjectBuilder = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogTheme);
        deleteSubjectBuilder
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(){
                            @Override
                            public void run() {
                                subjectDao.delete(clicked);
                                List<Subject> c = subjectDao.selectAll();
                                List<Raspisan> cr = raspDao.findBySubId(clicked.get_id());
                                for (int i = 0; i < cr.size(); i++) {
                                    Raspisan rr = cr.get(i);
                                    rr.setSub_id(-1);
                                    raspDao.update(rr);
                                }
                                List<HomeWork> hh = homeWorkDao.findBySubId(clicked.get_id());
                                for (int i = 0; i < hh.size(); i++) {
                                    HomeWork hhh = hh.get(i);
                                    hhh.setSub_id(-1);
                                    homeWorkDao.update(hhh);
                                }
                                setInUIThread(c);
                            }
                        }.start();

                    }
                })
                .setNegativeButton("Нет", null);

        openSubjectBuilder = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogTheme);
        openSubjectBuilder
                .setTitle("Информация о предмете")
                .setNeutralButton("Удалить", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                        LayoutInflater inflater = getActivity().getLayoutInflater();
                                        View root4 = inflater.inflate(R.layout.delete_dialog, null);
                                        deleteSubjectName = root4.findViewById(R.id.deleteSubjectName);
                                        TextView deleteInfo = root4.findViewById(R.id.del_info);
                                        String del_info = getResources().getString(R.string.del_subj);
                                        deleteInfo.setText(del_info);
                                        deleteSubjectName.setText(clicked.getName());
                                        deleteSubjectBuilder.setView(root4);
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                deleteSubjectBuilder.show();
                                            }
                                        });

                            }
                        }
                )
                .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Subject NewSubject = new Subject(clicked.get_id(), openDialogSubjectName.getText().toString(), openDialogCabinet.getText().toString());
                        if (selectedTeacher != null){
                            NewSubject.setTeach_id(selectedTeacher.get_id());
                        }
                        new Thread(){
                            @Override
                            public void run() {
                                subjectDao.update(NewSubject);
                                List<Subject> c = subjectDao.selectAll();
                                setInUIThread(c);
                            }
                        }.start();

                    }
                });


    }
}