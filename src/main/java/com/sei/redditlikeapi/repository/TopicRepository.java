package com.sei.redditlikeapi.repository;

import com.sei.redditlikeapi.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, Long> {

}
