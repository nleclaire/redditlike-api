package com.sei.redditlikeapi.service;

import com.sei.redditlikeapi.exception.InformationExistException;
import com.sei.redditlikeapi.exception.InformationForbidden;
import com.sei.redditlikeapi.exception.InformationNotFoundException;
import com.sei.redditlikeapi.model.User;
import com.sei.redditlikeapi.model.UserAdminHint;
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

    // ADMIN : "secretQuestion" : "some answer"
    public User createUser(UserAdminHint userObject) {
        if (!userRepository.existsByEmailAddress(userObject.getEmailAddress())) {
            if ((userRepository.existsByUserName(userObject.getUserName())))
                throw new InformationExistException("User with Username '" + userObject.getUserName() +
                        "' already exists");
            userObject.setPassword(passwordEncoder.encode(userObject.getPassword()));
            System.out.println("DATE" + new Date(System.currentTimeMillis()));
            userObject.setPasswordChangedTime(new Date(System.currentTimeMillis()));
            System.out.println("DATE 2: " + userObject.getPasswordChangedTime());
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

    // using to test if deletes user
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

    public User findUserByEmailAddress(String emailAddress) {
        return userRepository.findByEmailAddress(emailAddress);
    }

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

    public User createProfile(UserProfile newProfile) {
        User currentUser = utility.getAuthenticatedUser();
        if (currentUser.getUserProfile() == null) {
            currentUser.setUserProfile(newProfile);
            return userRepository.save(currentUser);
        } else
            throw new InformationExistException("User profile for User with ID " + currentUser.getId() +
                    " already exists.");
    }

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

    public User changePassword(String newPassword) {
        User currentUser = utility.getAuthenticatedUser();
        String encodedPassword = passwordEncoder.encode(newPassword);
        currentUser.setPassword(encodedPassword);
        currentUser.setPasswordChangedTime(new Date(System.currentTimeMillis()));
        return userRepository.save(currentUser);
    }
}
