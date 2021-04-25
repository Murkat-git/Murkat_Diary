package com.garifullin_timur.testing.ui.dz;

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
import com.garifullin_timur.testing.R;
import com.garifullin_timur.testing.ui.home.RVsubjectAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.List;
import java.util.zip.Inflater;

public class DzFragment extends Fragment {

    FloatingActionButton fab;
    View root;
    HomeworkDB db;
    ListView listView;
    HomeWorkDao homeWorkDao;
    TabLayout tabLayout;
    RecyclerView recyclerView;
    RVhomeworkAdapter rVhomeworkAdapter;
    int dayInd = 0;
    RVhomeworkAdapter.OnItemClickListener mOnClickListener;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_dz, container, false);

        fab = getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(this::onAddSubjectClick);
        db = HomeworkDB.create(getContext(), false);
        homeWorkDao = db.homeWorkDao();

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

                HomeWork clicked = rVhomeworkAdapter.getItem(position);
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View root2 = inflater.inflate(R.layout.homework_open_dialog, null);
                EditText tx1 = root2.findViewById(R.id.opendzName);
                EditText tx2 = root2.findViewById(R.id.opendzDz);
                tx1.setText(clicked.getSubjectName());
                tx2.setText(clicked.getDz());
                View root3 = inflater.inflate(R.layout.open_dialog_title, null);
                TextView dialogTitle = root3.findViewById(R.id.openInfo);
                dialogTitle.setText("Информация о дз");
                ImageView trash = root3.findViewById(R.id.trash);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogTheme);
                builder.setCustomTitle(root3)
                        .setView(root2)
                        .setNeutralButton("Закрыть", null)
                        .setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                HomeWork newHomework = new HomeWork(clicked.get_id(), tx1.getText().toString(), tx2.getText().toString(), clicked.getDayInd(), clicked.isDone());
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
                AlertDialog dialog = builder.create();
                new Thread(){
                    @Override
                    public void run() {

                        trash.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
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
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.show();
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
                        homeWorkDao.update(new HomeWork(longClicked.get_id(), longClicked.getSubjectName(), longClicked.getDz(), dayInd,!longClicked.isDone()));
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
    public void setInUIThread(List<HomeWork> c) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("mytag", "" + c.size());
                rVhomeworkAdapter = new RVhomeworkAdapter(getContext(), c, mOnClickListener);
                recyclerView.setAdapter(rVhomeworkAdapter);
            }
        });

    }

    public void onAddSubjectClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogTheme);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View root2 = inflater.inflate(R.layout.add_homework_dialog, null);
        EditText tx1 = root2.findViewById(R.id.addDzSubjectName);
        EditText tx2 = root2.findViewById(R.id.addDzDZ);
        View root3 = inflater.inflate(R.layout.open_dialog_title, null);
        TextView txTitle = root3.findViewById(R.id.openInfo);
        ImageView trash = root3.findViewById(R.id.trash);
        builder.setView(root2)
                .setTitle("Введите домашнее задание")
                .setNeutralButton("Отмена", null)
                .setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread() {
                            @Override
                            public void run() {
                                homeWorkDao.insert(new HomeWork(tx1.getText().toString(), tx2.getText().toString(), dayInd));
                                List<HomeWork> c = homeWorkDao.findByDay(dayInd);
                                setInUIThread(c);
                            }
                        }.start();
                    }
                }).show();

    }

}