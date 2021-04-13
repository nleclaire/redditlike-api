package com.sei.redditlikeapi.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class ArticleControllerTest {
    @Autowired
    private ArticleController controller;

    @Test
    public void contextLoads(){
        assertThat(controller).isNotNull();
    }

}
