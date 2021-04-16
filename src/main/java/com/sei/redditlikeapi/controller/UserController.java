package com.sei.redditlikeapi.controller;
import com.sei.redditlikeapi.exception.InformationNotFoundException;
import com.sei.redditlikeapi.model.User;
import com.sei.redditlikeapi.utilities.PasswordChange;
import com.sei.redditlikeapi.utilities.UserAdminHint;
import com.sei.redditlikeapi.model.UserProfile;
import com.sei.redditlikeapi.model.request.LoginRequest;
import com.sei.redditlikeapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

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

    @PutMapping("/users/profile")
    public UserProfile updateProfile(@RequestBody UserProfile newProfile) {
        return userService.updateProfile(newProfile);
    }

    @GetMapping("/users/profile")
    public UserProfile getProfile(){
        return userService.getProfile();
    }

    @DeleteMapping("/users/profile")
    @Transactional
    public ResponseEntity<?> deleteProfile(){
        userService.deleteProfile();
        HashMap responseMessage = new HashMap();
        responseMessage.put("Status", "User profile was successfully deleted");
        return new ResponseEntity<HashMap>(responseMessage, HttpStatus.OK);
    }

    @GetMapping("/users/profile/all")
    public List<UserProfile> getAllUserProfiles(){
        return userService.getAllUserProfiles();
    }

    @PutMapping("/users/login/changepassword")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChange newPassword) {
        userService.changePassword(newPassword);
        HashMap responseMessage = new HashMap();
        responseMessage.put("Status", "Password was successfully changed");
        return new ResponseEntity<HashMap>(responseMessage, HttpStatus.OK);
    }

    @GetMapping("/users")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }
}
