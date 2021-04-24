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
    View root;
    SubjectsDB db;
    ListView listView;
    SubjectDao subjectDao;
    TabLayout tabLayout;
    RecyclerView recyclerView;
    RVsubjectAdapter rVsubjectAdapter;
    int dayInd = 0;
    RVsubjectAdapter.OnItemClickListener mOnClickListener;
    private HomeViewModel homeViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_home, container, false);
        mOnClickListener = new RVsubjectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogTheme);
                new Thread(){
                    @Override

                    public void run() {
                        Subject clicked = rVsubjectAdapter.getItem(position);

                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        View root2 = inflater.inflate(R.layout.subject_open_dialog, null);
                        TextView tx2 = root2.findViewById(R.id.subjectName);
                        TextView tx3 = root2.findViewById(R.id.subjectCabinet);
                        tx2.setText(clicked.getName());
                        tx3.setText(clicked.getCabinet());
                        View root3 = inflater.inflate(R.layout.open_dialog_title, null);
                        TextView tx4 = root3.findViewById(R.id.openInfo);
                        tx4.setText("Информация о предмете");
                        ImageView trash = root3.findViewById(R.id.trash);
                        builder.setView(root2)
                                .setCustomTitle(root3)
                                .setNeutralButton("Закрыть", null
                                )
                                .setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Subject NewSubject = new Subject(clicked.get_id(), "" + tx2.getText(), "" + tx3.getText(), clicked.getDayIndex());
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
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog dialog = builder.create();
                                trash.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        new Thread(){
                                            @Override
                                            public void run() {
                                                dialog.dismiss();
                                                subjectDao.delete(clicked);
                                                List<Subject> c = subjectDao.findByDay(dayInd);
                                                setInUIThread(c);
                                            }
                                        }.start();
                                    }
                                });
                                dialog.show();
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
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                new Thread(){
//                    @Override
//
//                    public void run() {
//                        Cursor c = (Cursor) listView.getItemAtPosition(position);
//                        int SubjectId = c.getInt(0);
//                        Subject clicked = subjectDao.findById(SubjectId);
//
//                        LayoutInflater inflater = getActivity().getLayoutInflater();
//                        View root2 = inflater.inflate(R.layout.subject_open_dialog, null);
//                        TextView tx1 = root2.findViewById(R.id.subjectId);
//                        TextView tx2 = root2.findViewById(R.id.subjectName);
//                        TextView tx3 = root2.findViewById(R.id.subjectCabinet);
//                        tx1.setText("" + clicked.get_id());
//                        tx2.setText(clicked.getName());
//                        tx3.setText(clicked.getCabinet());
//                        builder.setView(root2)
//                                .setNegativeButton("Закрыть", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//
//                                    }
//                                })
//                                .setTitle("Информация о предмете");
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                builder.show();
//                            }
//                        });
//                    }
//                }.start();
//
//
//            }
//        });
        return root;
    }
    public void setInUIThread(List<Subject> c) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("mytag", "" + c.size());
                rVsubjectAdapter = new RVsubjectAdapter(c, mOnClickListener);
//                rVsubjectAdapter.setOnEntryClickListener(new RVsubjectAdapter.OnEntryClickListener() {
//                    @Override
//                    public void onEntryClick(View view, int position) {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                        new Thread(){
//                            @Override
//
//                            public void run() {
//
//                                Subject SubjectClck = rVsubjectAdapter.getItem(position);
//
//                                LayoutInflater inflater = getActivity().getLayoutInflater();
//                                View root2 = inflater.inflate(R.layout.subject_open_dialog, null);
//                                TextView tx1 = root2.findViewById(R.id.subjectId);
//                                TextView tx2 = root2.findViewById(R.id.subjectName);
//                                TextView tx3 = root2.findViewById(R.id.subjectCabinet);
//                                tx1.setText("" + SubjectClck.get_id());
//                                tx2.setText(SubjectClck.getName());
//                                tx3.setText(SubjectClck.getCabinet());
//                                builder.setView(root2)
//                                        .setNegativeButton("Закрыть", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//
//                                            }
//                                        })
//                                        .setTitle("Информация о предмете");
//                                getActivity().runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        builder.show();
//                                    }
//                                });
//                            }
//                        }.start();
//                    }
//                });
                recyclerView.setAdapter(rVsubjectAdapter);
            }
            });

}
    public void onAddSubjectClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogTheme);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View root2 = inflater.inflate(R.layout.add_subject_dialog, null);
        EditText tx1 = root2.findViewById(R.id.addSubjectName);
        EditText tx2 = root2.findViewById(R.id.addSubjectCab);
        builder.setView(root2)
                .setTitle("Введите информацию о предмете")
                .setNeutralButton("Отмена", new DialogInterface.OnClickListener() {
                            @Override public void onClick(DialogInterface dialog, int which) {
                            }
                })
                .setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread() {
                            @Override
                            public void run() {
                                subjectDao.insert(new Subject(tx1.getText().toString(), tx2.getText().toString(), dayInd));
                                List<Subject> c = subjectDao.findByDay(dayInd);
                                setInUIThread(c);
                            }
                        }.start();
                    }
                }).show();
            }

}