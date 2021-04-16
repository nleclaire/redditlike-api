package com.sei.redditlikeapi.service;

import com.sei.redditlikeapi.exception.InformationExistException;
import com.sei.redditlikeapi.exception.InformationForbidden;
import com.sei.redditlikeapi.exception.InformationNotFoundException;
import com.sei.redditlikeapi.model.User;
import com.sei.redditlikeapi.utilities.PasswordChange;
import com.sei.redditlikeapi.utilities.UserAdminHint;
import com.sei.redditlikeapi.model.UserProfile;
import com.sei.redditlikeapi.model.request.LoginRequest;
import com.sei.redditlikeapi.model.response.LoginResponse;
import com.sei.redditlikeapi.repository.ProfileRepository;
import com.sei.redditlikeapi.repository.UserRepository;
import com.sei.redditlikeapi.security.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtils jwtUtils;

    UtilityService utility = new UtilityService();

    // CREATES NEW USER, PUBLIC ENDPOINT
    // ADMIN trick: "whoShotFirst" : "Han"
    public User createUser(UserAdminHint userObject) {
        if (!userRepository.existsByEmailAddress(userObject.getEmailAddress())) {
            if ((userRepository.existsByUserName(userObject.getUserName())))
                throw new InformationExistException("User with Username '" + userObject.getUserName() +
                        "' already exists");
            userObject.setPassword(passwordEncoder.encode(userObject.getPassword()));
            userObject.setPasswordChangedTime(new Date(System.currentTimeMillis()));
            if (userObject.getWhoShotFirst() == null)
                return userRepository.save(userObject.toUser(false));
            else if (userObject.getWhoShotFirst().equals("Han"))
                return userRepository.save(userObject.toUser(true));
            else
                return userRepository.save(userObject.toUser(false));
        } else
            throw new InformationExistException("User with email address: '" + userObject.getEmailAddress() +
                    "' already exists");
    }

    // DELETES EXISTING USER
    // ONLY ADMIN USER HAS ACCESS
    public void deleteUser(Long userId) {
        User currentUser = utility.getAuthenticatedUser();
        if (userRepository.findById(userId).isPresent())
            if (utility.isUserAdmin(currentUser))
                userRepository.deleteById(userId);
            else
                throw new InformationForbidden("You cannot delete users as you don't have admin role");
        else
            throw new InformationNotFoundException("User with ID " + userId + " not found!");
    }

    // UTILITY Method
    public User findUserByEmailAddress(String emailAddress) {
        return userRepository.findByEmailAddress(emailAddress);
    }

    // LOGINS USER
    public ResponseEntity<?> loginUser(LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmailAddress(), loginRequest.getPassword()));
            final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmailAddress());
            final String JWT = jwtUtils.generateToken(userDetails);
            return ResponseEntity.ok(new LoginResponse(JWT));
        } catch (NullPointerException e) {
            throw new InformationNotFoundException("User with that email address " + loginRequest.getEmailAddress()
                    + " not found!");
        }
    }

    // CREATES NEW USER PROFILE
    public User createProfile(UserProfile newProfile) {
        User currentUser = utility.getAuthenticatedUser();
        if (currentUser.getUserProfile() == null) {
            currentUser.setUserProfile(newProfile);
            return userRepository.save(currentUser);
        } else
            throw new InformationExistException("User profile for User with ID " + currentUser.getId() +
                    " already exists.");
    }

    // UPDATES EXISTING USER PROFILE, throw's an error if profile exists already
    public UserProfile updateProfile(UserProfile newProfile) {
        User currentUser = utility.getAuthenticatedUser();
        if (currentUser.getUserProfile() != null) {
            Long id = currentUser.getUserProfile().getId();
            newProfile.setId(id);
            return profileRepository.save(newProfile);
        } else
            throw new InformationNotFoundException("User profile doesn't exist for User with ID " +
                    currentUser.getId());
    }

    // Get CURRENT USER'S PROFILE
    public UserProfile getProfile() {
        User currentUser = utility.getAuthenticatedUser();
        if (currentUser.getUserProfile() != null)
            return profileRepository.findByUserId(currentUser.getId());
        else
            throw new InformationNotFoundException("User profile doesn't exist for User with ID " +
                    currentUser.getId());
    }

    // DELETES CURRENT USER PROFILE
    public void deleteProfile() {
        User currentUser = utility.getAuthenticatedUser();
        if (currentUser.getUserProfile() != null)
            profileRepository.deleteById(currentUser.getUserProfile().getId());
        else
            throw new InformationNotFoundException("User profile doesn't exist for User with ID " +
                    currentUser.getId());
    }

    // RETURNS THE LIST OF ALL USER PROFILES
    public List<UserProfile> getAllUserProfiles() {
        if (profileRepository.findAll().isEmpty())
            throw new InformationNotFoundException("No profiles set up yet!");
        return profileRepository.findAll();
    }

    // CHANGES OLD PASSWORD OF CURRENT USER TO NEW, PUBLIC ENDPOINT BUT REQUIRES email AND password
    public void changePassword(PasswordChange passInfo) {
        if (passInfo.isNotNull()) {
            User currentUser = userRepository.findByEmailAddress(passInfo.getEmailAddress());
            if (currentUser == null)
                throw new InformationNotFoundException("User with email address " + passInfo.getEmailAddress() +
                        " doesn't exist");
            else if (passwordEncoder.matches(passInfo.getOldPassword(), currentUser.getPassword())) {
                currentUser.setPassword(passwordEncoder.encode(passInfo.getNewPassword()));
                currentUser.setPasswordChangedTime(new Date(System.currentTimeMillis()));
                userRepository.save(currentUser);
            } else
                throw new InformationForbidden("Passwords do not match!");
        } else
            throw new InformationNotFoundException("Wrong input provided");
    }

    // RETURNS A LIST OF ALL USERS
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
