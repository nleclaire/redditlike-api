package com.sei.redditlikeapi.repository;


import com.sei.redditlikeapi.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<UserProfile, Long> {
    //to register
    UserProfile findByUserId(Long id);
}
