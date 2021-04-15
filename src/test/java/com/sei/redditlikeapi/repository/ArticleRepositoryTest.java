package com.sei.redditlikeapi.repository;

import com.sei.redditlikeapi.model.Article;
import com.sei.redditlikeapi.model.Topic;
import com.sei.redditlikeapi.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@RunWith(SpringRunner.class)
@SpringBootTest
class ArticleRepositoryTest {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    private Topic topic;
    private Article article;
    private User user;

    @BeforeEach
    public void setUp() {
        user = userRepository.findAll().get(0);
        topic = new Topic("Name","Description");
        topicRepository.save(topic);
        article = new Article("Title","Text Content");
        article.setTopic(topic);
        article.setUser(user);
        articleRepository.save(article);
    }

    @AfterEach
    public void tearDown() {
        articleRepository.deleteById(article.getId());
        topicRepository.deleteById(topic.getId());
        article = null;
        topic = null;
    }

    @Test
    public void givenIdAndTitleShouldReturnArticle() {
        assertEquals(article.getId(), articleRepository.findByIdAndTitle(article.getId(),article.getTitle()).getId());
    }

    @Test
    public void givenTopicIdShouldReturnArticle() {
        assertEquals(article.getId(),articleRepository.findByTopicId(topic.getId()).get(0).getId());
    }

    @Test
    public void givenTopicIdAndArticleIdShouldReturnArticle() {
        assertEquals(article.getId(),articleRepository.findByTopicIdAndId(topic.getId(),article.getId()).getId());
    }

    @Test
    public void givenIdAndUserIdShouldReturnArticle() {
        assertEquals(article.getId(),articleRepository.findByIdAndUserId(article.getId(),user.getId()).getId());
    }
}
