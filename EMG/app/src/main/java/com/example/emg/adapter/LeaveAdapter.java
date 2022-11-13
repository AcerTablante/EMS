package com.example.emg.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emg.model.LeaveRequest;
import com.example.emg.R;

import java.util.ArrayList;

public class LeaveAdapter extends RecyclerView.Adapter<LeaveAdapter.MyViewHolder> {
    private ArrayList<LeaveRequest> leaveList;
    public LeaveAdapter(ArrayList<LeaveRequest> leaveList) {
        this.leaveList = leaveList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView position;
        private TextView leave_date;
        private TextView reason;
        public MyViewHolder(final View view){
            super(view);
            name = view.findViewById(R.id.tv_name);
            position = view.findViewById(R.id.tv_position);
            leave_date = view.findViewById(R.id.tv_leave);
            reason = view.findViewById(R.id.tv_reason);
        }
    }

    @NonNull
    @Override
    public LeaveAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.leave_item,parent,false);
        return new LeaveAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaveAdapter.MyViewHolder holder, int position) {
        String name = leaveList.get(position).getName();
        String emp_position = leaveList.get(position).getPosition();
        String date = leaveList.get(position).getLeave_date();
        String reason = leaveList.get(position).getReason();
        holder.name.setText(name);
        holder.position.setText(emp_position);
        holder.leave_date.setText(date);
        holder.reason.setText(reason);
    }
    @Override
    public int getItemCount() {
        return leaveList.size();
    }
}
