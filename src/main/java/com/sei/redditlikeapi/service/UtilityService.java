package com.sei.redditlikeapi.service;

import com.sei.redditlikeapi.exception.TokenExpiredException;
import com.sei.redditlikeapi.model.User;
import com.sei.redditlikeapi.repository.TopicRepository;
import com.sei.redditlikeapi.security.MyUserDetails;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.core.context.SecurityContextHolder;

//Here different helpful methods will be stored which can be used all around the services
public class UtilityService {
    //Returns Authenticated through JWT Token User
    public User getAuthenticatedUser() {
            MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return userDetails.getUser();
    }

    //Will check if topic exists by name
    public boolean checkIfTopicExists(TopicRepository repository, String name){
        return repository.findByName(name) != null ? true : false;
    }

    //Will check if topic exists related to User topics by name
    public boolean checkIfUserTopicExists(TopicRepository repository, Long userId, String name){
        return repository.findByUserIdAndName(userId,name) != null ? true : false;
    }

    //Will check if topic exists related to User topics by id
    public boolean checkIfUserTopicExists(TopicRepository repository,Long topicId, Long userId){
        return repository.findByIdAndUserId(topicId,userId) != null ? true : false;
    }

}
