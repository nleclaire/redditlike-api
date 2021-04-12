package com.sei.redditlikeapi.service;

import com.sei.redditlikeapi.exception.InformationExistException;
import com.sei.redditlikeapi.exception.InformationForbidden;
import com.sei.redditlikeapi.exception.InformationNotFoundException;
import com.sei.redditlikeapi.model.Article;
import com.sei.redditlikeapi.model.Topic;
import com.sei.redditlikeapi.model.User;
import com.sei.redditlikeapi.repository.ArticleRepository;
import com.sei.redditlikeapi.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TopicService {

    UtilityService utility = new UtilityService();

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private ArticleRepository articleRepository;

    public List<?> getTopics() {
        if (topicRepository.findAll().isEmpty()) {
            throw new InformationNotFoundException("No topics found!");
        } else
            return topicRepository.findAll();
    }

    public Topic createTopic(Topic topicObject) {
        User currentUser = utility.getAuthenticatedUser();
        if (utility.checkIfUserTopicExists(topicRepository, currentUser.getId(), topicObject.getName()))
            throw new InformationExistException("Topic with name " + topicObject.getName() +
                    " was already created by user: " + currentUser.getEmailAddress());
        else if (utility.checkIfTopicExists(topicRepository, topicObject.getName()))
            throw new InformationExistException("Topic with name " + topicObject.getName() + " already exists!");
        else {
            topicObject.setUser(currentUser);
            return topicRepository.save(topicObject);
        }
    }

    //TODO: Maybe check if user actually sets changes to the topic
    public Topic updateTopic(Long topicId, Topic topicObject) {
        User currentUser = utility.getAuthenticatedUser();
        if (topicRepository.findById(topicId).isPresent()) {
            if (utility.checkIfUserTopicExists(topicRepository, currentUser.getId(), topicId)) {
                Topic topic = topicRepository.findById(topicId).get();
                topic.setName(topicObject.getName());
                topic.setDescription(topicObject.getDescription());
                return topicRepository.save(topic);
            } else
                throw new InformationForbidden("You're not allowed to change data in topic with id " + topicId);
        }
        else
            throw new InformationNotFoundException("Topic with ID " + topicId + " doesn't exist");
    }

    public void deleteTopic(Long topicId) {
        Optional<Topic> topic = topicRepository.findById(topicId);
        if (topic.isEmpty())
            throw new InformationNotFoundException("Topic with id " + topicId + " doesn't exist!");
        else
            topicRepository.deleteById(topicId);
    }

    public Article createArticle(Long topicId, Article articleObject) {
        User currentUser = utility.getAuthenticatedUser();
        Optional<Topic> topic = topicRepository.findById(topicId);
        if (articleRepository.findByIdAndTitle(articleObject.getId(), articleObject.getTitle()) != null)
            throw new InformationExistException("Article with id " + articleObject.getId() + " already exists!");
        else
            articleObject.setTopic(topic.get());
        articleObject.setUser(currentUser);
        return articleRepository.save(articleObject);
    }

    public List<Article> getArticles(Long topicId) {
        List<Article> articles = articleRepository.findByTopicId(topicId);
        if (articles != null)
            return articles;
        else
            throw new InformationNotFoundException("Topic with Id " + topicId + " doesn't have any articles!");
    }

    public Article getArticle(Long topicId, Long articleId) {
        if (articleRepository.findByTopicIdAndId(topicId, articleId) != null)
            return articleRepository.findByTopicIdAndId(topicId, articleId);
        else
            throw new InformationNotFoundException("Article with id " + articleId + " and topic id " + topicId + " not found!");
    }

    // TODO: check whether current user is the user who created the article, or is an admin
    public Article updateArticle(Long topicId, Long articleId, Article articleObject) {
        List<Article> articles = this.getArticles(topicId);

        Article updateArticle = articles.stream().filter(a -> a.getId().equals(articleId)).findFirst().get();
        if (updateArticle == null)
            throw new InformationNotFoundException("Article with id " + articleId + " and topic id " + topicId + " not found!");
        else
            updateArticle.setTitle(articleObject.getTitle());
        updateArticle.setTextContent(articleObject.getTextContent());
        return articleRepository.save(updateArticle);
    }

    public void deleteArticle(Long topicId, Long articleId) {
        if (articleRepository.findByTopicIdAndId(topicId, articleId) == null)
            throw new InformationNotFoundException("Article with id " + articleId + " and topic id " + topicId + " not found!");
        else
            articleRepository.deleteById(articleId);
    }

}
