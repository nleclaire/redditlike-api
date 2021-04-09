package com.sei.redditlikeapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TopicController {

    @GetMapping("/helloworld")
    public String sayHello(){
        return "Hello World";
    }
}
