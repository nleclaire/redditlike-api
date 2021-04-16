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
public class ArticleService {
    private UtilityService utility = new UtilityService();

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private TopicRepository topicRepository;

    public Article createArticle(Long topicId, Article articleObject) {
        User currentUser = utility.getAuthenticatedUser();
        Optional<Topic> topic = topicRepository.findById(topicId);
        if (topic.isPresent()) {
            if (articleRepository.existsByTitle(articleObject.getTitle())) {
                throw new InformationExistException("Article with title " + articleObject.getTitle() + " already exists!");
            } else {
                articleObject.setTopic(topic.get());
                articleObject.setUser(currentUser);
                return articleRepository.save(articleObject);
            }
        } else throw new InformationNotFoundException("Topic with ID " + topicId + " doesn't exist");
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
