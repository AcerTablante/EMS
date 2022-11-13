package com.example.emg.login;


import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPresenter {
    LoginView view;

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
                        view.loginSuccessfully();
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

