package com.example.emg.adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emg.R;
import com.example.emg.model.LeaveRequest;

import java.util.ArrayList;

public class LeaveStatusAdapter extends RecyclerView.Adapter<LeaveStatusAdapter.MyViewHolder>  {
    ArrayList<LeaveRequest> leaveRequests;
    private Context leaveContext;

    public LeaveStatusAdapter(ArrayList<LeaveRequest> leaveRequests, Context leaveContext) {
        this.leaveRequests = leaveRequests;
        this.leaveContext = leaveContext;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView tv_position;
        private TextView tv_date_begin;
        private TextView tv_date_end;
        private TextView tv_type;
        private RelativeLayout status_color;
        private TextView status;
        public MyViewHolder(final View view){
            super(view);
            name = view.findViewById(R.id.tv_name);
            tv_type = view.findViewById(R.id.tv_type);
            tv_position = view.findViewById(R.id.tv_position);
            tv_date_begin = view.findViewById(R.id.tv_date_begin);
            tv_date_end = view.findViewById(R.id.tv_date_end);
            status = view.findViewById(R.id.status);
            status_color = view.findViewById(R.id.status_color);
        }
    }
    @NonNull
    @Override
    public LeaveStatusAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.leave_status_item,parent,false);
        return new LeaveStatusAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaveStatusAdapter.MyViewHolder holder, int position) {
    holder.name.setText(leaveRequests.get(position).name);
    holder.tv_position.setText(leaveRequests.get(position).position);
    holder.tv_type.setText(leaveRequests.get(position).leave_type);
    holder.tv_date_begin.setText(leaveRequests.get(position).leave_date_from);
    holder.tv_date_end.setText(leaveRequests.get(position).leave_date_to);
    if(leaveRequests.get(position).status.equals("APPROVE")){
        holder.status_color.setBackgroundColor(Color.GREEN);
        holder.status.setText("APPROVE");
    }
    if(leaveRequests.get(position).status.equals("DECLINE")){
        holder.status_color.setBackgroundColor(Color.RED);
        holder.status.setText("DECLINE");
    }
    if(leaveRequests.get(position).status.equals("PENDING")){
        holder.status_color.setBackgroundColor(Color.YELLOW);
        holder.status.setText("PENDING");
    }
    }

    @Override
    public int getItemCount() {
        return leaveRequests.size();
    }
}
