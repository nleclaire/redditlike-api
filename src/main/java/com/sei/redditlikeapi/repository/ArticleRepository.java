package com.sei.redditlikeapi.repository;

import com.sei.redditlikeapi.model.Article;
import com.sei.redditlikeapi.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    Article findByIdAndTitle(Long articleId, String title);
    List<Article> findByTopicId(Long topicId);
    Article findByTopicIdAndId(Long topicId, Long articleId);
}
