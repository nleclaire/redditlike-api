package com.sei.redditlikeapi.service;

import com.sei.redditlikeapi.exception.InformationExistException;
import com.sei.redditlikeapi.exception.InformationForbidden;
import com.sei.redditlikeapi.exception.InformationNotFoundException;
import com.sei.redditlikeapi.model.Article;
import com.sei.redditlikeapi.model.Topic;
import com.sei.redditlikeapi.model.User;
import com.sei.redditlikeapi.repository.ArticleRepository;
import com.sei.redditlikeapi.repository.TopicRepository;
import com.sei.redditlikeapi.security.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private ArticleRepository articleRepository;

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
        try {
            Topic topic = topicRepository.findById(topicId).get();
            topic.setName(topicObject.getName());
            topic.setDescription(topicObject.getDescription());
            topic.setUser(currentUser);
            return topicRepository.save(topic);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Topic with id " + topicId + " doesn't exist!");
        }
    }

    public void deleteTopic(Long topicId){
        Optional<Topic> topic = topicRepository.findById(topicId);
        if (topic.isEmpty())
            throw new InformationNotFoundException("Topic with id " + topicId + " doesn't exist!");
        else
            topicRepository.deleteById(topicId);
    }

    public Article createArticle(Long topicId, Article articleObject){
        User currentUser = getAuthenticatedUser();
        Topic topic = topicRepository.findById(topicId).get();
        Article article = articleRepository.findByIdAndTitle(articleObject.getId(), articleObject.getTitle());
        if (article != null) {
            throw new InformationExistException("Article with id " + articleObject.getId() + " already exists!");
        } else {
            articleObject.setTopic(topic);
            articleObject.setUser(currentUser);
            return articleRepository.save(articleObject);
        }
    }

    public List<Article> getArticles(Long topicId){
        List<Article> articles = articleRepository.findByTopicId(topicId);
        if (articles != null){
            return articles;
        } else {
            throw new InformationNotFoundException("Topic with Id " + topicId + " doesn't have any articles!");
        }
    }

    public Article getArticle(Long topicId, Long articleId){
        if (articleRepository.findByTopicIdAndId(topicId, articleId) != null) {
            return articleRepository.findByTopicIdAndId(topicId, articleId);
        } else {
            throw new InformationNotFoundException("Article with id " + articleId + " and topic id " + topicId + " not found!");
        }
    }



}
