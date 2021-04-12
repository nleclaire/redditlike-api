package com.sei.redditlikeapi.model.response;

// This class will send back the JWT Token if the username and pass are correct
public class LoginResponse {
    private String JWT;

    public LoginResponse(String JWT) {
        this.JWT = JWT;
    }

    public String getJWT() {
        return JWT;
    }

    public void setJWT(String JWT) {
        this.JWT = JWT;
    }
}
