package com.example.emg.adapter;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emg.R;
import com.example.emg.model.Employee;
import com.example.emg.model.News;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
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
        private ImageView newsImage;
        public MyViewHolder(final View view){
            super(view);
            newsTitle = view.findViewById(R.id.newsTitle);
            newsBody = view.findViewById(R.id.newsBody);
            newsImage = view.findViewById(R.id.newsImage);
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
