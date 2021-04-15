package com.sei.redditlikeapi;

import com.sei.redditlikeapi.model.Topic;
import org.junit.Test;
import org.junit.runner.Request;
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

    // Create HttpEntity saved my life
    // https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpEntity.html
    private HttpEntity createHttpRequestEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1bmNoLmxlY2xhaXJlQGVtYWlsLmNvbSIsImV4cCI6MTYxODQ4NzMyMSwiaWF0IjoxNjE4NDUxMzIxfQ.S31ExWI6k_rY9EdqYG1Wq1LS0-UtAuLtFH8NIcbnO2Q");

        Topic topic = new Topic(0L, "Test topic", "Test description");

        return new HttpEntity<>(topic, headers);
    }

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void getTopicShouldReturnASingleTopic(){
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/api/topics/1", Topic.class))
                .hasFieldOrProperty("name");
    }

    @Test
    public void postTopicShouldReturnASingleTopic(){
        String resourceURL = "http://localhost:" + port + "/api/topics/new";
        HttpEntity<Request> request = this.createHttpRequestEntity();
        ResponseEntity<?> response = restTemplate.exchange(resourceURL, HttpMethod.POST, request, Topic.class);

        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    }

    @Test
    public void putTopicShouldReturnASingleTopic(){
//        Topic updatedTopic = new Topic(0L, "Food", "Yum");
//        updatedTopic.setId(createResponse)
        assertThat(this.restTemplate.exchange("http://localhost:" + port + "/api/topics/1", HttpMethod.PUT, null, Void.class))
                .isNotNull(); // is HTTP ResponseEntity
    }

    @Test
    public void deleteTopicShouldReturnAResponseEntityWithFieldResponse(){
        assertThat(this.restTemplate.exchange("http://localhost:" + port + "/api/topics/1", HttpMethod.DELETE, null, Void.class)
                .getStatusCode().equals(HttpStatus.OK));
    }
}
