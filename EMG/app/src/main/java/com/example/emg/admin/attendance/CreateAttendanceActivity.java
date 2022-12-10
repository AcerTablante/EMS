package com.example.emg.admin.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;
import android.widget.Toolbar;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.example.emg.R;
import com.example.emg.admin.add_employee.AddEmployeeActivity;
import com.example.emg.base.AdminBase;
import com.example.emg.employee.leave_request.LeaveRequestActivity;
import com.example.emg.model.Attendance;
import com.example.emg.model.Employee;
import com.example.emg.utils.LoadingDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreateAttendanceActivity extends AdminBase implements CreateAttendanceView, View.OnClickListener{
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    CreateAttendacePresenter presenter;
    private CodeScanner mCodeScanner;
    LoadingDialog dialog;
    Button time_in,time_out;
    String scanned_id="";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_attendance);
        presenter=new CreateAttendacePresenter(this);
        initializeView();
        initializeNavBar();
        mCodeScanner.setScanMode(ScanMode.CONTINUOUS);
        mCodeScanner.setCamera(mCodeScanner.CAMERA_BACK);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mCodeScanner.stopPreview();
                        dialog.startLoadingDialog();
                        Toast.makeText(CreateAttendanceActivity.this, "Scanning", Toast.LENGTH_SHORT).show();
                        checkAttendance(result.getText());

                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        mCodeScanner.startPreview();
    }
    private void initializeView(){
        dialog = new LoadingDialog(CreateAttendanceActivity.this);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        time_in = findViewById(R.id.time_in);
        time_out = findViewById(R.id.time_out);
        time_in.setOnClickListener(this);
        time_out.setOnClickListener(this);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
    }
    private void checkAttendance(String id){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("d-MM-yyyy", Locale.getDefault());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String formattedDate = df.format(c);
        myRef.child("Attendance").child(formattedDate).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Attendance attendance = snapshot.getValue(Attendance.class);
                    if(attendance.time_out.equals("-")){
                        time_out.setEnabled(true);
                        time_out.setBackgroundColor(Color.BLACK);
                        scanned_id=id;
                    }else{
                        Toast.makeText(CreateAttendanceActivity.this,"You have already time-in/time-out",Toast.LENGTH_SHORT).show();
                        mCodeScanner.startPreview();
                    }
                    //Time out
                    //full attendance
                }else{
                    time_in.setEnabled(true);
                    time_in.setBackgroundColor(Color.BLACK);
                    scanned_id=id;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        dialog.dismissDialog();
    }

    private void initializeNavBar(){
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.menu_open,R.string.menu_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        this.adminNavigation(navigationView,drawerLayout,this);
    }
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.time_in:
                dialog.startLoadingDialog();
                presenter.addAttendance(scanned_id);
                break;
            case R.id.time_out:
                dialog.startLoadingDialog();
                presenter.timeOut(scanned_id);
                break;
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }
    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void attendanceSuccess() {
        dialog.dismissDialog();
        time_in.setEnabled(false);
        time_in.setBackgroundColor(Color.GRAY);
        time_out.setBackgroundColor(Color.GRAY);
        time_out.setEnabled(false);
        mCodeScanner.startPreview();
        Toast.makeText(this,"Attendance Added",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void attendanceFailed(String message) {
        dialog.dismissDialog();
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}