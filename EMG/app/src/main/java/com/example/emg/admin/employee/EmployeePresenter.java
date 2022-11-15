package com.example.emg.admin.employee;


import androidx.annotation.NonNull;

import com.example.emg.model.Employee;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EmployeePresenter {
    EmployeeView view;
    DatabaseReference database;
    ArrayList<Employee> list;
    public EmployeePresenter(EmployeeView view) {
        this.view = view;
    }
    ArrayList<Employee> populateRecycleView(){
    database = FirebaseDatabase.getInstance().getReference("Employees");
    database.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                Employee employee = dataSnapshot.getValue(Employee.class);
                list.add(employee);
            }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
    return list;
    }
}
