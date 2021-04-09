package com.sei.redditlikeapi.service;

import com.sei.redditlikeapi.exception.InformationExistException;
import com.sei.redditlikeapi.exception.InformationNotFoundException;
import com.sei.redditlikeapi.model.Topic;
import com.sei.redditlikeapi.model.User;
import com.sei.redditlikeapi.repository.TopicRepository;
import com.sei.redditlikeapi.security.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    public User getAuthenticatedUser() {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUser();
    }

    public List<?> getTopics(){
        if (topicRepository.findAll().isEmpty()) {
            throw new InformationNotFoundException("No topics found!");
        } else
            return topicRepository.findAll();
    }

    public Topic createTopic(Topic topicObject){
        User currentUser = getAuthenticatedUser();
        Topic topic = topicRepository.findByUserIdAndName(currentUser.getId(),
                topicObject.getName());
        if (topic != null)
            throw new InformationExistException("Topic with name " + topic.getName() + " already exists!");
        else {
            topicObject.setUser(currentUser); // to set the user owner to new topic
            return topicRepository.save(topicObject);
        }
    }
}
