package com.example.emg.base;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.emg.admin.news.NewsActivity;
import com.example.emg.admin.view_attendance.AttendanceActivity;
import com.example.emg.admin.leave.Leave;
import com.example.emg.MainActivity;
import com.example.emg.R;
import com.example.emg.admin.employee.EmployeeActivity;
import com.example.emg.admin.attendance.CreateAttendanceActivity;

import com.google.android.material.navigation.NavigationView;

public abstract class AdminBase extends AppCompatActivity {
    public void adminNavigation(NavigationView nav, DrawerLayout drawerLayout, Context context){
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_dashboard:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        context.startActivity(new Intent(context, MainActivity.class));
                        break;
                    case R.id.newsfeed:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        startActivity(new Intent(context, NewsActivity.class));
                        break;
                    case R.id.Employees:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        startActivity(new Intent(context, EmployeeActivity.class));
                        break;
                    case R.id.leaves:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        context.startActivity(new Intent(context, Leave.class));
                        break;
                    case R.id.qr:
                        context.startActivity(new Intent(context, CreateAttendanceActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.attendance:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        context.startActivity(new Intent(context, AttendanceActivity.class));
                        break;
                }
                return true;
            }
        });
    };
}
