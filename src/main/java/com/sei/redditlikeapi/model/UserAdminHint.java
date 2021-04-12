package com.sei.redditlikeapi.model;

public class UserAdminHint extends User{

   private String secretQuestion = null;


    public UserAdminHint() {
    }

    public User toUser(Boolean admin){
        User returnUser = new User(this.getId(), this.getUserName(), this.getEmailAddress(), this.getPassword());
        returnUser.setAdmin(admin);
        return returnUser;
    }

    public String getSecretQuestion() {
        return secretQuestion;
    }
}
