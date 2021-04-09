package com.sei.redditlikeapi.model.request;

// This class will allow User to login with username and password
public class LoginRequest {
    private String emailAddress;
    private String password;

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPassword() {
        return password;
    }

}
