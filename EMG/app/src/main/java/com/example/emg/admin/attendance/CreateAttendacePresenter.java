package com.example.emg.admin.attendance;


import androidx.annotation.NonNull;

import com.example.emg.model.Attendance;
import com.example.emg.model.Employee;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreateAttendacePresenter {
    CreateAttendanceView view;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();

    public CreateAttendacePresenter(CreateAttendanceView view) {
        this.view = view;
    }
    void addAttendance(String id){
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String formattedDate = df.format(c);
        //Check if user exist
        myRef.child("Employees").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(id).exists()){
                    Employee employee = snapshot.child(id).getValue(Employee.class);
                    Attendance attendance = new Attendance(id,formattedDate,"TIME IN",currentTime);
                    attendance.setName(employee.name);
                    DatabaseReference attendance_ref = myRef.child("Attendance").child(formattedDate).child(id);
                    attendance_ref.setValue(attendance).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        view.attendanceSuccess();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            view.attendanceFailed(e.getLocalizedMessage());
                        }
                    });
                }else{
                    view.attendanceFailed("Invalid barcode");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
