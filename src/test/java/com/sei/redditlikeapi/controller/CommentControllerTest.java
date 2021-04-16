package com.sei.redditlikeapi.controller;

import com.sei.redditlikeapi.model.Article;
import com.sei.redditlikeapi.model.Comment;
import com.sei.redditlikeapi.model.Topic;
import com.sei.redditlikeapi.repository.ArticleRepository;
import com.sei.redditlikeapi.repository.CommentRepository;
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

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.JVM) // https://www.baeldung.com/junit-5-test-order - necessary for following test order
public class CommentControllerTest {

    private Comment comment;
    private Topic topic;
    private Article article;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate; // using the restTemplate exchange method to send requests and receive response
    // https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/client/RestTemplate.html

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CommentRepository commentRepository;


    // Create HttpEntity saved my life
    // https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpEntity.html
    // Lets us spoof an HTTP request with Authorization and Content-Type headers
    private HttpEntity createHttpEntityWithComment(Comment comment){
        // set headers "Content-Type" : "application/json" and "Authorization" : "Bearer JWT_TOKEN"
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ2aWt0b3Iub2xlc25ldnljaEBob3RtYWlsLmNvbSIsImV4cCI6MTYxODYxMzIzNCwiaWF0IjoxNjE4NTc3MjM0fQ._Dmu8aP4ahDlA-NBG-ddPKSAWVe1m6S4GZA4BQHPajw");

        // return an HttpEntity with body of topic and headers
        return new HttpEntity<>(comment, headers);
    }

    // Another HttpEntity, to create generic request
    private HttpEntity createHttpRequest(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ2aWt0b3Iub2xlc25ldnljaEBob3RtYWlsLmNvbSIsImV4cCI6MTYxODYxMzIzNCwiaWF0IjoxNjE4NTc3MjM0fQ._Dmu8aP4ahDlA-NBG-ddPKSAWVe1m6S4GZA4BQHPajw");

        return new HttpEntity<>(headers);
    }

    // Create new Comment, and and POST it
    // Check to make sure that status code is 200, and response fields match that of the Test topic
    @Test
    public void postCommentShouldReturnOKForASingleCommentAndIsOfClassTypeComment(){
        topic = new Topic("Test", "Description");
        topicRepository.save(topic);
        article = new Article("TestArticle", "SomeArticle");
        article.setTopic(topic);
        articleRepository.save(article);
        String resourceURL = "http://localhost:" + port + "/api/topics/" + topic.getId() + "/articles/" + article.getId() +
                "/comments";
        comment = new Comment("Text description");
        HttpEntity<?> request = createHttpEntityWithComment(comment);
        ResponseEntity<?> response = restTemplate.exchange(resourceURL, HttpMethod.POST, request, Comment.class);
        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("textContent", "Text description");
    }

    // Find that same created Comment by textContent, and GET it
    // Check to make sure the Http status is 200 (OK) and that fields match the Test comment
    @Test
    public void getCommentShouldReturnASingleComment(){
        Long topicId = topicRepository.findByName("Test").getId();
        Long articleId = articleRepository.findByTitle("TestArticle").getId();
        Long id = commentRepository.findByTextContentAndArticleId("Text description", articleId).getId();

        String resourceURL = "http://localhost:" + port + "/api/topics/" + topicId + "/articles/" + articleId +
                "/comments/" + id;
        HttpEntity<?> request = createHttpRequest();
        ResponseEntity<?> response = restTemplate.exchange(resourceURL, HttpMethod.GET, request, Comment.class);

        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("textContent", "Text description");
    }

    // Find Test comment by textContent, and PUT some new info there
    // Check to make sure info matches updated information
    @Test
    public void putCommentShouldReturnASingleCommentWithUpdatedField(){
        Long topicId = topicRepository.findByName("Test").getId();
        Long articleId = articleRepository.findByTitle("TestArticle").getId();
        Long id = commentRepository.findByTextContentAndArticleId("Text description", articleId).getId();

        String resourceURL = "http://localhost:" + port + "/api/topics/" + topicId + "/articles/" + articleId +
                "/comments/" + id;
        comment = new Comment("Update description");
        HttpEntity<?> request = createHttpEntityWithComment(comment);
        ResponseEntity<?> response = restTemplate.exchange(resourceURL, HttpMethod.PUT, request, Comment.class);

        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("textContent", "Update description");
    }

    //    // Get Test comment by textContent, DELETE it
    //    // Verify that the response contains field "Response" as specified in controller
    @Test
    public void deleteCommentShouldReturnAResponseEntityWithFieldResponse(){
        Long topicId = topicRepository.findByName("Test").getId();
        Long articleId = articleRepository.findByTitle("TestArticle").getId();
        Long id = commentRepository.findByTextContentAndArticleId("Update description", articleId).getId();

        String resourceURL = "http://localhost:" + port + "/api/topics/" + topicId + "/articles/" + articleId +
                "/comments/" + id;
        HttpEntity<?> request = createHttpRequest();
        ResponseEntity<?> response = restTemplate.exchange(resourceURL, HttpMethod.DELETE, request, Object.class);

        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertThat(response.getBody()).hasFieldOrProperty("Response");

        topicRepository.deleteById(topicId);
    }
}


