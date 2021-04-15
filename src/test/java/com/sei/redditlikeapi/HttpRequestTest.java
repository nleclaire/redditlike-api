package com.sei.redditlikeapi;

import com.sei.redditlikeapi.model.Topic;
import com.sei.redditlikeapi.repository.TopicRepository;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
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
public class HttpRequestTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private Topic topic;

    @Autowired
    private TopicRepository topicRepository;

    // Create HttpEntity saved my life
    // https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpEntity.html
    private HttpEntity createHttpEntityWithTopic(Topic topic){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ2aWt0b3Iub2xlc25ldnljaEBob3RtYWlsLmNvbSIsImV4cCI6MTYxODU0MzAzMiwiaWF0IjoxNjE4NTA3MDMyfQ.PgmSGbOudHhehOZZ5TQ1hIzqjnP07q4Ev9yIexFybsQ");

        return new HttpEntity<>(topic, headers);
    }

    private HttpEntity createHttpRequest(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ2aWt0b3Iub2xlc25ldnljaEBob3RtYWlsLmNvbSIsImV4cCI6MTYxODU0MzAzMiwiaWF0IjoxNjE4NTA3MDMyfQ.PgmSGbOudHhehOZZ5TQ1hIzqjnP07q4Ev9yIexFybsQ");

        return new HttpEntity<>(headers);
    }

    @Test
    public void postTopicShouldReturnOKForASingleTopicAndIsOfClassTypeTopic(){
        String resourceURL = "http://localhost:" + port + "/api/topics/new";
        topic = new Topic("Test", "Test description");

        HttpEntity<?> request = createHttpEntityWithTopic(topic);
        ResponseEntity<?> response = restTemplate.exchange(resourceURL, HttpMethod.POST, request, Topic.class);

        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("name", "Test");
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("description", "Test description");
    }

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

    @Test
    public void putTopicShouldReturnASingleTopicWithUpdatedFields(){
        Long id = topicRepository.findByName("Test").getId();
        String resourceURL = "http://localhost:" + port + "/api/topics/" + id;
        topic = new Topic("Test", "Update Description");
        HttpEntity<?> request = createHttpEntityWithTopic(topic);
        ResponseEntity<?> response = restTemplate.exchange(resourceURL, HttpMethod.PUT, request, Topic.class);

        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("name", "Test");
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("description", "Update Description");
    }

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
