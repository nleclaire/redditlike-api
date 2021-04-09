package com.sei.redditlikeapi.model;

import javax.persistence.*;

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
}
