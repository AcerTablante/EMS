package com.example.emg.model;


import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Employee implements Parcelable {
    public String name;
    public String address;
    public String phone;
    public String email;
    public String department;
    public String position;
    public String password;
    public String id;
    public String imageURL;
    public Uri imageURI;

    public Employee(String name, String address, String phone, String email, String department, String position, String password, String id, String imageURL) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.department = department;
        this.position = position;
        this.password = password;
        this.id = id;
        this.imageURL = imageURL;
    }
    public Employee(String name, String address, String phone, String email, String department, String position, String password) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.password=password;
        this.email = email;
        this.department = department;
        this.position = position;
    }

    public Employee(String name, String address, String phone, String email, String department, String position, String password, String id) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.department = department;
        this.position = position;
        this.password = password;
        this.id = id;
    }

    public Employee(){

    }

    protected Employee(Parcel in) {
        name = in.readString();
        address = in.readString();
        phone = in.readString();
        email = in.readString();
        department = in.readString();
        position = in.readString();
        password = in.readString();
        id = in.readString();
        imageURL = in.readString();
        imageURI = in.readParcelable(Uri.class.getClassLoader());
    }

    public static final Creator<Employee> CREATOR = new Creator<Employee>() {
        @Override
        public Employee createFromParcel(Parcel in) {
            return new Employee(in);
        }

        @Override
        public Employee[] newArray(int size) {
            return new Employee[size];
        }
    };

    public String getName() {
        return name;
    }
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
    public void setImageURI(Uri imageURI) {
        this.imageURI = imageURI;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Employee(String name) {
        this.name = name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(address);
        parcel.writeString(phone);
        parcel.writeString(email);
        parcel.writeString(department);
        parcel.writeString(position);
        parcel.writeString(password);
        parcel.writeString(id);
        parcel.writeString(imageURL);
        parcel.writeParcelable(imageURI, i);
    }
}
