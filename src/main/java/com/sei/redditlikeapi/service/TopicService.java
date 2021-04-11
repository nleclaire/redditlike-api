package com.sei.redditlikeapi.service;

import com.sei.redditlikeapi.exception.InformationExistException;
import com.sei.redditlikeapi.exception.InformationForbidden;
import com.sei.redditlikeapi.exception.InformationNotFoundException;
import com.sei.redditlikeapi.model.Topic;
import com.sei.redditlikeapi.model.User;
import com.sei.redditlikeapi.repository.TopicRepository;
import com.sei.redditlikeapi.security.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Topic updateTopic(Long topicId, Topic topicObject){
        User currentUser = getAuthenticatedUser();
        Topic topic = topicRepository.findById(topicId).get();
        if (topic == null){
            throw new InformationNotFoundException("Topic with id " + topicId + " doesn't exist!");
        } else {
            topic.setName(topicObject.getName());
            topic.setDescription(topicObject.getDescription());
            topic.setUser(currentUser);
            return topicRepository.save(topic);
        }
    }
}
