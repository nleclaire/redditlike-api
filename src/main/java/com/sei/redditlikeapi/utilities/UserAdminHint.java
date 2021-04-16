package com.sei.redditlikeapi.utilities;

import com.sei.redditlikeapi.model.User;

// Class Object used for setting ADMIN user
public class UserAdminHint extends User {

    private String whoShotFirst;

    public UserAdminHint() {
    }

    public User toUser(Boolean admin){
        User returnUser = new User(this.getId(), this.getUserName(), this.getEmailAddress(), this.getPassword(),getPasswordChangedTime());
        returnUser.setAdmin(admin);
        return returnUser;
    }

    public String getWhoShotFirst() {
        return this.whoShotFirst;
    }
}
