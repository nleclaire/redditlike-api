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
    private User user;

    //Should at least be one user in users table
    @BeforeEach
    public void setUp() {
        topic = new Topic("Name","Description");
        user = userRepository.findAll().get(0);
        topic.setUser(user);
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
    public void givenNameShouldReturnTopicWithThatName() {
        String name = topic.getName();
        topicRepository.save(topic);
        assertEquals(name,topicRepository.findByName(name).getName());

    }

    @Test
    public void givenUserIdShouldReturnListOfTopicsWithThatUserId() {
        topicRepository.save(topic);
        assertTrue(!topicRepository.findByUserId(user.getId()).isEmpty());
    }

    @Test
    public void givenUserIdAndNameShouldReturnAppropriateTopic() {
        String name = topic.getName();
        topicRepository.save(topic);
        assertEquals(topic.getId(), topicRepository.findByUserIdAndName(user.getId(),name).getId());
    }

    @Test
    public void givenTopicIdAndUserIdShouldReturnAppropriateTopic() {
        topicRepository.save(topic);
        assertEquals(topic.getId(), topicRepository.findByIdAndUserId(topic.getId(), user.getId()).getId());
    }
}
