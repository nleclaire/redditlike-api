package com.sei.redditlikeapi.controller;

import com.sei.redditlikeapi.model.Topic;
import com.sei.redditlikeapi.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
