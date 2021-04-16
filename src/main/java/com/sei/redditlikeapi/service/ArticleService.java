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

    // Get current user, try to find topic by Id.
    // If topic is not present, throw an exception
    // If article title exists already, throw exception. Otherwise set article's topic and user
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

    // Return a list of articles if topic exists and list is not null
    public List<Article> getArticles(Long topicId) {
        if (topicRepository.findById(topicId).isEmpty())
            throw new InformationNotFoundException("Topic with ID " + topicId + " doesn't exist");
        List<Article> articles = articleRepository.findByTopicId(topicId);
        if (articles != null)
            return articles;
        else
            throw new InformationNotFoundException("Topic with ID " + topicId + " doesn't have any articles!");
    }

    // Return a single article if and only if topic and article Ids can be found
    public Article getArticle(Long topicId, Long articleId) {
        if (topicRepository.findById(topicId).isEmpty())
            throw new InformationNotFoundException("Topic with ID " + topicId + " doesn't exist");
        if (articleRepository.findByTopicIdAndId(topicId, articleId) != null)
            return articleRepository.findByTopicIdAndId(topicId, articleId);
        else
            throw new InformationNotFoundException("Article with ID " + articleId + " in topic with  ID " +
                    topicId + " not found!");
    }

    // Verify topic exists, then get article by topicId and articleId
    // Allow user to update article if they are the original user OR they are an admin
    public Article updateArticle(Long topicId, Long articleId, Article articleObject) {
        User currentUser = utility.getAuthenticatedUser();
        if (topicRepository.findById(topicId).isEmpty())
            throw new InformationNotFoundException("Topic with ID " + topicId + " doesn't exist");
        Article updateArticle = articleRepository.findByTopicIdAndId(topicId, articleId);
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

    // Verify topic and article exist
    // Delete article only if user is original poster OR user is admin
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
