package com.sei.redditlikeapi.repository;

import com.sei.redditlikeapi.model.Article;
import com.sei.redditlikeapi.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    Article findByIdAndTitle(Long articleId, String title);
}
