package com.example.emg.login;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.emg.MainActivity;
import com.example.emg.R;
import com.example.emg.employee.dashboard.EmployeeDashboard;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity implements View.OnClickListener,LoginView  {
    Button loginButton;
    TextInputEditText edit_email,edit_password;
    LoginPresenter presenter;
    AlertDialog.Builder builder;
    private FirebaseAuth mAuth;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        presenter = new LoginPresenter(this);
        initializeView();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(Login.this,EmployeeDashboard.class));
        }
    }
    private void initializeView(){
        loginButton = findViewById(R.id.btnlogin);
        loginButton.setOnClickListener(this);
        edit_email = findViewById(R.id.email);
        edit_password = findViewById(R.id.password);
        builder = new AlertDialog.Builder(this);
        mAuth = FirebaseAuth.getInstance();
        sp = getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnlogin:
                presenter.login(edit_email.getText().toString().trim(),edit_password.getText().toString().trim());
                break;
        }
    }
    @Override
    public void noCredentials(String email, String password) {

    }

    @Override
    public void loginSuccessfully(String name,String position,String id) {
        Toast.makeText(this,"Login Successfully!",Toast.LENGTH_SHORT).show();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("name",name);
        editor.putString("position",position);
        editor.putString("id",id);
        editor.commit();
        startActivity(new Intent(Login.this,EmployeeDashboard.class));
    }

    @Override
    public void loginSuccesfullyAdmin() {
        Toast.makeText(this,"Login Successfully!",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Login.this, MainActivity.class));
    }

    @Override
    public void invalidCredentials() {
        builder.setTitle("Incorrect Credentials");
        builder.setMessage("The credentials you input doesn't appear to belong to an account please try again");
        builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alert = builder.setIcon(android.R.drawable.ic_dialog_alert).show();
    }

    @Override
    public void test() {

    }
}