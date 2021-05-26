package com.garifullin_timur.testing.ui.rasp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.garifullin_timur.testing.Database.Raspisan;
import com.garifullin_timur.testing.Database.Subject;
import com.garifullin_timur.testing.Database.SubjectDao;
import com.garifullin_timur.testing.Database.SubjectsDB;
import com.garifullin_timur.testing.R;
import com.garifullin_timur.testing.ui.home.RVsubjectAdapter;

import java.util.List;

public class RVraspAdapter extends  RecyclerView.Adapter<RVraspAdapter.RaspViewHolder> {
    private RVraspAdapter.OnItemClickListener mListener;
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public static class RaspViewHolder extends RecyclerView.ViewHolder{
        TextView tx1, tx2;

        public RaspViewHolder(View itemView, RVraspAdapter.OnItemClickListener listener) {
            super(itemView);
            tx1 = itemView.findViewById(R.id.itemName);
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

    public RVraspAdapter.RaspViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.raps_item, parent, false);
        RVraspAdapter.RaspViewHolder svh = new RVraspAdapter.RaspViewHolder(v, mListener);
        return svh;
    }

    @Override
    public void onBindViewHolder(@NonNull RVraspAdapter.RaspViewHolder holder, int position) {

        String subjectName = timetableNames.get(position);
        holder.tx1.setText(subjectName);


    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    public Raspisan getItem(int position) {
        return timetable.get(position);
    }

    @Override
    public int getItemCount() {
        return timetable.size();
    }
    List<Raspisan> timetable;
    List<String> timetableNames;
    public RVraspAdapter(List<Raspisan> timetable, RVraspAdapter.OnItemClickListener listener, List<String> timetableNames) {
        this.timetable = timetable;
        mListener = listener;
        this.timetableNames = timetableNames;
    }
}
