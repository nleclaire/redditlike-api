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

    public Topic getTopic(Long id) {
        if (topicRepository.findById(id).isEmpty()) {
            throw new InformationNotFoundException("Topic with ID " + id + " doesn't exist!");
        } else
            return topicRepository.findById(id).get();
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
            if (utility.checkIfUserTopicExists(topicRepository, currentUser.getId(), topicId)
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

    public Article createArticle(Long topicId, Article articleObject) {
        User currentUser = utility.getAuthenticatedUser();
        Optional<Topic> topic = topicRepository.findById(topicId);
        if (topic.isPresent()) {
            if (articleRepository.findByIdAndTitle(articleObject.getId(), articleObject.getTitle()) != null)
                throw new InformationExistException("Article with id " + articleObject.getId() + " already exists!");
            else {
                articleObject.setTopic(topic.get());
                articleObject.setUser(currentUser);
                return articleRepository.save(articleObject);
            }
        } else
            throw new InformationNotFoundException("Topic with ID " + topicId + " doesn't exist");
    }

    public List<Article> getArticles(Long topicId) {
        if (topicRepository.findById(topicId).isEmpty())
            throw new InformationNotFoundException("Topic with ID " + topicId + " doesn't exist");
        List<Article> articles = articleRepository.findByTopicId(topicId);
        if (articles != null)
            return articles;
        else
            throw new InformationNotFoundException("Topic with ID " + topicId + " doesn't have any articles!");
    }

    public Article getArticle(Long topicId, Long articleId) {
        if (topicRepository.findById(topicId).isEmpty())
            throw new InformationNotFoundException("Topic with ID " + topicId + " doesn't exist");
        if (articleRepository.findByTopicIdAndId(topicId, articleId) != null)
            return articleRepository.findByTopicIdAndId(topicId, articleId);
        else
            throw new InformationNotFoundException("Article with ID " + articleId + " in topic with  ID " +
                    topicId + " not found!");
    }

    // TODO: check whether current user is the user who created the article, or is an admin
    public Article updateArticle(Long topicId, Long articleId, Article articleObject) {
        User currentUser = utility.getAuthenticatedUser();
        if (topicRepository.findById(topicId).isEmpty())
            throw new InformationNotFoundException("Topic with ID " + topicId + " doesn't exist");
        List<Article> articles = this.getArticles(topicId);
        Article updateArticle = articles.stream().filter(a -> a.getId().equals(articleId)).findFirst().get();
        if (updateArticle == null)
            throw new InformationNotFoundException("Article with ID " + articleId + " and topic ID " + topicId + " not found!");
        else {
            if (updateArticle.getId() == articleRepository.findByIdAndUserId(articleId, currentUser.getId()).getId()
                    || utility.isUserAdmin(currentUser)) {
                updateArticle.setTitle(articleObject.getTitle());
                updateArticle.setTextContent(articleObject.getTextContent());
                return articleRepository.save(updateArticle);
            }
            else
                throw new InformationForbidden("You're not allowed to change article with ID " + articleId);
        }
    }

    public void deleteArticle(Long topicId, Long articleId) {
        User currentUser = utility.getAuthenticatedUser();
        if (topicRepository.findById(topicId).isEmpty())
            throw new InformationNotFoundException("Topic with ID " + topicId + " doesn't exist");
        if (articleRepository.findByTopicIdAndId(topicId, articleId) == null)
            throw new InformationNotFoundException("Article with ID " + articleId + " in topic with ID " + topicId +
                    " not found!");
        else {
            if (articleRepository.findById(articleId).get().getUser().getId() == currentUser.getId()
                    || utility.isUserAdmin(currentUser))
                articleRepository.deleteById(articleId);
            else
                throw new InformationForbidden("You're not allowed to delete article with ID " + articleId);
        }
    }
}
