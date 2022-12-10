package com.example.emg.admin.leave;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.emg.R;
import com.example.emg.adapter.LeaveAdapter;
import com.example.emg.adapter.LeaveStatusAdapter;
import com.example.emg.employee.leave_status.LeaveStatus;
import com.example.emg.model.LeaveRequest;
import com.example.emg.model.News;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class HistoryFragment extends Fragment {
    private ArrayList<LeaveRequest> list = new ArrayList<>();
    RecyclerView recyclerView;
    DatabaseReference database;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_history, container, false);
        // Inflate the layout for this fragment
        recyclerView = view.findViewById(R.id.leaveHistoryRecycler);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        LeaveStatusAdapter leaveAdapter = new LeaveStatusAdapter(list,getActivity());
        recyclerView.setAdapter(leaveAdapter);
        database = FirebaseDatabase.getInstance().getReference("Leave_History");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    LeaveRequest leaveRequest = dataSnapshot.getValue(LeaveRequest.class);
                    if(!leaveRequest.status.equals("PENDING")){
                        list.add(leaveRequest);
                    }
                }
               leaveAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}