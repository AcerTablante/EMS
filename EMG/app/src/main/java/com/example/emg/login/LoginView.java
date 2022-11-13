package com.example.emg.login;

public interface LoginView {

    void noCredentials(String email, String password);
    void loginSuccessfully();
    void loginSuccesfullyAdmin();
    void invalidCredentials();
    void test();
}
