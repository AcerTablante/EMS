package com.example.emg.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emg.R;
import com.example.emg.admin.add_employee.AddEmployeeActivity;
import com.example.emg.admin.create_news.CreateNewsActivity;
import com.example.emg.model.Employee;
import com.example.emg.model.News;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {
    private ArrayList<News> newsList;
    private Context newsContext;
    final StorageReference mStorageReference = FirebaseStorage.getInstance().getReference("news");
    public NewsAdapter(Context context, ArrayList<News> newsList){
        this.newsList = newsList;
        this.newsContext=context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView newsTitle,newsBody;
        private ImageView newsImage,news_delete;
        private CardView news_card;
        public MyViewHolder(final View view){
            super(view);
            newsTitle = view.findViewById(R.id.newsTitle);
            newsBody = view.findViewById(R.id.newsBody);
            newsImage = view.findViewById(R.id.newsImage);
            news_delete = view.findViewById(R.id.news_delete);
            news_card = view.findViewById(R.id.news_card);
        }
    }
    @NonNull
    @Override
    public NewsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item,parent,false);
        return new NewsAdapter.MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.MyViewHolder holder, int position) { ;
    String title = newsList.get(position).title;
    String body = newsList.get(position).body;
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            holder.news_delete.setVisibility(View.GONE);
        }else{
            holder.news_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(newsContext, CreateNewsActivity.class);
                    intent.putExtra("news",newsList.get(position));
                    newsContext.startActivity(intent);
                }
            });
        }
    holder.news_delete.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("news/"+newsList.get(position).image_name);
            storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = firebaseDatabase.getReference("News").child(newsList.get(position).id);
                    myRef.removeValue();
                    notifyItemRemoved(position);
                    Log.v("delete","deleted:");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("delete",e.getLocalizedMessage());
                }
            });
        }
    });

    holder.newsBody.setText(body);
    holder.newsTitle.setText(title);
    mStorageReference.child(newsList.get(position).image_name).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(holder.newsImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.v("news_adapter",e.getLocalizedMessage());
                Log.v("news_adapter",e.getMessage());
            }
        });
    }
    @Override
    public int getItemCount() {
        return newsList.size();
    }
}
