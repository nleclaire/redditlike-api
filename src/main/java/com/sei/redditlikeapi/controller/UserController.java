package com.sei.redditlikeapi.controller;
import com.sei.redditlikeapi.model.User;
import com.sei.redditlikeapi.utilities.PasswordChange;
import com.sei.redditlikeapi.utilities.UserAdminHint;
import com.sei.redditlikeapi.model.UserProfile;
import com.sei.redditlikeapi.model.request.LoginRequest;
import com.sei.redditlikeapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/auth")
public class UserController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/users/register")
    public User createUser(@RequestBody UserAdminHint userObject){
        return userService.createUser(userObject);
    }

    @DeleteMapping("/users/delete/{userId}")
    public ResponseEntity<HashMap> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        HashMap responseMessage = new HashMap();
        responseMessage.put("Status", "User with ID " + userId + " was successfully deleted.");
        return new ResponseEntity<HashMap>(responseMessage, HttpStatus.OK);
    }

    @PostMapping("/users/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest){
        return userService.loginUser(loginRequest);
    }

    @PostMapping("/users/profile")
    public User createProfile(@RequestBody UserProfile newProfile) {
        return userService.createProfile(newProfile);
    }

    @PutMapping("/users/profile/update")
    public UserProfile updateProfile(@RequestBody UserProfile newProfile) {
        return userService.updateProfile(newProfile);
    }

    @PutMapping("/users/login/changepassword")
    public User changePassword(@RequestBody PasswordChange newPassword) {
        return userService.changePassword(newPassword);
    }
}
