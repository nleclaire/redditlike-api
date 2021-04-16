package com.sei.redditlikeapi.controller;

import com.sei.redditlikeapi.model.Article;
import com.sei.redditlikeapi.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/topics/{topicId}")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping("/articles")
    public Article createArticle(@PathVariable Long topicId, @RequestBody Article articleObject){
        return articleService.createArticle(topicId, articleObject);
    }

    @GetMapping("/articles")
    public List<Article> getArticles(@PathVariable Long topicId){
        return articleService.getArticles(topicId);
    }

    @GetMapping("/articles/{articleId}")
    public Article getArticle(@PathVariable Long topicId, @PathVariable Long articleId){
        return articleService.getArticle(topicId, articleId);
    }

    @PutMapping("/articles/{articleId}")
    public Article updateArticle(@PathVariable Long topicId, @PathVariable Long articleId, @RequestBody Article articleObject){
        return articleService.updateArticle(topicId, articleId, articleObject);
    }

    @DeleteMapping("/articles/{articleId}")
    public ResponseEntity<HashMap> deleteTopic(@PathVariable Long topicId, @PathVariable Long articleId){
        articleService.deleteArticle(topicId, articleId);
        HashMap response = new HashMap();
        response.put("Response", "Article with id " + articleId + " has been deleted");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
