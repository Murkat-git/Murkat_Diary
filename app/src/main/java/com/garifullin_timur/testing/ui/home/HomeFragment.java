package com.garifullin_timur.testing.ui.home;

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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.garifullin_timur.testing.Database.Subject;
import com.garifullin_timur.testing.Database.SubjectDao;
import com.garifullin_timur.testing.Database.SubjectsDB;
import com.garifullin_timur.testing.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.List;


public class HomeFragment extends Fragment {
    FloatingActionButton fab;
    View root, root2, root3;
    SubjectsDB db;
    SubjectDao subjectDao;
    TabLayout tabLayout;
    RecyclerView recyclerView;
    RVsubjectAdapter rVsubjectAdapter;
    int dayInd = 0;
    Subject clicked;
    TextView openDialogCabinet;
    TextView openDialogSubjectName;
    TextView deleteSubjectName;
    EditText addSubjectName;
    EditText addSubjectCabinet;
    AlertDialog.Builder addSubjectBuilder, openSubjectBuilder, deleteSubjectBuilder;
    RVsubjectAdapter.OnItemClickListener mOnClickListener;
    private HomeViewModel homeViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_home, container, false);
        setDialogs();
        mOnClickListener = new RVsubjectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                        clicked = rVsubjectAdapter.getItem(position);

                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        root2 = inflater.inflate(R.layout.subject_open_dialog, null);
                        openDialogSubjectName = root2.findViewById(R.id.subjectName);
                        openDialogCabinet = root2.findViewById(R.id.subjectCabinet);
                        openDialogSubjectName.setText(clicked.getName());
                        openDialogCabinet.setText(clicked.getCabinet());
                        openSubjectBuilder.setView(root2);
                        openSubjectBuilder.show();

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
        db = SubjectsDB.create(getContext(), false);
        subjectDao = db.subjectDao();
        new Thread(){
            @Override
            public void run() {
                List<Subject> c = subjectDao.findByDay(dayInd);
                setInUIThread(c);
            }
        }.start();

//        listView = root.findViewById(R.id.rasp);
        tabLayout = root.findViewById(R.id.tabLSubjects);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                dayInd = tab.getPosition();
                new Thread(){
                    @Override
                    public void run() {
                        List<Subject> c = subjectDao.findByDay(dayInd);
                        setInUIThread(c);
                    }
                }.start();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

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
        root3 = inflater.inflate(R.layout.add_subject_dialog, null);
        addSubjectName = root2.findViewById(R.id.addSubjectName);
        addSubjectCabinet = root2.findViewById(R.id.addSubjectCab);
        addSubjectBuilder.setView(root3)
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
                                subjectDao.insert(new Subject(addSubjectName.getText().toString(), addSubjectCabinet.getText().toString(), dayInd));
                                List<Subject> c = subjectDao.findByDay(dayInd);
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
                                List<Subject> c = subjectDao.findByDay(dayInd);
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
                                        View root4 = inflater.inflate(R.layout.delete_subject_dialog, null);
                                        deleteSubjectName = root4.findViewById(R.id.deleteSubjectName);
                                        deleteSubjectName.setText(clicked.getName());
                                        deleteSubjectBuilder.setView(root4)
                                                .show();

                            }
                        }
                )
                .setNegativeButton("Закрыть", null)
                .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Subject NewSubject = new Subject(clicked.get_id(), "" + openDialogSubjectName.getText().toString(), openDialogCabinet.getText().toString(), clicked.getDayIndex());
                        new Thread(){
                            @Override
                            public void run() {
                                subjectDao.update(NewSubject);
                                List<Subject> c = subjectDao.findByDay(dayInd);
                                setInUIThread(c);
                            }
                        }.start();

                    }
                });


    }
}