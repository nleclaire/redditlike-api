package com.sei.redditlikeapi.repository;

import com.sei.redditlikeapi.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    List<Topic> findByUserId(Long id);
    Topic findByName(String name);
    Topic findByUserIdAndName(Long id, String name);
    Topic findByIdAndUserId(Long topicId, Long userId);
}
