package com.sei.redditlikeapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;


import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="users")
public class User {
    private static final long PASSWORD_EXPIRATION_TIME
            =   60L * 1000L;    // 30 days

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String userName;

    @Column(unique = true)
    private String emailAddress;

    @Column
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)  // only allowed to write, not read
    private String password;

    @Column(name = "password_changed_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date passwordChangedTime;

    @Column
    private boolean isAdmin = false;

    //User has single profile
    @OneToOne(cascade = CascadeType.ALL) //Fetch the record from user profile
    @JoinColumn(name = "profile_id", referencedColumnName = "id") //LIKE THIS BECAUSE OWNING SIDE
    private UserProfile userProfile;

    @OneToMany(mappedBy = "user", orphanRemoval = true) //User can have more than one topic
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Topic> topicList;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Article> articleList;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Comment> commentList;

    public User() {
    }


    public User(String userName, String emailAddress, String password) {
        this.userName = userName;
        this.emailAddress = emailAddress;
        this.password = password;
        this.isAdmin = false;
    }

    public User(Long id, String userName, String emailAddress, String password, Date passwordChangedTime) {
        this.id = id;
        this.userName = userName;
        this.emailAddress = emailAddress;
        this.password = password;
        this.passwordChangedTime = passwordChangedTime;
        this.isAdmin = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

//    public List<Topic> getTopicList() {
//        return topicList;
//    }

// //   public List<Article> getArticleList() {
//        return articleList;
//    }

//    public List<Comment> getCommentList() {
//        return commentList;
//    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        this.isAdmin = admin;
    }

    public boolean isPasswordExpired() {
        if (this.passwordChangedTime == null) return false;

        long currentTime = System.currentTimeMillis();
        long lastChangedTime = this.passwordChangedTime.getTime();

        return currentTime > lastChangedTime + PASSWORD_EXPIRATION_TIME;
    }

    public void setPasswordChangedTime(Date passwordChangedTime) {
        this.passwordChangedTime = passwordChangedTime;
    }

    public Date getPasswordChangedTime() {
        return passwordChangedTime;
    }
}
