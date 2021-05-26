package com.garifullin_timur.testing.ui.teacher;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.garifullin_timur.testing.Database.Subject;
import com.garifullin_timur.testing.Database.Teacher;
import com.garifullin_timur.testing.R;
import com.garifullin_timur.testing.ui.home.RVsubjectAdapter;

import org.w3c.dom.ls.LSInput;

import java.util.List;

public class RVteacherAdapter extends RecyclerView.Adapter<RVteacherAdapter.TeacherViewHolder>{
    private RVteacherAdapter.OnItemClickListener mListener;
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public static class TeacherViewHolder extends RecyclerView.ViewHolder{
        TextView tx1, tx2;

        public TeacherViewHolder(View itemView, RVteacherAdapter.OnItemClickListener listener) {
            super(itemView);
            tx1 = itemView.findViewById(R.id.teacher_name);
            tx2 = itemView.findViewById(R.id.teacher_subjects);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }


    }
    @NonNull
    @Override
    public RVteacherAdapter.TeacherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.teacher_list_item, parent, false);
        RVteacherAdapter.TeacherViewHolder tvh = new RVteacherAdapter.TeacherViewHolder(v, mListener);
        return tvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RVteacherAdapter.TeacherViewHolder holder, int position) {
        holder.tx1.setText(teachers.get(position).getName());
        holder.tx2.setText(subjects.get(position));
    }
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public Teacher getItem(int position) {
        return teachers.get(position);
    }

    public String getSubjects(int pos){return subjects.get(pos);}

    @Override
    public int getItemCount() {
        return teachers.size();
    }
    List<Teacher> teachers;
    List<String> subjects;
    public RVteacherAdapter(List<Teacher> teachers,List<String> subjects ,RVteacherAdapter.OnItemClickListener listener) {
        this.teachers = teachers;
        mListener = listener;
        this.subjects = subjects;
    }
}
