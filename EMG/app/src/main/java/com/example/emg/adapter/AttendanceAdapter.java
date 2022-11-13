package com.example.emg.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emg.model.Attendance;
import com.example.emg.R;

import java.util.ArrayList;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.MyViewHolder> {
    private ArrayList<Attendance> attendancesList;

    public AttendanceAdapter(ArrayList<Attendance> attendancesList) {
        this.attendancesList = attendancesList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        public MyViewHolder(final View view){
            super(view);
            name = view.findViewById(R.id.fullname);
        }
    }
    @NonNull
    @Override
    public AttendanceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_item,parent,false);
        return new AttendanceAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceAdapter.MyViewHolder holder, int position) {
        String name = attendancesList.get(position).getName();
        holder.name.setText(name);
    }

    @Override
    public int getItemCount() {
        return attendancesList.size();
    }
}
