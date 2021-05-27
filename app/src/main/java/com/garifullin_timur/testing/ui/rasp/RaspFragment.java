package com.garifullin_timur.testing.ui.rasp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.garifullin_timur.testing.Database.RaspDB;
import com.garifullin_timur.testing.Database.RaspDao;
import com.garifullin_timur.testing.Database.Raspisan;
import com.garifullin_timur.testing.Database.Subject;
import com.garifullin_timur.testing.Database.SubjectDao;
import com.garifullin_timur.testing.Database.SubjectsDB;
import com.garifullin_timur.testing.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.w3c.dom.Text;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;


public class RaspFragment extends Fragment {
    FloatingActionButton fab;
    View root, root2;
    RaspDB db;
    RaspDao raspDao;
    TabLayout tabLayout;
    RecyclerView recyclerView;
    RVraspAdapter rVraspAdapter;
    int dayInd = 0;
    Raspisan clicked;
    TextView openDialogCabinet;
    TextView openDialogSubjectName;
    TextView deleteSubjectName;
    EditText addSubjectName;
    EditText addSubjectCabinet;
    SearchableSpinner spinner;
    Subject selectedSubject;
    List<Subject> toSpinner;
    SubjectDao subjectDao;
    AlertDialog.Builder addSubjectBuilder, openSubjectBuilder, deleteSubjectBuilder;
    RVraspAdapter.OnItemClickListener mOnClickListener;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_rasp, container, false);
        db = RaspDB.create(getContext(), false);
        raspDao = db.raspDao();
        SubjectsDB dbs= SubjectsDB.create(getContext(), false);
        subjectDao = dbs.subjectDao();
        setDialogs();
        mOnClickListener = new RVraspAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                new Thread(){
                    @Override
                    public void run() {
                        clicked = rVraspAdapter.getItem(position);

                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        root2 = inflater.inflate(R.layout.add_rasp_dialog, null);
                        spinner = root2.findViewById(R.id.spinner);
                        toSpinner = subjectDao.selectAll();
                        spinner.setTitle("Выберите предмет");
                        List<String> names = new AbstractList<String>() {
                            @Override
                            public int size() { return toSpinner.size(); }

                            @Override
                            public String get(int location) {
                                return toSpinner.get(location).getName();
                            }
                        };
                        ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item);
//                        spinAdapter.add(choose);
                        spinAdapter.addAll(names);

                        spinner.setAdapter(spinAdapter);
                        selectedSubject = null;
                        Log.d("mytag", "yes");
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
                                openSubjectBuilder.setView(root2);
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
                List<Raspisan> c = raspDao.findByDay(dayInd);
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
                        List<Raspisan> c = raspDao.findByDay(dayInd);
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
    public void setInUIThread(List<Raspisan> c) {
        Log.d("mytag", "" + c.size());
        new Thread(){
            @Override
            public void run() {
                ArrayList<String> cNames = new ArrayList<>();
                for (Raspisan y:c) {
                    Log.d("mytag", "" + y.getSub_id());
                    if (y.getSub_id() == -1){
                        cNames.add("Предмет удален");
                    }
                    else {
                        Subject subject = subjectDao.findById(y.getSub_id());
                        cNames.add(subject.getName());
                    }
                }
                rVraspAdapter = new RVraspAdapter(c, mOnClickListener, cNames);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(rVraspAdapter);
                    }
                });
            }
        }.start();






    }
    public void onAddSubjectClick(View v) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        root2 = inflater.inflate(R.layout.add_rasp_dialog, null);
        spinner = root2.findViewById(R.id.spinner);
        new Thread(){
            @Override
            public void run() {
                toSpinner = subjectDao.selectAll();
                spinner.setTitle("Выберите предмет");
                List<String> names = new AbstractList<String>() {
                    @Override
                    public int size() { return toSpinner.size(); }

                    @Override
                    public String get(int location) {
                        return toSpinner.get(location).getName();
                    }
                };
                ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item);
//                        spinAdapter.add(choose);
                spinAdapter.addAll(names);

                spinner.setAdapter(spinAdapter);
                selectedSubject = null;
                Log.d("mytag", "yes");
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
                        addSubjectBuilder.setView(root2)
                                .show();
                    }
                });
            }
        }.start();


    }
    public void setDialogs(){
        addSubjectBuilder = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogTheme);
        addSubjectBuilder
                .setTitle("Добавить урок")
                .setNegativeButton("Отмена", null)
                .setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (selectedSubject != null) {
                            new Thread() {
                                @Override
                                public void run() {
                                    Raspisan newRasp = new Raspisan(dayInd, selectedSubject.get_id());
                                    raspDao.insert(newRasp);
                                    List<Raspisan> c = raspDao.findByDay(dayInd);
                                    setInUIThread(c);
                                }
                            }.start();
                        } else {
                            Toast.makeText(getContext(), "Пожалуйста, выберите предмет", Toast.LENGTH_LONG).show();
                        }
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
                                raspDao.delete(clicked);
                                List<Raspisan> c = raspDao.findByDay(dayInd);
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
                                new Thread(){
                                    @Override
                                    public void run() {
                                        LayoutInflater inflater = getActivity().getLayoutInflater();
                                        View root4 = inflater.inflate(R.layout.delete_dialog, null);
                                        deleteSubjectName = root4.findViewById(R.id.deleteSubjectName);
                                        TextView deleteInfo = root4.findViewById(R.id.del_info);
                                        String del_info = getResources().getString(R.string.del_subj);
                                        deleteInfo.setText(del_info);
                                        Subject subject = subjectDao.findById(clicked.getSub_id());
                                        deleteSubjectName.setText(subject.getName());
                                        deleteSubjectBuilder.setView(root4);
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                deleteSubjectBuilder.show();
                                            }
                                        });
                                    }
                                }.start();


                            }
                        }
                )
                .setNegativeButton("Закрыть", null)
                .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Raspisan newRaspisan = new Raspisan(clicked.get_id(),clicked.getDayIndex(), clicked.getSub_id());
                        if (selectedSubject != null){
                            Log.d("mytag", "ok" + selectedSubject.get_id());
                            newRaspisan.setSub_id(selectedSubject.get_id());
                        }
                        new Thread(){
                            @Override
                            public void run() {
                                raspDao.update(newRaspisan);
                                List<Raspisan> c = raspDao.findByDay(dayInd);
                                setInUIThread(c);
                            }
                        }.start();

                    }
                });


    }
}