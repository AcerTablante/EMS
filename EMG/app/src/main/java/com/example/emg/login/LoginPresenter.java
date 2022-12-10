package com.example.emg.login;


import androidx.annotation.NonNull;

import com.example.emg.model.Attendance;
import com.example.emg.model.Employee;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginPresenter {
    LoginView view;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();
    Employee employee;
    public LoginPresenter(LoginView view) {
        this.view = view;
    }

    void login(String email,String password){
        if(email.isEmpty() || password.isEmpty()){
            view.noCredentials(email,password);
        }else if(checkIfAdmin(email,password)){
           view.loginSuccesfullyAdmin();
        }else{
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        myRef.child("Employees").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.child(auth.getCurrentUser().getUid()).exists()){
                                    employee = snapshot.child(auth.getCurrentUser().getUid()).getValue(Employee.class);
                                    view.loginSuccessfully(employee.name,employee.position,employee.id);
                                }else{
                                    view.test();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });

                    }else{
                        view.invalidCredentials();
                    }
                }
            });

        }
    }
    private boolean checkIfAdmin(String email,String password){
        if(email.equals("admin@gmail.com") && password.equals("admin123")){
            return true;
        }else{
            return false;
        }
    }



    //If not static
//    FirebaseAuth auth;
//    void login(String email, String password) {
//        auth = FirebaseAuth.getInstance();
//        if (email.isEmpty() || password.isEmpty()) {
//            view.noCredentials(email,password);
//        } else {
//            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    if(task.isSuccessful()){
//                        view.loginSuccessfully();
//                    }else{
//                        view.invalidCredentials();
//                    }
//                }
//            });
//
//        }
    }

