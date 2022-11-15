package com.example.emg.admin.add_employee;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.emg.R;

import com.example.emg.admin.employee.EmployeeActivity;
import com.example.emg.model.Employee;
import com.example.emg.utils.LoadingDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddEmployeeActivity extends AppCompatActivity implements AddEmployeeView , View.OnClickListener {
    AddEmployeePresenter presenter;
    ImageView profile_pic;
    TextInputEditText fullname,address,phone,department,position,email,password;
    String str_fullname,str_address,str_phone,str_department,str_position,str_email,str_password;
    Button btn_upload,btn_submit;
    LoadingDialog dialog;
    Employee employee;
    Uri imagelink;
    private Uri mImageUri;
    private static final int PICK_IMAGE_REQUEST = 1;
    private StorageReference mStorageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);
        initializeView();
        getDatafromIntent();
        populateFields();
        presenter = new AddEmployeePresenter(this);
    }
    private void initializeView(){
        dialog = new LoadingDialog(AddEmployeeActivity.this);
        fullname = findViewById(R.id.fullname);
        address = findViewById(R.id.address);
        phone = findViewById(R.id.phone);
        department = findViewById(R.id.department);
        position = findViewById(R.id.position);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_upload = findViewById(R.id.btn_upload);
        profile_pic = findViewById(R.id.profile_pic);
        btn_submit= findViewById(R.id.btnAdd);
        btn_submit.setOnClickListener(this);
        btn_upload.setOnClickListener(this);
    }
    private void getDatafromIntent(){
        Intent intent = getIntent();
        employee = intent.getParcelableExtra("employee");
        imagelink = intent.getParcelableExtra("imageLink");
    }
    private void populateFields(){
       if(employee!=null){
           fullname.setText(employee.name);
           address.setText(employee.address);
           phone.setText(employee.phone);
           department.setText(employee.department);
           position.setText(employee.position);
           email.setText(employee.email);
           email.setEnabled(false);
           password.setText(employee.password);
           mImageUri = employee.imageURI;
           Picasso.get().load(employee.imageURI).into(profile_pic);
       }
    }
    private void getDataFromFields(){
        str_fullname = fullname.getText().toString().trim();
        str_address = address.getText().toString().trim();
        str_phone = phone.getText().toString().trim();
        str_department = department.getText().toString().trim();
        str_position = position.getText().toString().trim();
        str_email = email.getText().toString().trim();
        str_password = password.getText().toString().trim();
    }
    private Boolean validateEmail(String email){
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_upload:
                browseImage();
                break;
            case R.id.btnAdd:
                getDataFromFields();
                if(str_fullname.isEmpty()||str_address.isEmpty()||str_phone.isEmpty()|str_department.isEmpty()||str_position.isEmpty()||str_email.isEmpty()||str_password.isEmpty()){
                    Toast.makeText(this,"All fields must be filled up!",Toast.LENGTH_SHORT).show();
                }else if(!validateEmail(str_email)){
                    Toast.makeText(this,"Email Address must be valid",Toast.LENGTH_SHORT).show();
                }else if(str_password.length()<8){
                    Toast.makeText(this,"Password should be 8 chararacters minimum",Toast.LENGTH_SHORT).show();
                }else if(mImageUri==null){
                    Toast.makeText(this,"Image is required",Toast.LENGTH_SHORT).show();
                }
                else{
                    dialog.startLoadingDialog();
                    if(employee!=null){
                        Employee new_employee = new Employee(str_fullname,str_address,str_phone,str_email,str_department,str_position,str_password,employee.id);
                        if(employee.imageURI!=null){
                            new_employee.setImageURI(employee.imageURI);
                        }else{
                            new_employee.setImageURL(System.currentTimeMillis()+"."+getFileExtension(mImageUri));
                            new_employee.setImageURI(mImageUri);
                        }
                        presenter.editEmployee(new_employee);
                    }
                    Employee new_employee = new Employee(str_fullname,str_address,str_phone,str_email,str_department,str_position,str_password);
                    new_employee.setImageURL(System.currentTimeMillis()+"."+getFileExtension(mImageUri));
                    new_employee.setImageURI(mImageUri);
                    presenter.register(new_employee);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data!= null && data.getData()!=null){
        mImageUri = data.getData();
        Picasso.get().load(mImageUri).into(profile_pic);
        }
    }

    private void browseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    public void addEmployeeSuccess() {
        dialog.dismissDialog();
        Toast.makeText(this,"Successfully Added Employee",Toast.LENGTH_LONG).show();
        startActivity(new Intent(AddEmployeeActivity.this, EmployeeActivity.class));
    }

    @Override
    public void editEmployeeSuccess() {
        dialog.dismissDialog();
        Toast.makeText(this,"Successfully Updated Employee",Toast.LENGTH_LONG).show();
        startActivity(new Intent(AddEmployeeActivity.this, EmployeeActivity.class));
    }

    @Override
    public void addEmployeeFailed() {
        dialog.dismissDialog();
        Toast.makeText(this,"Something went wrong",Toast.LENGTH_LONG).show();
    }

    @Override
    public void imageUploadMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void message(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }
}