package com.example.emg.admin.view_employee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.emg.R;
import com.example.emg.model.Employee;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewEmployeeActivity extends AppCompatActivity {
    CircleImageView profile_pic;
    ImageView qr;
    TextView tv_name,tv_position,tv_department,tv_email,tv_phone;
    Employee employee;
    Uri imagelink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_employee);
        getDatafromIntent();
        initializeView();
        populateFields();
    }
    private void initializeView(){
        profile_pic = findViewById(R.id.profile_pic);
        tv_name = findViewById(R.id.tv_name);
        tv_position = findViewById(R.id.tv_position);
        tv_department = findViewById(R.id.tv_department);
        tv_email = findViewById(R.id.tv_email);
        tv_phone = findViewById(R.id.tv_phone);
        qr = findViewById(R.id.qr);
    }
    private void populateFields(){
        if(employee.imageURI!=null) {
            Picasso.get().load(employee.imageURI).into(profile_pic);
        }else{
            Picasso.get().load("https://w7.pngwing.com/pngs/481/915/png-transparent-computer-icons-user-avatar-woman-avatar-computer-business-conversation-thumbnail.png").into(profile_pic);
        }
        tv_name.setText(employee.name);
        tv_position.setText(employee.position);
        tv_department.setText(employee.department);
        tv_phone.setText(employee.phone);
        tv_email.setText(employee.email);
        displayBarcode(employee.id);
    }
    private void displayBarcode(String id){
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(id, BarcodeFormat.QR_CODE, 900, 900);
            qr.setImageBitmap(bitmap);
        } catch(Exception e) {
            Log.v("qr",e.getLocalizedMessage());
        }
    }
    private void getDatafromIntent(){
        Intent intent = getIntent();
        employee = intent.getParcelableExtra("employee");
        imagelink = intent.getParcelableExtra("imageLink");
    }
}