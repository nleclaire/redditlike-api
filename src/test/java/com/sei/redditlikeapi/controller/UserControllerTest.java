package com.sei.redditlikeapi.controller;

import com.sei.redditlikeapi.model.User;
import com.sei.redditlikeapi.utilities.UserAdminHint;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.*;


import java.net.http.HttpResponse;

@ExtendWith(SpringExtension.class)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    private User user;
    private ControllerTestUtility utility = new ControllerTestUtility();

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private HttpEntity createHttpEntityWithBody(Object object){
        // set headers "Content-Type" : "application/json" and "Authorization" : "Bearer JWT_TOKEN"
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setBearerAuth("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1bmNoLmxlY2xhaXJlQGVtYWlsLmNvbSIsImV4cCI6MTYxODU1NTA2MCwiaWF0IjoxNjE4NTE5MDYwfQ.p_1hvQxeE9UhmQVCRC-mmTBdEMTau8kUg-gUJnFypqI");

        // return an HttpEntity with body of topic and headers
        return new HttpEntity<>(object, headers);
    }

    @Test
    public void postUserShouldReturnHttpStatusOkAndUser(){
        String resourceURL = "http://localhost:" + port + "/auth/users/register";
        String password = passwordEncoder.encode("123456");
        user = new User("Han", "HanSolo@gmail.com", password);

        HttpEntity<?> request = createHttpEntityWithBody(user);
        ResponseEntity<?> response = restTemplate.exchange(resourceURL, HttpMethod.POST, request, User.class);
        System.out.println(response);

        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("userName", "Han");
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("emailAddress", "HanSolo@gmail.com");
    }
}
