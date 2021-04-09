package com.sei.redditlikeapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="topics")
public class Comment {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String textContent;

    @Column
    private LocalDate dateCreated;

    @JsonIgnore // Always in the opposite side of the mapping
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @JsonIgnore // Always in the opposite side of the mapping
    @ManyToOne
    @JoinColumn(name="article_id")
    private Article article;


    public Comment() {
    }

    public Comment(Long id, String title, String textContent) {
        this.id = id;
        this.textContent = textContent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String description) {
        this.textContent = description;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", textContent='" + textContent + '\'' +
                ", dateCreated=" + dateCreated +
                '}';
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
