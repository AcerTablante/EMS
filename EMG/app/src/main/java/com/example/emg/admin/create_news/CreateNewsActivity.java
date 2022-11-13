package com.example.emg.admin.create_news;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.emg.R;
import com.example.emg.admin.news.NewsActivity;
import com.example.emg.model.News;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class CreateNewsActivity extends AppCompatActivity implements View.OnClickListener {
    private Uri mImageUri;
    private static final int PICK_IMAGE_REQUEST = 1;
    ImageView news_image;
    Button upload,createNews;
    TextInputEditText newsTitle,newsBody;
    DatabaseReference database;
    Integer newsCount= 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_news);
        initializeView();
        getNewsCount();
    }
    private void initializeView(){
        news_image = findViewById(R.id.newsImage);
        upload = findViewById(R.id.btn_upload);
        createNews = findViewById(R.id.btn_create_news);
        newsTitle = findViewById(R.id.newsTitle);
        newsBody = findViewById(R.id.newsBody);
        upload.setOnClickListener(this);
        createNews.setOnClickListener(this);
    }
    private void browseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data!= null && data.getData()!=null){
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(news_image);
        }
    }
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_upload:
                browseImage();
                break;
            case R.id.btn_create_news:
                if(newsTitle.getText().toString().trim()!=null||newsBody.getText().toString()!=null){
                    createNews(newsTitle.getText().toString().trim(),newsBody.getText().toString());
                }else{
                    Toast.makeText(CreateNewsActivity.this,"Missing Fields",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void createNews(String title,String body) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("News");
        final StorageReference mStorageReference = FirebaseStorage.getInstance().getReference("news");
        Integer final_count = newsCount++;
        StorageReference fileReference = mStorageReference.child(final_count+"."+getFileExtension(mImageUri));
        fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                News news = new News(title,body,final_count.toString()+"."+getFileExtension(mImageUri));
                myRef.child(newsCount.toString()).setValue(news).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(CreateNewsActivity.this,"Successfully Added News",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(CreateNewsActivity.this, NewsActivity.class);
                    startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreateNewsActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                        Log.e("firebaseError",e.getLocalizedMessage());
                        Log.e("firebaseError",e.getMessage());
                    }
                });
            }
        });
    }
    private void getNewsCount(){
        database = FirebaseDatabase.getInstance().getReference("News");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                newsCount=0;
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                   newsCount++;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}