package com.example.emg.admin.leave;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.example.emg.R;
import com.example.emg.base.AdminBase;
import com.example.emg.model.LeaveRequest;
import com.example.emg.adapter.LeaveAdapter;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class Leave extends AdminBase {
    private ArrayList<LeaveRequest> leaveRequest;
    private RecyclerView recyclerView;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
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
        setContentView(R.layout.activity_leave);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.menu_open,R.string.menu_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        this.adminNavigation(navigationView,drawerLayout,this);
        leaveRequest = new ArrayList<>();
        leaveRequest.add(new LeaveRequest("Juan Dela Cruz","Admin","December 25, 2022","Feeling Sick"));
        leaveRequest.add(new LeaveRequest("John Cena","Employee","December 25, 2022","Feeling Sick"));
        recyclerView = findViewById(R.id.leaveRecycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        LeaveAdapter leaveAdapter = new LeaveAdapter(leaveRequest);
        recyclerView.setAdapter(leaveAdapter);
    }

}