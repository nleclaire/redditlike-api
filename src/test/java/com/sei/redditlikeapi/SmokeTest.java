package com.sei.redditlikeapi;

import com.sei.redditlikeapi.controller.ArticleController;
import com.sei.redditlikeapi.controller.CommentController;
import com.sei.redditlikeapi.controller.TopicController;
import com.sei.redditlikeapi.controller.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SmokeTest {

    @Autowired
    private TopicController topicController;

    @Autowired
    private ArticleController articleController;

    @Autowired
    private CommentController commentController;

    @Autowired
    private UserController userController;

    @Test
    public void contextLoads() {
        assertThat(topicController).isNotNull();
        assertThat(articleController).isNotNull();
        assertThat(commentController).isNotNull();
        assertThat(userController).isNotNull();
    }

}
