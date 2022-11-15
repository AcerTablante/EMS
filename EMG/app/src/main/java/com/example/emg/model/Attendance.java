package com.example.emg.model;

public class Attendance {
    public String id;
    public String name;
    public String date;
    public String attendance_type;
    public String time;
    public Attendance(){

    }

    public Attendance(String id, String date, String attendance_type, String time) {
        this.id = id;
        this.date = date;
        this.attendance_type = attendance_type;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Attendance(String name) {
        this.name = name;
    }
}
