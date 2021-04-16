package com.sei.redditlikeapi.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class ControllerTestUtility {
    public HttpEntity createHttpEntityWithBody(Object object) {
        // set headers "Content-Type" : "application/json" and "Authorization" : "Bearer JWT_TOKEN"
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ2aWt0b3Iub2xlc25ldnljaEBob3RtYWlsLmNvbSIsImV4cCI6MTYxODYxMzIzNCwiaWF0IjoxNjE4NTc3MjM0fQ._Dmu8aP4ahDlA-NBG-ddPKSAWVe1m6S4GZA4BQHPajw");

        // return an HttpEntity with body of topic and headers
        return new HttpEntity<>(object, headers);
    }

    // Another HttpEntity, to create generic request
    public HttpEntity createHttpRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ2aWt0b3Iub2xlc25ldnljaEBob3RtYWlsLmNvbSIsImV4cCI6MTYxODYxMzIzNCwiaWF0IjoxNjE4NTc3MjM0fQ._Dmu8aP4ahDlA-NBG-ddPKSAWVe1m6S4GZA4BQHPajw");

        return new HttpEntity<>(headers);
    }
}
