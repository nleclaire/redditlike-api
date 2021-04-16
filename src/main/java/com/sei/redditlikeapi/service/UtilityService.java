package com.sei.redditlikeapi.service;

import com.sei.redditlikeapi.exception.InformationForbidden;
import com.sei.redditlikeapi.exception.InformationNotFoundException;
import com.sei.redditlikeapi.model.User;
import com.sei.redditlikeapi.repository.TopicRepository;
import com.sei.redditlikeapi.security.MyUserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.context.SecurityContextHolder;

//Here different helpful methods will be stored which can be used all around the services
public class UtilityService {

    // Returns AUTHENTICATED through JWT Token User
    public User getAuthenticatedUser() {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser"))
            throw new InformationForbidden("Forbidden");
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails.getUser().isPasswordExpired())
            throw new InformationForbidden("Password is expired. Please, change your password");
        return userDetails.getUser();
    }

    // Checks if USER is ADMIN
    public boolean isUserAdmin(User user) {
        return user != null ? user.getIsAdmin() : false;
    }

    // Will check if TOPIC exists by NAME
    public boolean checkIfTopicExists(TopicRepository repository, String name) {
        return repository.findByName(name) != null ? true : false;
    }

    // Will check if TOPIC exists related to USER TOPICS by NAME
    public boolean checkIfUserTopicExists(TopicRepository repository, Long userId, String name) {
        return repository.findByUserIdAndName(userId, name) != null ? true : false;
    }

    // Will check if TOPIC exists related to USER TOPICS by ID
    public boolean checkIfUserTopicExists(TopicRepository repository, Long topicId, Long userId) {
        return repository.findByIdAndUserId(topicId, userId) != null ? true : false;
    }

    // Will throw an error if some data is set incorrectly
    public void errorIfRepositoryElementNotExistById(JpaRepository repository, Long id, String elementName) {
        if (repository.findById(id).isEmpty())
            throw new InformationNotFoundException(elementName + " with ID " + id + " doesn't exist!");
    }
}
