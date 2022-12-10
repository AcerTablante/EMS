package com.example.emg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;

import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.emg.base.AdminBase;
import com.example.emg.model.Attendance;
import com.example.emg.model.Employee;
import com.example.emg.model.LeaveRequest;
import com.example.emg.utils.LoadingDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AdminBase implements View.OnClickListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    DatabaseReference database;
    Integer employee_count = 0;
    Integer pending_count = 0;
    Integer approve_count=0;
    Integer leave_count=0;
    Integer attendace_count=0;
    Integer sick_count = 0;
    Integer vacation_count = 0;
    Integer pending_sick_count = 0;
    Integer pending_vacation_count = 0;
    TextView emp_count,pending,approve,leaves;
    ImageView cancelButton;
    Button ok_btn;
    LinearLayout employee_box,approve_box,pending_box;
    View alertCustomDialog;
    AlertDialog dialog;
    TextView count1,count2,dialog_title,tv_tag1,tv_tag2;
    LoadingDialog loadingDialog;
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
        setContentView(R.layout.activity_main);
        loadingDialog = new LoadingDialog(this);
        loadingDialog.startLoadingDialog();
        alertCustomDialog = LayoutInflater.from(MainActivity.this).inflate(R.layout.custom_dialog,null);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setView(alertCustomDialog);
        dialog = alertDialog.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        initializeView();
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.menu_open,R.string.menu_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        this.adminNavigation(navigationView,drawerLayout,this);
        getEmployeeCount();
        getPending();
        getApprove();
        getLeaves();
        getAttendance();
        getPendingLeaves();
    }
    private void initializeView(){
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        emp_count = findViewById(R.id.emp_count);
        pending = findViewById(R.id.pendings);
        approve = findViewById(R.id.approve);
        leaves = findViewById(R.id.leaves);
        employee_box = findViewById(R.id.employee_box);
        employee_box.setOnClickListener(this);
        approve_box = findViewById(R.id.approve_box);
        approve_box.setOnClickListener(this);
        pending_box = findViewById(R.id.pending_box);
        pending_box.setOnClickListener(this);
        cancelButton = alertCustomDialog.findViewById(R.id.cancelID);
        cancelButton.setOnClickListener(this);
        ok_btn = alertCustomDialog.findViewById(R.id.ok_btn_id);
        ok_btn.setOnClickListener(this);
        count1 = alertCustomDialog.findViewById(R.id.count1);
        tv_tag1 = alertCustomDialog.findViewById(R.id.tv_tag1);
        tv_tag2 = alertCustomDialog.findViewById(R.id.tv_tag2);
        count2 = alertCustomDialog.findViewById(R.id.count2);
        dialog_title = alertCustomDialog.findViewById(R.id.dialog_title);
    }
    private void getAttendance(){
            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);
            SimpleDateFormat df = new SimpleDateFormat("d-MM-yyyy", Locale.getDefault());
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            String formattedDate = df.format(c);
            database = FirebaseDatabase.getInstance().getReference("Attendance").child(formattedDate);
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    attendace_count = 0;
                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        attendace_count++;
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        loadingDialog.dismissDialog();
    }
    private void getEmployeeCount(){
        database = FirebaseDatabase.getInstance().getReference("Employees");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                employee_count=0;
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                  employee_count++;
                }
                emp_count.setText(employee_count.toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void getPending(){
        database = FirebaseDatabase.getInstance().getReference("Leave");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            pending_count=0;
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Iterable<DataSnapshot> leaveRequest = dataSnapshot.getChildren();
                    for(DataSnapshot dataSnapshot1 : leaveRequest){
                        if(dataSnapshot1.getValue(LeaveRequest.class).status.equals("PENDING")){
                            pending_count++;
                        }
                    }
                }
                pending.setText(pending_count.toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }
    private void getApprove(){
        database = FirebaseDatabase.getInstance().getReference("Leave");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                approve_count=0;
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Iterable<DataSnapshot> leaveRequest = dataSnapshot.getChildren();
                    for(DataSnapshot dataSnapshot1 : leaveRequest){
                        if(dataSnapshot1.getValue(LeaveRequest.class).status.equals("APPROVE")){
                            approve_count++;
                        }
                    }
                }
                approve.setText(approve_count.toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }
    private void getLeaves(){
//        Leave_History
        database = FirebaseDatabase.getInstance().getReference("Leave_History");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                leave_count=0;
                sick_count=0;
                vacation_count=0;

                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    if(dataSnapshot.getValue(LeaveRequest.class).leave_type.equals("Sick Leave")&& dataSnapshot.getValue(LeaveRequest.class).status.equals("APPROVE")) sick_count++;
                    if(dataSnapshot.getValue(LeaveRequest.class).leave_type.equals("Vacation Leave")&& dataSnapshot.getValue(LeaveRequest.class).status.equals("APPROVE")) vacation_count++;

                    leave_count++;
                }
                leaves.setText(leave_count.toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void getPendingLeaves(){
        database = FirebaseDatabase.getInstance().getReference("Leave");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pending_sick_count=0;
                pending_vacation_count=0;
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Iterable<DataSnapshot> leaveRequest = dataSnapshot.getChildren();
                    for(DataSnapshot dataSnapshot1 : leaveRequest){
                        if(dataSnapshot1.getValue(LeaveRequest.class).status.equals("PENDING")){
                            if(dataSnapshot1.getValue(LeaveRequest.class).leave_type.equals("Sick Leave")) pending_sick_count++;
                            if(dataSnapshot1.getValue(LeaveRequest.class).leave_type.equals("Vacation Leave")) pending_vacation_count++;
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.employee_box:
                dialog_title.setText("Employees");
                tv_tag1.setText("Present");
                tv_tag2.setText("Absent");
                count1.setText(attendace_count.toString());
//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
//                alertDialog.setView(alertCustomDialog);
//                dialog = alertDialog.create();
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                count1.setText(attendace_count);
                Integer total = employee_count-attendace_count;
                count2.setText(total.toString());
                dialog.show();
                break;
            case R.id.approve_box:
                dialog_title.setText("Approved");
                tv_tag1.setText("Sick Leave");
                tv_tag2.setText("Vacation Leave");
                count1.setText(sick_count.toString());
                count2.setText(vacation_count.toString());
                dialog.show();
                break;
            case R.id.pending_box:
                dialog_title.setText("Pending");
                tv_tag1.setText("Sick Leave");
                tv_tag2.setText("Vacation Leave");
                count1.setText(pending_sick_count.toString());
                count2.setText(pending_vacation_count.toString());
                dialog.show();
                break;
            case R.id.cancelID:
                dialog.cancel();
                break;
            case R.id.ok_btn_id:
                dialog.cancel();
                break;

        }
    }
}