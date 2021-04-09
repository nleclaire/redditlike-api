package com.sei.redditlikeapi.service;

import com.sei.redditlikeapi.exception.InformationNotFoundException;
import com.sei.redditlikeapi.model.Topic;
import com.sei.redditlikeapi.model.User;
import com.sei.redditlikeapi.repository.TopicRepository;
import com.sei.redditlikeapi.security.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
        User currentUser = getAuthenticatedUser();
        List<Topic> categories = topicRepository.findByUserId(currentUser.getId());
        if (categories.isEmpty()) {
            throw new InformationNotFoundException("No categories found for that user Id " + currentUser.getId());
        } else
            return categories;
    }
}
