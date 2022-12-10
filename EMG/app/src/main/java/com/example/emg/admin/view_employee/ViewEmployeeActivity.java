package com.example.emg.admin.view_employee;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emg.R;
import com.example.emg.model.Employee;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewEmployeeActivity extends AppCompatActivity implements View.OnClickListener {
    CircleImageView profile_pic;
    ImageView qr;
    RelativeLayout relative;
    TextView tv_name,tv_position,tv_department,tv_email,tv_phone;
    Employee employee;
    Button print;
    Uri imagelink;
    Bitmap bitmap;
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
        print = findViewById(R.id.print);
        print.setOnClickListener(this);
        relative = findViewById(R.id.main_rel);
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
    private void createPdf() {
        DisplayMetrics displayMetrics = new DisplayMetrics ();
        this.getWindowManager ().getDefaultDisplay ().getMetrics ( displayMetrics );
        float height = displayMetrics.heightPixels;
        float width = displayMetrics.widthPixels;

        int convertHeight = (int) height,
                convertWidth = (int) width;

        PdfDocument document = new PdfDocument ();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder ( convertWidth, convertHeight, 1 ).create ();
        PdfDocument.Page page = document.startPage ( pageInfo );

        Canvas canvas = page.getCanvas ();
        Paint paint = new Paint ();
        canvas.drawPaint ( paint );
        bitmap = Bitmap.createScaledBitmap ( bitmap, convertWidth, convertHeight, true );
        canvas.drawBitmap ( bitmap, 0, 0, null );
        document.finishPage ( page );
        Calendar instance = Calendar.getInstance ();
        String format = new SimpleDateFormat( "MM/dd/yyyy", Locale.getDefault () ).format ( instance.getTime () );
        String format2 = new SimpleDateFormat ( "HH:mm:ss", Locale.getDefault () ).format ( instance.getTime () );
        String[] split = format.split ( "/" );
        String[] split2 = format2.split ( ":" );
        //pdf download location
        String targetPdf = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File filepath = new File ( targetPdf ,employee.name+"_"+System.currentTimeMillis()+".pdf");
        try {
            document.writeTo ( new FileOutputStream( filepath ) );
        } catch (IOException e) {
            e.printStackTrace ();
            Toast.makeText ( this, "something want wrong try again" + e.toString (), Toast.LENGTH_SHORT ).show ();
        }
        //close document
        document.close ();
        //progress bar
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog ( this );
        progressDialog.setMessage ( "Saving..." + targetPdf );
        progressDialog.show ();
        //time after cancel
        new Handler().postDelayed (new Runnable () {
            @Override
            public void run() {
                print.setVisibility(View.VISIBLE);
                progressDialog.cancel ();
            }
        }, 5000 );
    }
    private Bitmap loadBitmapFromView(RelativeLayout relativeLayout, int width, int height) {
        bitmap = Bitmap.createBitmap ( width, height, Bitmap.Config.ARGB_8888 );
        Canvas canvas = new Canvas ( bitmap );
        relativeLayout.draw ( canvas );
        return bitmap;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.print:
                print.setVisibility(View.GONE);
                loadBitmapFromView(relative,relative.getWidth(),relative.getHeight());
                createPdf();
                break;
        }
    }
}