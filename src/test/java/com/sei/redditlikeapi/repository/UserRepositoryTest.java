package com.sei.redditlikeapi.repository;

import com.sei.redditlikeapi.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@RunWith(SpringRunner.class)
@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp(){
        user = new User("username", "email@email.com", "pass");
        userRepository.save(user);
    }

    @AfterEach
    public void tearDown(){
        userRepository.deleteById(user.getId());
    }

    @Test
    void givenEmailReturnsTrueIfExists() {
        assertTrue(userRepository.existsByEmailAddress(user.getEmailAddress()));
    }

    @Test
    void givenUserNameReturnsTrueIfExists() {
        assertTrue(userRepository.existsByUserName(user.getUserName()));
    }

    @Test
    void givenEmailReturnsUser() {
        assertEquals(user.getId(), userRepository.findByEmailAddress(user.getEmailAddress()).getId());
    }
}
