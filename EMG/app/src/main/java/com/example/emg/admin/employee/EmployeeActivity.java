package com.example.emg.admin.employee;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emg.R;
import com.example.emg.adapter.EmployeeAdapter;
import com.example.emg.admin.add_employee.AddEmployeeActivity;
import com.example.emg.base.AdminBase;
import com.example.emg.model.Employee;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EmployeeActivity extends AdminBase implements EmployeeView, View.OnClickListener {
    private ArrayList<Employee> employee;
    private RecyclerView recyclerView;
    Button btn_add_employee;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    EmployeePresenter presenter;
    DatabaseReference database;
    ArrayList<Employee> list = new ArrayList<>();
    ActionBarDrawerToggle actionBarDrawerToggle;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employees);
        presenter = new EmployeePresenter(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initializeView();
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.menu_open,R.string.menu_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setTitle("");
        this.adminNavigation(navigationView,drawerLayout,this);
        recyclerView = findViewById(R.id.employeeRecycle);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //dummy data
        EmployeeAdapter employeeAdapter = new EmployeeAdapter(this,list);
        recyclerView.setAdapter(employeeAdapter);
        database = FirebaseDatabase.getInstance().getReference("Employees");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Employee employee = dataSnapshot.getValue(Employee.class);
                    list.add(employee);
                }
                employeeAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initializeView(){
        btn_add_employee = findViewById(R.id.btn_add_employee);
        btn_add_employee.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_add_employee:
                startActivity(new Intent(EmployeeActivity.this, AddEmployeeActivity.class));
                break;
        }

    }
}