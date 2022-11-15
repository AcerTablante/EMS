package com.example.emg.admin.add_employee;


import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.emg.model.Employee;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.storage.UploadTask;

public class AddEmployeePresenter {
    AddEmployeeView view;
    public AddEmployeePresenter(AddEmployeeView view) {
        this.view = view;
    }
    void register(Employee employee) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("Employees");
        final StorageReference mStorageReference = FirebaseStorage.getInstance().getReference("uploads");
        final FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(employee.email,employee.password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = auth.getCurrentUser();

                    Employee new_employee = new Employee(employee.name,employee.address,employee.phone,employee.email,employee.department,employee.position,employee.password,user.getUid());
                    StorageReference fileReference = mStorageReference.child(user.getUid());
                    fileReference.putFile(employee.imageURI)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    myRef.child(new_employee.id).setValue(new_employee).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            view.addEmployeeSuccess();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            view.addEmployeeFailed();
                                        }
                                    });
                                view.imageUploadMessage("Upload Success");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            view.imageUploadMessage("Upload Failed Something went Wrong");
                            Log.v("firebase",e.getLocalizedMessage());
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.v("firebase",e.getLocalizedMessage());
            }
        });
    }
    void editEmployee(Employee employee){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("Employees");
        final StorageReference mStorageReference = FirebaseStorage.getInstance().getReference("uploads");
        DatabaseReference ref_old_password = myRef.child(employee.id).child("password");
        //Update auth
        ref_old_password.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String old_password = dataSnapshot.getValue(String.class);
                FirebaseAuth user = FirebaseAuth.getInstance();
                user.signInWithEmailAndPassword(employee.email,old_password);
                user.getCurrentUser().updatePassword(employee.password).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    view.message(e.getLocalizedMessage());
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        //Update database
        Employee edited_employee = new Employee(employee.name,employee.address,employee.phone,employee.email,employee.department,employee.position,employee.password,employee.id);
        myRef.child(employee.id).setValue(edited_employee).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                view.addEmployeeFailed();
            }
        });
        //Update Storage
        StorageReference fileReference = mStorageReference.child(employee.id);
        fileReference.putFile(employee.imageURI);
        view.addEmployeeSuccess();
    }

}
