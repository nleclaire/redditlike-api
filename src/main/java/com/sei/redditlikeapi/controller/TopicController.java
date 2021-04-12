package com.sei.redditlikeapi.controller;

import com.sei.redditlikeapi.model.Article;
import com.sei.redditlikeapi.model.Topic;
import com.sei.redditlikeapi.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class TopicController {
    @Autowired
    private TopicService topicService;

    //PUBLIC endpoint, everybody can have access
    @GetMapping("/topics")
    public List<?> getTopics(){
        return topicService.getTopics();
    }

    //PUBLIC endpoint, everybody can have access
    @GetMapping("/topics/{topicId}")
    public Topic getTopic(@PathVariable Long topicId){
        return topicService.getTopic(topicId);
    }

    //PRIVATE endpoint, registered users can have access
    @PostMapping("/topics/new")
    public Topic createTopic(@RequestBody Topic topicObject){
        return topicService.createTopic(topicObject);
    }

    //PRIVATE endpoint, registered users can have access to their own topics only
    @PutMapping("/topics/{topicId}")
    public Topic updateTopic(@PathVariable Long topicId, @RequestBody Topic topicObject){
        return topicService.updateTopic(topicId, topicObject);
    }

    //PRIVATE endpoint, registered users can have access to their own topics only
    @DeleteMapping("/topics/{topicId}")
    public ResponseEntity<HashMap> deleteTopic(@PathVariable Long topicId){
        topicService.deleteTopic(topicId);
        HashMap response = new HashMap();
        response.put("Response", "Topic with id " + topicId + " has been deleted");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/topics/{topicId}/articles")
    public Article createArticle(@PathVariable Long topicId, @RequestBody Article articleObject){
        return topicService.createArticle(topicId, articleObject);
    }

    @GetMapping("/topics/{topicId}/articles")
    public List<Article> getArticles(@PathVariable Long topicId){
        return topicService.getArticles(topicId);
    }

    @GetMapping("/topics/{topicId}/articles/{articleId}")
    public Article getArticle(@PathVariable Long topicId, @PathVariable Long articleId){
        return topicService.getArticle(topicId, articleId);
    }

    @PutMapping("/topics/{topicId}/articles/{articleId}")
    public Article updateArticle(@PathVariable Long topicId, @PathVariable Long articleId, @RequestBody Article articleObject){
        return topicService.updateArticle(topicId, articleId, articleObject);
    }

    @DeleteMapping("/topics/{topicId}/articles/{articleId}")
    public ResponseEntity<HashMap> deleteTopic(@PathVariable Long topicId, @PathVariable Long articleId){
        topicService.deleteArticle(topicId, articleId);
        HashMap response = new HashMap();
        response.put("Response", "Article with id " + articleId + " has been deleted");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
