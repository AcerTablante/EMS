package com.example.emg.model;


import android.os.Parcel;
import android.os.Parcelable;

public class News implements Parcelable {
    public String id;
    public String title;
    public  String body;
    public  String image_name;
    public String image_uri;

    public void setImage_uri(String image_uri) {
        this.image_uri = image_uri;
    }

    public News(){

    }

    public News(String id, String title, String body, String image_name) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.image_name = image_name;
    }

    protected News(Parcel in) {
        id = in.readString();
        title = in.readString();
        body = in.readString();
        image_name = in.readString();
    }

    public static final Creator<News> CREATOR = new Creator<News>() {
        @Override
        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(body);
        parcel.writeString(image_name);
    }
}
