package com.example.emg.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emg.model.LeaveRequest;
import com.example.emg.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        private TextView leave_date_to;
        private TextView reason;
        private Button btnApprove,btnDecline;
        public MyViewHolder(final View view){
            super(view);
            name = view.findViewById(R.id.tv_name);
            position = view.findViewById(R.id.tv_position);
            leave_date = view.findViewById(R.id.tv_leave_from);
            reason = view.findViewById(R.id.tv_reason);
            leave_date_to = view.findViewById(R.id.tv_to);
            btnApprove = view.findViewById(R.id.btnApprove);
            btnDecline = view.findViewById(R.id.btnDecline);

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
        String name = leaveList.get(position).name;
        String emp_position = leaveList.get(position).position;
        String date = leaveList.get(position).leave_date_from;
        String date_to = leaveList.get(position).leave_date_to;
        String type = leaveList.get(position).leave_type;
        holder.name.setText(name);
        holder.position.setText(emp_position);
        holder.leave_date.setText(date);
        holder.reason.setText(type);
        holder.leave_date_to.setText(date_to);


        holder.btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LeaveRequest leaveRequest_final = leaveList.get(position);
                leaveRequest_final.setStatus("APPROVE");
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Leave_History");
                myRef.child(leaveList.get(position).leave_id).setValue(leaveRequest_final).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DatabaseReference myRef = database.getReference("Leave").child(leaveList.get(position).user_id);
                        myRef.child(leaveList.get(position).leave_id).setValue(leaveRequest_final).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });
        holder.btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LeaveRequest leaveRequest_final = leaveList.get(position);
                leaveRequest_final.setStatus("DECLINE");
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Leave_History");
                myRef.child(leaveList.get(position).leave_id).setValue(leaveRequest_final).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DatabaseReference myRef = database.getReference("Leave").child(leaveList.get(position).user_id);
                        myRef.child(leaveList.get(position).leave_id).setValue(leaveRequest_final).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

            }
        });
    }
    @Override
    public int getItemCount() {
        return leaveList.size();
    }
}
