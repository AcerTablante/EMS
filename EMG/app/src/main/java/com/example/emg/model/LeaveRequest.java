package com.example.emg.model;


public class LeaveRequest {
    public LeaveRequest(String user_id, String leave_id, String name, String leave_type, String position, String leave_date_from, String leave_date_to, String reason, String status) {
        this.user_id = user_id;
        this.leave_id = leave_id;
        this.name = name;
        this.leave_type = leave_type;
        this.position = position;
        this.leave_date_from = leave_date_from;
        this.leave_date_to = leave_date_to;
        this.reason = reason;
        this.status = status;
    }

   public String user_id;
   public String leave_id;
   public String name;
   public String leave_type;
   public String position;

    public void setStatus(String status) {
        this.status = status;
    }

    public String leave_date_from;
   public String leave_date_to;
   public String reason;
   public String status;



    public LeaveRequest() {
    }


}
