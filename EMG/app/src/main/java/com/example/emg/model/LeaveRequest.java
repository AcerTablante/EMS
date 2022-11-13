package com.example.emg.model;


public class LeaveRequest {
    String name;
    String position;
    String leave_date;
    String reason;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getLeave_date() {
        return leave_date;
    }

    public void setLeave_date(String leave_date) {
        this.leave_date = leave_date;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }


    public LeaveRequest(String name, String position, String leave_date, String reason) {
        this.name = name;
        this.position = position;
        this.leave_date = leave_date;
        this.reason = reason;
    }
}
