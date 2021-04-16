package com.sei.redditlikeapi.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class ControllerTestUtility {
    public HttpEntity createHttpEntityWithBody(Object object) {
        // set headers "Content-Type" : "application/json" and "Authorization" : "Bearer JWT_TOKEN"
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjM3BvQGdtYWlsLmNvbSIsImV4cCI6MTYxODYyNDQyMiwiaWF0IjoxNjE4NTg4NDIyfQ.0HwZKOUgofZMyigtIDqrw-AwtgMy940w6hqMt7cGFUU");

        // return an HttpEntity with body of object and headers
        return new HttpEntity<>(object, headers);
    }

    // Another HttpEntity, to create generic request
    public HttpEntity createHttpRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjM3BvQGdtYWlsLmNvbSIsImV4cCI6MTYxODYyNDQyMiwiaWF0IjoxNjE4NTg4NDIyfQ.0HwZKOUgofZMyigtIDqrw-AwtgMy940w6hqMt7cGFUU");

        return new HttpEntity<>(headers);
    }
}
