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
import com.example.emg.model.LeaveRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class LeaveFragment extends Fragment {
    private ArrayList<LeaveRequest> list = new ArrayList<>();
    private RecyclerView recyclerView;
    DatabaseReference database;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_leave, container, false);
        recyclerView = view.findViewById(R.id.leaveRecycler);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        LeaveAdapter leaveAdapter = new LeaveAdapter(list);
        recyclerView.setAdapter(leaveAdapter);
        database = FirebaseDatabase.getInstance().getReference("Leave");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Iterable<DataSnapshot> leaveRequest = dataSnapshot.getChildren();
                    for(DataSnapshot dataSnapshot1 : leaveRequest){
                        if(dataSnapshot1.getValue(LeaveRequest.class).status.equals("PENDING")){
                            list.add(dataSnapshot1.getValue(LeaveRequest.class));
                        }
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