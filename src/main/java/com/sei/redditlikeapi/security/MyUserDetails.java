package com.sei.redditlikeapi.security;


import com.sei.redditlikeapi.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;

public class MyUserDetails implements UserDetails {
    private User user;

    public MyUserDetails(User user) {
        this.user = user;
    }


    // Returns the authorities granted to the user.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new HashSet<GrantedAuthority>();
    }

    // Returns the password used to authenticate the user.
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // Returns the username used to authenticate the user.
    @Override
    public String getUsername() {
        return user.getEmailAddress();
    }

    // Indicates whether the user's account has expired.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // Indicates whether the user is locked or unlocked.
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // Indicates whether the user's credentials (password) has expired.
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Indicates whether the user is enabled or disabled.
    @Override
    public boolean isEnabled() {
        return true;
    }

    public User getUser() {
        return user;
    }
}
