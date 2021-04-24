package com.garifullin_timur.testing.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.garifullin_timur.testing.Database.Subject;
import com.garifullin_timur.testing.R;

import java.util.List;

public class RVsubjectAdapter extends RecyclerView.Adapter<RVsubjectAdapter.SubjectViewHolder> {
    private OnItemClickListener mListener;
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public static class SubjectViewHolder extends RecyclerView.ViewHolder{
        TextView tx1, tx2;
        ImageView img;

        public SubjectViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            tx1 = itemView.findViewById(R.id.itemName);
            tx2 = itemView.findViewById(R.id.itemCabinet);
            img = itemView.findViewById(R.id.imageView3);
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
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_list_item, parent, false);
        SubjectViewHolder svh = new SubjectViewHolder(v, mListener);
        return svh;
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        holder.tx1.setText(subjects.get(position).getName());
        holder.tx2.setText(subjects.get(position).getCabinet());
        holder.img.setImageResource(R.drawable.ic__41_sticky_notes);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    public Subject getItem(int position) {
        return subjects.get(position);
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }
    List<Subject> subjects;
    public RVsubjectAdapter(List<Subject> subjects, OnItemClickListener listener) {
        this.subjects = subjects;
        mListener = listener;
    }
}
