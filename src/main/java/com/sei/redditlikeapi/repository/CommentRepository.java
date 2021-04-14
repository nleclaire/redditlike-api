package com.sei.redditlikeapi.repository;

import com.sei.redditlikeapi.model.Comment;
import com.sei.redditlikeapi.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByParentCommentIsNullAndArticleId(Long articleId);
}
