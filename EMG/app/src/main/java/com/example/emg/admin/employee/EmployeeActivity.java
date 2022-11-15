package com.example.emg.admin.employee;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emg.R;
import com.example.emg.adapter.EmployeeAdapter;
import com.example.emg.admin.add_employee.AddEmployeeActivity;
import com.example.emg.admin.attendance.CreateAttendanceActivity;
import com.example.emg.base.AdminBase;
import com.example.emg.model.Employee;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class EmployeeActivity extends AdminBase implements EmployeeView, View.OnClickListener {
    private ArrayList<Employee> employee;
    private RecyclerView recyclerView;
    Button btn_add_employee;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    EmployeeAdapter employeeAdapter;
    Toolbar toolbar;
    EmployeePresenter presenter;
    DatabaseReference database;
    ArrayList<Employee> list = new ArrayList<>();
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
        setContentView(R.layout.activity_employees);
        presenter = new EmployeePresenter(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initializeView();
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.menu_open,R.string.menu_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setTitle("");
        this.adminNavigation(navigationView,drawerLayout,this);
        recyclerView = findViewById(R.id.employeeRecycle);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //dummy data
        employeeAdapter = new EmployeeAdapter(this,list);
        recyclerView.setAdapter(employeeAdapter);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
        database = FirebaseDatabase.getInstance().getReference("Employees");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Employee employee = dataSnapshot.getValue(Employee.class);
                    list.add(employee);
                }
                employeeAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    ItemTouchHelper.SimpleCallback simpleCallback=new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            Employee selectedEmployee = list.get(position);
            deleteData(selectedEmployee);
            employeeAdapter.notifyDataSetChanged();
//            Toast.makeText(EmployeeActivity.this, , Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(EmployeeActivity.this, R.color.red))
                    .addActionIcon(R.drawable.ic_baseline_delete_24)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };


    private void initializeView(){
        btn_add_employee = findViewById(R.id.btn_add_employee);
        btn_add_employee.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_add_employee:
                startActivity(new Intent(EmployeeActivity.this, AddEmployeeActivity.class));
                break;
        }

    }
    private void deleteData(Employee employee){
        //Delete image
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("uploads/"+employee.id);
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            Log.v("delete","deleted:" + employee.name);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            Log.e("delete",e.getLocalizedMessage());
            }
        });
        //Delete Auth
        FirebaseAuth user = FirebaseAuth.getInstance();
        user.signInWithEmailAndPassword(employee.email,employee.password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                authResult.getUser().delete();
                //Delete Realtime Database
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference myRef = firebaseDatabase.getReference("Employees").child(employee.id);
                myRef.removeValue();
                Toast.makeText(EmployeeActivity.this,"Employee removed",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("delete",e.getLocalizedMessage());
            }
        });


    }


}