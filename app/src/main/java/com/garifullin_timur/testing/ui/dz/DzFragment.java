package com.garifullin_timur.testing.ui.dz;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.garifullin_timur.testing.Database.HomeWork;
import com.garifullin_timur.testing.Database.HomeWorkDao;
import com.garifullin_timur.testing.Database.HomeworkDB;
import com.garifullin_timur.testing.Database.Subject;
import com.garifullin_timur.testing.Database.SubjectDao;
import com.garifullin_timur.testing.Database.SubjectsDB;
import com.garifullin_timur.testing.Database.Teacher;
import com.garifullin_timur.testing.R;
import com.garifullin_timur.testing.ui.home.RVsubjectAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.toptoche.searchablespinnerlibrary.SearchableListDialog;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class DzFragment extends Fragment {

    FloatingActionButton fab;
    View root;
    HomeworkDB db;
    SubjectsDB dbs;
    HomeWorkDao homeWorkDao;
    SubjectDao subjectDao;
    TabLayout tabLayout;
    RecyclerView recyclerView;
    RVhomeworkAdapter rVhomeworkAdapter;
    int dayInd = 0;
    Subject selectedSubject;
    RVhomeworkAdapter.OnItemClickListener mOnClickListener;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_dz, container, false);
        setHasOptionsMenu(true);
        fab = getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(this::onAddSubjectClick);
        db = HomeworkDB.create(getContext(), false);
        homeWorkDao = db.homeWorkDao();
        dbs = SubjectsDB.create(getContext(), false);
        subjectDao = dbs.subjectDao();
        recyclerView = root.findViewById(R.id.homerv);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);
        new Thread(){
            @Override
            public void run() {
                List<HomeWork> c = homeWorkDao.findByDay(dayInd);
                setInUIThread(c);
            }
        }.start();
        mOnClickListener = new RVhomeworkAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                new Thread(){
                    @Override
                    public void run() {
                        HomeWork clicked = rVhomeworkAdapter.getItem(position);
                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        View root2 = inflater.inflate(R.layout.homework_open_dialog, null);
                        SearchableSpinner spinner = root2.findViewById(R.id.spinner);
                        List<Subject> toSpinner = subjectDao.selectAll();
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
                        spinner.setAdapter(spinAdapter);
                        spinner.setTitle("Выберите предмет");
                        selectedSubject = null;
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                selectedSubject = toSpinner.get(position);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        EditText tx2 = root2.findViewById(R.id.opendzDz);
                        tx2.setText(clicked.getDz());
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogTheme);
                        builder.setTitle("Информация о Дз")
                                .setView(root2)
                                .setNeutralButton("Удалить", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        AlertDialog.Builder deleteDZ = new AlertDialog.Builder(getContext(), R.style.MyAlertDialogTheme)
                                                .setNegativeButton("Нет", null)
                                                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        new Thread(){
                                                            @Override
                                                            public void run() {
                                                                homeWorkDao.delete(clicked);
                                                                List<HomeWork> c = homeWorkDao.findByDay(dayInd);
                                                                setInUIThread(c);
                                                            }
                                                        }.start();
                                                    }
                                                });
                                        View root4 = inflater.inflate(R.layout.delete_dialog, null);
                                        TextView delete_info = root4.findViewById(R.id.del_info);
                                        TextView delete_name = root4.findViewById(R.id.deleteSubjectName);
                                        String del_info = getResources().getString(R.string.del_dz);
                                        delete_info.setText(del_info);
                                        delete_name.setText(subjectDao.findById(clicked.getSub_id()).getName());
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                deleteDZ.setView(root4);
                                                deleteDZ.show();
                                            }
                                        });

                                    }
                                })
                                .setNegativeButton("Закрыть", null)
                                .setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        HomeWork newHomework;
                                        if (selectedSubject != null) {
                                            newHomework = new HomeWork(clicked.get_id(), selectedSubject.get_id(), tx2.getText().toString(), clicked.getDayInd(), clicked.isDone());
                                        }
                                        else {
                                            newHomework = new HomeWork(clicked.get_id(), clicked.getSub_id(), tx2.getText().toString(), clicked.getDayInd(), clicked.isDone());
                                        }
                                        new Thread(){
                                            @Override
                                            public void run() {
                                                homeWorkDao.update(newHomework);
                                                List<HomeWork> c = homeWorkDao.findByDay(dayInd);
                                                setInUIThread(c);
                                            }
                                        }.start();
                                    }
                                });
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                builder.show();
                            }
                        });

                    }
                }.start();

            }

            @Override
            public void onLongClick(int position) {
                HomeWork longClicked = rVhomeworkAdapter.getItem(position);
                new Thread(){
                    @Override
                    public void run() {
                        homeWorkDao.update(new HomeWork(longClicked.get_id(), longClicked.getSub_id(), longClicked.getDz(), dayInd,!longClicked.isDone()));
                        List<HomeWork> c = homeWorkDao.findByDay(dayInd);
                        setInUIThread(c);
                    }
                }.start();

            }
        };
        tabLayout = root.findViewById(R.id.tabLHomeWorks);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                dayInd = tab.getPosition();
                new Thread(){
                    @Override
                    public void run() {
                        List<HomeWork> c = homeWorkDao.findByDay(dayInd);
                        setInUIThread(c);
                    }
                }.start();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
        return root;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d("mytag", "selected");
        if (item.getItemId() == R.id.copy){
            new Thread(){
                @Override
                public void run() {
                    Log.d("mytag", "oke");
                    List<HomeWork> c = homeWorkDao.findByDay(dayInd);
                    String genned = "";
                    for (HomeWork y: c) {
                        if (y.getSub_id() == -1){
                            genned += "Предмет удален";
                        }
                        else {
                            Subject subject = subjectDao.findById(y.getSub_id());
                            genned += subject.getName();
                        }
                        genned += ": ";
                        genned += y.getDz() + "\n";

                    }
                    ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Murkat copied homework", genned);
                    clipboard.setPrimaryClip(clip);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Домашнее задание скопировано", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }.start();
        }
        else if (item.getItemId() == R.id.delete){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogTheme);
            builder.setTitle("Вы уверены?")
                    .setNeutralButton("Нет", null)
                    .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new Thread(){
                                @Override
                                public void run() {
                                    homeWorkDao.deleteAllByDay(dayInd);
                                    setInUIThread(new ArrayList<HomeWork>());
                                }
                            }.start();
                        }
                    })
                    .show();

        }
        else return super.onOptionsItemSelected(item);
        return true;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    public void setInUIThread(List<HomeWork> c) {

        Log.d("mytag", "" + c.size());
        new Thread(){
            @Override
            public void run() {
                ArrayList<String> cNames = new ArrayList();
                for (HomeWork y:c) {
                    if (y.getSub_id() == -1){
                        cNames.add("Предмет удален");
                    }
                    else {
                        Subject subject = subjectDao.findById(y.getSub_id());
                        cNames.add(subject.getName());
                    }

                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rVhomeworkAdapter = new RVhomeworkAdapter(getContext(), c, mOnClickListener, cNames);
                        recyclerView.setAdapter(rVhomeworkAdapter);
                    }
                });

            }
        }.start();
    }

    public void onAddSubjectClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogTheme);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View root2 = inflater.inflate(R.layout.add_homework_dialog, null);
        SearchableSpinner spinner = root2.findViewById(R.id.spinner);
        new Thread(){
            @Override
            public void run() {
                List<Subject> toSpinner = subjectDao.selectAll();
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
                spinner.setAdapter(spinAdapter);
                spinner.setTitle("Выберите предмет");
                selectedSubject = null;
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedSubject = toSpinner.get(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        EditText tx2 = root2.findViewById(R.id.addDzDZ);
                        View root3 = inflater.inflate(R.layout.open_dialog_title, null);
                        builder.setView(root2)
                                .setTitle("Введите домашнее задание")
                                .setNeutralButton("Отмена", null)
                                .setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        new Thread() {
                                            @Override
                                            public void run() {
                                                if (selectedSubject != null){
                                                    homeWorkDao.insert(new HomeWork(selectedSubject.get_id(), tx2.getText().toString(), dayInd));
                                                    List<HomeWork> c = homeWorkDao.findByDay(dayInd);
                                                    setInUIThread(c);
                                                }
                                                else {
                                                    getActivity().runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(getContext(), "Пожалуйста, выберите предмет", Toast.LENGTH_LONG).show();
                                                        }
                                                    });

                                                }
                                            }
                                        }.start();
                                    }
                                }).show();
                    }
                });
            }
        }.start();


    }


}