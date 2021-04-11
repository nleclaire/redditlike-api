package com.sei.redditlikeapi.controller;

import com.sei.redditlikeapi.model.Topic;
import com.sei.redditlikeapi.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/topics/new")
    public Topic createTopic(@RequestBody Topic topicObject){
        return topicService.createTopic(topicObject);
    }

    @PutMapping("/topics/{topicId}")
    public Topic updateTopic(@PathVariable Long topicId, @RequestBody Topic topicObject){
        return topicService.updateTopic(topicId, topicObject);
    }

    @DeleteMapping("/topics/{topicId}")
    public ResponseEntity<HashMap> deleteTopic(@PathVariable Long topicId){
        topicService.deleteTopic(topicId);
        HashMap response = new HashMap();
        response.put("Response", "Topic with id " + topicId + " has been deleted");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



}
