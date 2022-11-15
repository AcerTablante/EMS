package com.example.emg.admin.add_employee;


public interface AddEmployeeView {
    void addEmployeeSuccess();
    void editEmployeeSuccess();
    void addEmployeeFailed();
    void imageUploadMessage(String message);
    void message(String message);
}
