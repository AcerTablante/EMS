package com.example.emg.admin.view_attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.example.emg.R;
import com.example.emg.base.AdminBase;
import com.example.emg.model.Attendance;
import com.example.emg.adapter.AttendanceAdapter;
import com.google.android.material.navigation.NavigationView;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;

public class AttendanceActivity extends AdminBase {
    private ArrayList<Attendance> attendances;
    private RecyclerView recyclerView;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ScanOptions options = new ScanOptions();
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
        setContentView(R.layout.activity_attendance);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.menu_open,R.string.menu_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        this.adminNavigation(navigationView,drawerLayout,this);
        attendances = new ArrayList<>();
        attendances.add(new Attendance("John Cena"));
        attendances.add(new Attendance("Juan Dela Cruz"));
        recyclerView = findViewById(R.id.attendanceRecycle);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        AttendanceAdapter attendanceAdapter = new AttendanceAdapter(attendances);
        recyclerView.setAdapter(attendanceAdapter);
    }
}