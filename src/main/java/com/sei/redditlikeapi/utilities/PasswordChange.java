package com.sei.redditlikeapi.utilities;

// Class object used for changing the password
public class PasswordChange {
    private String emailAddress;
    private String oldPassword;
    private String newPassword;

    public PasswordChange() {
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public boolean isNotNull(){
        return this.oldPassword!=null && this.newPassword!=null ? true: false;
    }

    public String getEmailAddress() {
        return emailAddress;
    }
}
