package com.sei.redditlikeapi.controller;

import com.sei.redditlikeapi.model.Topic;
import com.sei.redditlikeapi.repository.TopicRepository;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.JVM) // https://www.baeldung.com/junit-5-test-order - necessary for following test order
public class TopicControllerTest {

    private Topic topic;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate; // using the restTemplate exchange method to send requests and receive response
    // https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/client/RestTemplate.html

    @Autowired
    private TopicRepository topicRepository;

    // Create HttpEntity saved my life
    // https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpEntity.html
    // Lets us spoof an HTTP request with Authorization and Content-Type headers
    private HttpEntity createHttpEntityWithBody(Object object){
        // set headers "Content-Type" : "application/json" and "Authorization" : "Bearer JWT_TOKEN"
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1bmNoLmxlY2xhaXJlQGVtYWlsLmNvbSIsImV4cCI6MTYxODU1NTA2MCwiaWF0IjoxNjE4NTE5MDYwfQ.p_1hvQxeE9UhmQVCRC-mmTBdEMTau8kUg-gUJnFypqI");

        // return an HttpEntity with body of topic and headers
        return new HttpEntity<>(object, headers);
    }

    // Another HttpEntity, to create generic request
    private HttpEntity createHttpRequest(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1bmNoLmxlY2xhaXJlQGVtYWlsLmNvbSIsImV4cCI6MTYxODU1NTA2MCwiaWF0IjoxNjE4NTE5MDYwfQ.p_1hvQxeE9UhmQVCRC-mmTBdEMTau8kUg-gUJnFypqI");

        return new HttpEntity<>(headers);
    }

    // Create new topic, and and POST it
    // Check to make sure that status code is 200, and response fields match that of the Test topic
    @Test
    public void postTopicShouldReturnOKForASingleTopicAndIsOfClassTypeTopic(){
        String resourceURL = "http://localhost:" + port + "/api/topics/new";
        topic = new Topic("Test", "Test description");

        // make new HttpEntity with topic as request body
        HttpEntity<?> request = createHttpEntityWithBody(topic);
        ResponseEntity<?> response = restTemplate.exchange(resourceURL, HttpMethod.POST, request, Topic.class);

        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("name", "Test");
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("description", "Test description");
    }

    // Find that same Test topic by name, and GET it
    // Check to make sure the Http status is 200 (OK) and that fields match the Test topic
    @Test
    public void getTopicShouldReturnASingleTopic(){

        Long id = topicRepository.findByName("Test").getId();
        String resourceURL = "http://localhost:" + port + "/api/topics/" + id;
        HttpEntity<?> request = createHttpRequest();
        ResponseEntity<?> response = restTemplate.exchange(resourceURL, HttpMethod.GET, request, Topic.class);

        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("name", "Test");
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("description", "Test description");

    }

    // Find Test topic by name, and PUT some new info there
    // Check to make sure info matches updated information
    @Test
    public void putTopicShouldReturnASingleTopicWithUpdatedFields(){
        Long id = topicRepository.findByName("Test").getId();
        String resourceURL = "http://localhost:" + port + "/api/topics/" + id;
        topic = new Topic("Test", "Update Description");
        HttpEntity<?> request = createHttpEntityWithBody(topic);
        ResponseEntity<?> response = restTemplate.exchange(resourceURL, HttpMethod.PUT, request, Topic.class);

        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("name", "Test");
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("description", "Update Description");
    }

    // Get Test topic by name, DELETE it
    // Verify that the response contains field "Response" as specified in controller
    @Test
    public void deleteTopicShouldReturnAResponseEntityWithFieldResponse(){
        Long id = topicRepository.findByName("Test").getId();
        String resourceURL = "http://localhost:" + port + "/api/topics/" + id;

        HttpEntity<?> request = createHttpRequest();
        ResponseEntity<?> response = restTemplate.exchange(resourceURL, HttpMethod.DELETE, request, Object.class);

        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertThat(response.getBody()).hasFieldOrProperty("Response");
    }
}
