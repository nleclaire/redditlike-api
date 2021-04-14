package com.sei.redditlikeapi.repository;

import com.sei.redditlikeapi.model.Topic;
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
class TopicRepositoryTest {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private UserRepository userRepository;

    private Topic topic;

    @BeforeEach
    public void setUp() {
        topic = new Topic("Name","Description");
    }

    @AfterEach
    public void tearDown() {
        topicRepository.deleteById(topic.getId());
        topic = null;
    }

    @Test
    public void givenTopicToAddShouldReturnAddedTopic(){
        topic = new Topic("Development","description");
        topicRepository.save(topic);
        Topic fetchedTopic = topicRepository.findById(topic.getId()).get();
        System.out.println("FETCHED TOPIC  " + fetchedTopic);
        assertEquals(topic.getId(), fetchedTopic.getId());
    }


    @Test
    void findByName() {
        String name = topic.getName();
        topicRepository.save(topic);
        assertEquals(name,topicRepository.findByName(name).getName());

    }

    @Test //Should at least be one user in users table
    void findByUserId() {
        User myUser = userRepository.findAll().get(0);
        topic.setUser(myUser);
        topicRepository.save(topic);
        assertEquals(topic.getId(), topicRepository.findByUserId(myUser.getId()).get(0).getId());
    }

    @Test
    void findByUserIdAndName() {
        String name = topic.getName();
        User myUser = userRepository.findAll().get(0);
        topic.setUser(myUser);
        topicRepository.save(topic);
        assertEquals(topic.getId(), topicRepository.findByUserIdAndName(myUser.getId(),name).getId());
    }

    @Test
    void findByIdAndUserId() {
        User myUser = userRepository.findAll().get(0);
        topic.setUser(myUser);
        topicRepository.save(topic);
        assertEquals(topic.getId(), topicRepository.findByIdAndUserId(topic.getId(), myUser.getId()).getId());
    }
}
