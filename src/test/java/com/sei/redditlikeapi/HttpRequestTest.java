package com.sei.redditlikeapi;

import com.sei.redditlikeapi.model.Topic;
import com.sei.redditlikeapi.repository.TopicRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.*;

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

    @BeforeEach
    public void setUp() {
        postTopicShouldReturnOKForASingleTopicAndIsOfClassTypeTopic();
    }

    @AfterEach
    public void tearDown() {
        topicRepository.deleteById(topic.getId());
    }

    // Create HttpEntity saved my life
    // https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpEntity.html
    private HttpEntity createHttpEntityWithTopic(Topic topic){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1bmNoLmxlY2xhaXJlQGVtYWlsLmNvbSIsImV4cCI6MTYxODUyOTI2NywiaWF0IjoxNjE4NDkzMjY3fQ.OoaL2Opo-QAS8DNcS85vtgfFFZBCe1cz3VYd02Hleec");

        return new HttpEntity<>(topic, headers);
    }

    private HttpEntity createHttpRequest(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1bmNoLmxlY2xhaXJlQGVtYWlsLmNvbSIsImV4cCI6MTYxODUyOTI2NywiaWF0IjoxNjE4NDkzMjY3fQ.OoaL2Opo-QAS8DNcS85vtgfFFZBCe1cz3VYd02Hleec");

        return new HttpEntity<>(headers);
    }

    @Test
    public void postTopicShouldReturnOKForASingleTopicAndIsOfClassTypeTopic(){
        String resourceURL = "http://localhost:" + port + "/api/topics/new";
//        topic = new Topic("Test", "Test description");

        HttpEntity<?> request = createHttpEntityWithTopic(topic);
        ResponseEntity<?> response = restTemplate.exchange(resourceURL, HttpMethod.POST, request, Topic.class);

//        id = topic.getId();

        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("name", "Test");
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("description", "Test description");
//        assertThat(response.getBody()).hasSameClassAs(topic);
    }


    @Test
    public void getTopicShouldReturnASingleTopic(){
//        id = topicRepository.findByName("Test").getId();
        System.out.println(topic);

        String resourceURL = "http://localhost:" + port + "/api/topics/" + topic.getId();
        HttpEntity<?> request = createHttpRequest();
        ResponseEntity<?> response = restTemplate.exchange(resourceURL, HttpMethod.GET, request, Topic.class);

        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("name", "Test");
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("description", "Test description");

    }

    @Test
    public void putTopicShouldReturnASingleTopicWithUpdatedFields(){
//        id = topicRepository.findByName("Test").getId();

        String resourceURL = "http://localhost:" + port + "/api/topics/" + topic.getId();

        topic = new Topic("Update Test", "Update description");
        HttpEntity<?> request = createHttpEntityWithTopic(topic);
        ResponseEntity<?> response = restTemplate.exchange(resourceURL, HttpMethod.PUT, request, Topic.class);
//        topicRepository.save(topic);

//        topic.setId(restTemplate.exchange(resourceURL, HttpMethod.GET, request, Topic.class).getBody().getId());

        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("name", "Update Test");
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("description", "Update description");
    }

    @Test
    public void deleteTopicShouldReturnAResponseEntityWithFieldResponse(){
//        id = topicRepository.findByName("Update Test").getId();

        String resourceURL = "http://localhost:" + port + "/api/topics/" + topic.getId();
        HttpEntity<?> request = createHttpEntityWithTopic(topic);
        ResponseEntity<?> response = restTemplate.exchange(resourceURL, HttpMethod.DELETE, request, Topic.class);
        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertThat(response.getBody()).hasFieldOrProperty("response");
    }
}
