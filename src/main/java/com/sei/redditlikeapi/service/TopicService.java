package com.sei.redditlikeapi.service;

import com.sei.redditlikeapi.exception.InformationExistException;
import com.sei.redditlikeapi.exception.InformationForbidden;
import com.sei.redditlikeapi.exception.InformationNotFoundException;
import com.sei.redditlikeapi.model.Topic;
import com.sei.redditlikeapi.model.User;
import com.sei.redditlikeapi.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicService {

    private UtilityService utility = new UtilityService();

    @Autowired
    private TopicRepository topicRepository;

    // Return a list of topics if exists
    public List<?> getTopics() {
        if (topicRepository.findAll().isEmpty()) {
            throw new InformationNotFoundException("No topics found!");
        } else
            return topicRepository.findAll();
    }

    // Return a single topic if exists
    public Topic getTopic(Long id) {
        if (topicRepository.findById(id).isEmpty()) {
            throw new InformationNotFoundException("Topic with ID " + id + " doesn't exist!");
        } else
            return topicRepository.findById(id).get();
    }

    // Create a new topic if not exists already
    public Topic createTopic(Topic topicObject) {
        User currentUser = utility.getAuthenticatedUser();
        if (utility.checkIfUserTopicExists(topicRepository, currentUser.getId(), topicObject.getName()))
            throw new InformationExistException("Topic with name " + topicObject.getName() +
                    " was already created by user: " + currentUser.getEmailAddress());
        else if (utility.checkIfTopicExists(topicRepository, topicObject.getName()))
            throw new InformationExistException("Topic with name " + topicObject.getName() + " already exists!");
        else {
            topicObject.setUser(currentUser); // set current user as original poster
            return topicRepository.save(topicObject);
        }
    }

    // Check if topic exists, and verify current user
    // If user is either original poster OR admin, then update the topic
    // Otherwise, throw exception
    public Topic updateTopic(Long topicId, Topic topicObject) {
        User currentUser = utility.getAuthenticatedUser();
        if (topicRepository.findById(topicId).isPresent()) {
            if (utility.checkIfUserTopicExists(topicRepository, topicId, currentUser.getId())
                    || utility.isUserAdmin(currentUser)) {
                Topic topic = topicRepository.findById(topicId).get();
                topic.setName(topicObject.getName());
                topic.setDescription(topicObject.getDescription());
                return topicRepository.save(topic);
            } else
                throw new InformationForbidden("You're not allowed to change data in topic with id " + topicId);
        } else
            throw new InformationNotFoundException("Topic with ID " + topicId + " doesn't exist");
    }

    // Verify topic exists by Id
    // Verify user is either original poster OR is admin
    // Delete the topic, or throw exception
    public void deleteTopic(Long topicId) {
        User currentUser = utility.getAuthenticatedUser();
        if (topicRepository.findById(topicId).isPresent()) {
            if (utility.checkIfUserTopicExists(topicRepository, topicId, currentUser.getId())
                    || utility.isUserAdmin(currentUser))
                topicRepository.deleteById(topicId);
            else
                throw new InformationForbidden("You're not allowed to delete topic with id " + topicId);
        } else
            throw new InformationNotFoundException("Topic with ID " + topicId + " doesn't exist");
    }

}
