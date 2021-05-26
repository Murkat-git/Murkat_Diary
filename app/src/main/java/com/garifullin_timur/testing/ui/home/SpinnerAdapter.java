package com.garifullin_timur.testing.ui.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.garifullin_timur.testing.Database.Teacher;
import com.garifullin_timur.testing.R;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<Teacher>{

    private Context context;
    // Your custom values for the spinner (User)
    private List<Teacher> values;

    public SpinnerAdapter(Context context, int textViewResourceId,
                       List<Teacher> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount(){
        return values.size();
    }

    @Override
    public Teacher getItem(int position){
        return values.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }


    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.spinner_item, null, true);
        TextView textView = v.findViewById(R.id.textView7);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        textView.setText(values.get(position).getName());

        // And finally return your dynamic (or custom) view for each spinner item
        return textView;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.spinner_item, null, true);
        TextView textView = v.findViewById(R.id.textView7);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        textView.setText(values.get(position).getName());

        return textView;
    }

}
