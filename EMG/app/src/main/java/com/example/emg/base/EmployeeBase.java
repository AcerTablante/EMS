package com.example.emg.base;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.emg.R;
import com.example.emg.employee.attendance.EmployeeAttendance;
import com.example.emg.employee.calendar.EmployeeCalendar;
import com.example.emg.employee.dashboard.EmployeeDashboard;
import com.example.emg.employee.leave_request.LeaveRequestActivity;
import com.example.emg.employee.leave_status.LeaveStatus;
import com.example.emg.login.Login;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public abstract class EmployeeBase extends AppCompatActivity {
    public void employeeNavigation(NavigationView nav, DrawerLayout drawerLayout, Context context){
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_dashboard:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        context.startActivity(new Intent(context, EmployeeDashboard.class));
                        break;
                    case R.id.Calendar:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        context.startActivity(new Intent(context, EmployeeCalendar.class));
                        break;
                    case R.id.attendance:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        context.startActivity(new Intent(context, EmployeeAttendance.class));
                        break;
                    case R.id.leave_request:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        context.startActivity(new Intent(context, LeaveRequestActivity.class));
                        break;
                    case R.id.leave_status:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        context.startActivity(new Intent(context, LeaveStatus.class));
                        break;
                    case R.id.exit:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        FirebaseAuth.getInstance().signOut();
                        context.getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE).edit().clear().apply();
                        context.startActivity(new Intent(context, Login.class));
                        break;
                }
                return true;
            }
        });
    };

    public void changeHeaderImage(NavigationView nav){

        SharedPreferences sp = getApplicationContext().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
        String name = sp.getString("name","");
//        position = sp.getString("position","");
        String id = sp.getString("id","");

        nav.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                nav.removeOnLayoutChangeListener( this );
                TextView textView = nav.findViewById(R.id.employee_name);
                textView.setText(name);
                CircleImageView image = nav.findViewById(R.id.emp_pic);
                final StorageReference mStorageReference = FirebaseStorage.getInstance().getReference("uploads");
                mStorageReference.child(id).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(image);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Picasso.get().load("https://w7.pngwing.com/pngs/481/915/png-transparent-computer-icons-user-avatar-woman-avatar-computer-business-conversation-thumbnail.png").into(image);
                    }
                });

            }
        });


    }

}
