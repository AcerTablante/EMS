package com.example.emg.login;

public interface LoginView {

    void noCredentials(String email, String password);
    void loginSuccessfully(String name ,String position,String id);
    void loginSuccesfullyAdmin();
    void invalidCredentials();
    void test();
}
