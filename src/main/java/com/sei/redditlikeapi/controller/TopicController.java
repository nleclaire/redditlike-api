package com.sei.redditlikeapi.controller;

import com.sei.redditlikeapi.model.Topic;
import com.sei.redditlikeapi.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TopicController {
    private TopicRepository topicRepository;

    @Autowired
    public void setTopicRepository(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    @GetMapping("/topics/")
    public List<Topic> getTopics(){
        return topicRepository.findAll();
    }

    @PostMapping("/topics")
    public Topic getTopics(@RequestBody Topic topicObject){
        return topicRepository.save(topicObject);
    }
}
