package com.sei.redditlikeapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="topics")
public class Article {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String textContent;

    @OneToMany(mappedBy = "article", orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Comment> commentList;

    @JsonIgnore // Always in the opposite side of the mapping
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @JsonIgnore // To prevent StackOverflow when calling each other
    @ManyToOne
    @JoinColumn(name="topic_id")
    private Topic topic;


    public Article() {
    }

    public Article(Long id, String title, String textContent) {
        this.id = id;
        this.title = title;
        this.textContent = textContent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String description) {
        this.textContent = description;
    }

    @Override
    public String toString() {
        return "Articles{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", textContent='" + textContent + '\'' +
                '}';
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }


    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }
}
