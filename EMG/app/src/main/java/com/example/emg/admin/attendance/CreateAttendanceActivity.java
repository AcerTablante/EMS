package com.example.emg.admin.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.example.emg.R;
import com.example.emg.admin.add_employee.AddEmployeeActivity;
import com.example.emg.base.AdminBase;
import com.example.emg.utils.LoadingDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.Result;

public class CreateAttendanceActivity extends AdminBase implements CreateAttendanceView, View.OnClickListener{
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    CreateAttendacePresenter presenter;
    private CodeScanner mCodeScanner;
    LoadingDialog dialog;


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
                        Toast.makeText(CreateAttendanceActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
                        presenter.addAttendance(result.getText());
                        dialog.startLoadingDialog();
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
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
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
        Toast.makeText(this,"Attendance Added",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void attendanceFailed(String message) {
        dialog.dismissDialog();
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}