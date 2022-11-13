package com.example.emg.adapter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emg.admin.view_employee.ViewEmployeeActivity;
import com.example.emg.model.Employee;
import com.example.emg.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.MyViewHolder> {
    final StorageReference mStorageReference = FirebaseStorage.getInstance().getReference("uploads");
    private ArrayList<Employee> employeeList;
    private Context employeeContext;
    Uri imageLink;
     public EmployeeAdapter(Context context,ArrayList<Employee> employeeList){
         this.employeeList = employeeList;
         this.employeeContext=context;
     }

     public class MyViewHolder extends RecyclerView.ViewHolder{
         private TextView name;
         private ImageView profile;
         private Button btnView;
         public MyViewHolder(final View view){
             super(view);
             name = view.findViewById(R.id.fullname);
             profile = view.findViewById(R.id.profile_pic);
             btnView = view.findViewById(R.id.btnView);
         }
     }

    @NonNull
    @Override
    public EmployeeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_item,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeAdapter.MyViewHolder holder, int position) {

        mStorageReference.child(employeeList.get(position).id).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(holder.profile);
                employeeList.get(position).setImageURI(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Picasso.get().load("https://w7.pngwing.com/pngs/481/915/png-transparent-computer-icons-user-avatar-woman-avatar-computer-business-conversation-thumbnail.png").into(holder.profile);
            }
        });
        String name = employeeList.get(position).name;
        holder.name.setText(name);
        holder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(employeeContext, ViewEmployeeActivity.class);

                intent.putExtra("employee",employeeList.get(position));
                intent.putExtra("imageLink",imageLink);
                employeeContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }
}
