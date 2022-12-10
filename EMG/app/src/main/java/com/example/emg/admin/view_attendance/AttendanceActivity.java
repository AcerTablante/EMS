package com.example.emg.admin.view_attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.emg.R;
import com.example.emg.admin.attendance.CreateAttendanceActivity;
import com.example.emg.base.AdminBase;
import com.example.emg.model.Attendance;
import com.example.emg.adapter.AttendanceAdapter;
import com.example.emg.model.Employee;
import com.example.emg.utils.ExcelUtils;
import com.google.android.material.navigation.NavigationView;
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

public class AttendanceActivity extends AdminBase implements View.OnClickListener {
    private ArrayList<Attendance> attendances = new ArrayList<>();
    private RecyclerView recyclerView;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    DatabaseReference database;
    ExcelUtils excelUtils = new ExcelUtils();
    EditText date;
    ScanOptions options = new ScanOptions();
    ActionBarDrawerToggle actionBarDrawerToggle;
    AttendanceAdapter attendanceAdapter;
    final Calendar calendar = Calendar.getInstance();
    final int year = calendar.get(Calendar.YEAR);
    final int month = calendar.get(Calendar.MONTH);
    final int day = calendar.get(Calendar.DAY_OF_MONTH);
    Button export;
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
        date = findViewById(R.id.date);
        date.setOnClickListener(this);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.menu_open,R.string.menu_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        this.adminNavigation(navigationView,drawerLayout,this);
        recyclerView = findViewById(R.id.attendanceRecycle);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        export = findViewById(R.id.export);
        export.setOnClickListener(this);
        attendanceAdapter = new AttendanceAdapter(attendances);
        recyclerView.setAdapter(attendanceAdapter);
        getData();
    }
    private void getData(){
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("d-MM-yyyy", Locale.getDefault());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String formattedDate = df.format(c);
        date.setText(formattedDate);
        database = FirebaseDatabase.getInstance().getReference("Attendance").child(formattedDate);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                attendances.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Attendance attendance = dataSnapshot.getValue(Attendance.class);
                    attendances.add(attendance);
                }
               attendanceAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void datePicker(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(AttendanceActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int years, int months, int days) {
                months = months+1;
                String current_date = months+"/"+days+"/"+years;
                String format_date = days+"-"+months+"-"+years;
                date.setText(current_date);
                database = FirebaseDatabase.getInstance().getReference("Attendance").child(format_date);
                database.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        attendances.clear();
                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                            Attendance attendance = dataSnapshot.getValue(Attendance.class);
                            attendances.add(attendance);
                        }
                        attendanceAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        },year,month,day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.date:
                datePicker();
                break;
            case R.id.export:
                Boolean isExcelGenerated = ExcelUtils.exportDataIntoWorkbook(this,"EMP_"+System.currentTimeMillis(),attendances);
                break;

        }
    }
}