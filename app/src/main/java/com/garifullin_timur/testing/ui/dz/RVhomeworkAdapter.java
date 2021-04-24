package com.garifullin_timur.testing.ui.dz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.garifullin_timur.testing.Database.HomeWork;
import com.garifullin_timur.testing.Database.Subject;
import com.garifullin_timur.testing.R;

import java.util.List;

public class RVhomeworkAdapter extends RecyclerView.Adapter<RVhomeworkAdapter.HomeViewHolder> {
    private RVhomeworkAdapter.OnItemClickListener mListener;
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public static class HomeViewHolder extends RecyclerView.ViewHolder{
        TextView tx1, tx2;
        ImageView img;

        public HomeViewHolder(View itemView, RVhomeworkAdapter.OnItemClickListener listener) {
            super(itemView);
            tx1 = itemView.findViewById(R.id.dzSubjectName);
            tx2 = itemView.findViewById(R.id.dz);
//            img = itemView.findViewById(R.id.imageView3);
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
    public RVhomeworkAdapter.HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.homework_item, parent, false);
        RVhomeworkAdapter.HomeViewHolder svh = new RVhomeworkAdapter.HomeViewHolder(v, mListener);
        return svh;
    }

    @Override
    public void onBindViewHolder(@NonNull RVhomeworkAdapter.HomeViewHolder holder, int position) {
        holder.tx1.setText(homeworks.get(position).getSubjectName());
        holder.tx2.setText(homeworks.get(position).getDz());
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    public HomeWork getItem(int position) {
        return homeworks.get(position);
    }

    @Override
    public int getItemCount() {
        return homeworks.size();
    }
    List<HomeWork> homeworks;
    public RVhomeworkAdapter(List<HomeWork> subjects, RVhomeworkAdapter.OnItemClickListener listener) {
        this.homeworks = subjects;
        mListener = listener;
    }
}