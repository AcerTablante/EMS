package com.example.emg.adapter;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emg.employee.dashboard.EmployeeDashboard;
import com.example.emg.login.Login;
import com.example.emg.model.Attendance;
import com.example.emg.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.MyViewHolder> {
    private ArrayList<Attendance> attendancesList;

    public AttendanceAdapter(ArrayList<Attendance> attendancesList) {
        this.attendancesList = attendancesList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView time_in;
        private TextView time_out;
        private TextView tv_present;
        public MyViewHolder(final View view){
            super(view);
            name = view.findViewById(R.id.fullname);
            time_in = view.findViewById(R.id.tv_time_in);
            time_out = view.findViewById(R.id.tv_time_out);
            tv_present = view.findViewById(R.id.tv_present);
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
        String time = attendancesList.get(position).time;
        String time2 = attendancesList.get(position).time_out;
        String date = attendancesList.get(position).date;
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
           holder.tv_present.setText(date);
        }
        holder.name.setText(name);
        holder.time_in.setText(time);
        holder.time_out.setText(time2);
    }

    @Override
    public int getItemCount() {
        return attendancesList.size();
    }
}
