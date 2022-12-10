package com.example.emg.employee.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.emg.R;
import com.example.emg.adapter.AttendanceAdapter;
import com.example.emg.base.EmployeeBase;
import com.example.emg.model.Attendance;
import com.example.emg.model.LeaveRequest;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.journeyapps.barcodescanner.ScanOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EmployeeAttendance extends EmployeeBase {
    private ArrayList<Attendance> attendances = new ArrayList<>();
    private RecyclerView recyclerView;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    DatabaseReference database;
    ActionBarDrawerToggle actionBarDrawerToggle;
    AttendanceAdapter attendanceAdapter;
    final Calendar calendar = Calendar.getInstance();
    final int year = calendar.get(Calendar.YEAR);
    final int month = calendar.get(Calendar.MONTH);
    final int day = calendar.get(Calendar.DAY_OF_MONTH);
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
        setContentView(R.layout.activity_employee_attendance);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        changeHeaderImage(navigationView);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.menu_open,R.string.menu_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        this.employeeNavigation(navigationView,drawerLayout,this);
        recyclerView = findViewById(R.id.attendanceRecycle);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        attendanceAdapter = new AttendanceAdapter(attendances);
        recyclerView.setAdapter(attendanceAdapter);
        getData();
    }
    private void getData(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("d-MM-yyyy", Locale.getDefault());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String formattedDate = df.format(c);
        database = FirebaseDatabase.getInstance().getReference("Attendance");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                attendances.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Iterable<DataSnapshot> attendance = dataSnapshot.getChildren();
                    for(DataSnapshot dataSnapshot1 : attendance){
                        if(dataSnapshot1.getValue(Attendance.class).id.equals(auth.getCurrentUser().getUid())){
                            attendances.add(dataSnapshot1.getValue(Attendance.class));
                        }
                    }
                }
                attendanceAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}