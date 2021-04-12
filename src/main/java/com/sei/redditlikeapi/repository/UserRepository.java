package com.sei.redditlikeapi.repository;

import com.sei.redditlikeapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //to register
    boolean existsByEmailAddress(String userEmailAddress);
    boolean existsByUserName(String userName);
    User findByEmailAddress(String userEmailAddress);
}
