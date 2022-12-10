package com.example.emg.employee.leave_request;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.emg.R;
import com.example.emg.admin.create_news.CreateNewsActivity;
import com.example.emg.base.EmployeeBase;


import com.example.emg.employee.leave_status.LeaveStatus;
import com.example.emg.model.LeaveRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.UUID;

public class LeaveRequestActivity extends EmployeeBase implements View.OnClickListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    TextInputEditText leave_type,leave_reason;
    EditText leave_from,leave_to;
    Button btn_leave;
    String name,position,id;
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
        setContentView(R.layout.activity_leave_request);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        changeHeaderImage(navigationView);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.menu_open,R.string.menu_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setTitle("");
        initializeView();
        employeeNavigation(navigationView,drawerLayout,this);
        getUser();
    }
    private void initializeView(){
        leave_type = findViewById(R.id.leave_type);
        leave_from = findViewById(R.id.leave_from);
        leave_to = findViewById(R.id.leave_to);
        leave_reason =findViewById(R.id.leave_reason);
        leave_from.setOnClickListener(this);
        leave_to.setOnClickListener(this);
        btn_leave = findViewById(R.id.btn_add_leave);
        btn_leave.setOnClickListener(this);
    }
    private void getUser(){
        SharedPreferences sp = getApplicationContext().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
        name = sp.getString("name","");
        position = sp.getString("position","");
        id = sp.getString("id","");
    }
    private void datePicker(String type){
        DatePickerDialog datePickerDialog = new DatePickerDialog(LeaveRequestActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int years, int months, int days) {
                months = months+1;
                String date = months+"/"+days+"/"+years;

                if(type == "from") leave_from.setText(date);
                if(type == "to") leave_to.setText(date);
            }
        },year,month,day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }
    private void fileLeave(LeaveRequest leaveRequest){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("Leave");
        myRef.child(leaveRequest.user_id).child(leaveRequest.leave_id).setValue(leaveRequest).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent intent = new Intent(LeaveRequestActivity.this, LeaveStatus.class);
                startActivity(intent);
                Toast.makeText(LeaveRequestActivity.this,"Leave Created!",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LeaveRequestActivity.this,"Something went wrong!",Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.leave_from:
                datePicker("from");
                break;
            case R.id.leave_to:
                datePicker("to");
                break;
            case R.id.btn_add_leave:
                if(leave_type.getText().toString().isEmpty()||leave_to.getText().toString().isEmpty()||leave_from.getText().toString().isEmpty()||leave_reason.getText().toString().isEmpty()){
                    Toast.makeText(LeaveRequestActivity.this,"Some fields are empty!",Toast.LENGTH_SHORT).show();
                }else{
                    LeaveRequest leaveRequest = new LeaveRequest(id, UUID.randomUUID().toString(),name,leave_type.getText().toString(),position,leave_from.getText().toString(),leave_to.getText().toString(),leave_reason.getText().toString(),"PENDING");
                    fileLeave(leaveRequest);
                }
                break;
        }
    }
}