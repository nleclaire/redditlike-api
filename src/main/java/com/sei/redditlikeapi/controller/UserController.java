package com.sei.redditlikeapi.controller;
import com.sei.redditlikeapi.model.User;
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
    public User createUser(@RequestBody User userObject){
        System.out.println("===> Create User executed");
        return userService.createUser(userObject);
    }

    @DeleteMapping("/users/delete/{userId}")
    public ResponseEntity<HashMap> deleteUser(@PathVariable Long userId) {
        System.out.println("===> Delete User Executed");
        userService.deleteUser(userId);
        HashMap responseMessage = new HashMap();
        responseMessage.put("Status", "User with ID " + userId + " was successfully deleted.");
        return new ResponseEntity<HashMap>(responseMessage, HttpStatus.BAD_GATEWAY);
    }

    @PostMapping("/users/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest){
        System.out.println("calling loginUser");
        return userService.loginUser(loginRequest);
    }

    @PostMapping("/api/profile")
    public User createProfile(@RequestBody UserProfile newProfile) {
        System.out.println("calling createProfile ==>");
        return userService.createProfile(newProfile);
    }

    @PutMapping("/api/profile/update")
    public UserProfile updateProfile(@RequestBody UserProfile newProfile) {
        System.out.println("calling createProfile ==>");
        return userService.updateProfile(newProfile);
    }
}
