package com.sei.redditlikeapi.service;

import com.sei.redditlikeapi.exception.InformationExistException;
import com.sei.redditlikeapi.exception.InformationNotFoundException;
import com.sei.redditlikeapi.model.User;
import com.sei.redditlikeapi.model.UserProfile;
import com.sei.redditlikeapi.model.request.LoginRequest;
import com.sei.redditlikeapi.model.response.LoginResponse;
import com.sei.redditlikeapi.repository.ProfileRepository;
import com.sei.redditlikeapi.repository.UserRepository;
import com.sei.redditlikeapi.security.JWTUtils;
import com.sei.redditlikeapi.security.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public User createUser(User userObject){
        System.out.println(" ===> SERVICE: Create User Executed ");
        if (!userRepository.existsByEmailAddress(userObject.getEmailAddress())){
            if ((userRepository.existsByUserName(userObject.getUserName())))
                throw new InformationExistException("User with Username '" + userObject.getUserName() +
                        "' already exists");
            userObject.setPassword(passwordEncoder.encode(userObject.getPassword()));
            return userRepository.save(userObject);
        }
        else
            throw new InformationExistException("User with email address: '" + userObject.getEmailAddress() +
                    "' already exists");
    }

    // using to test if deletes user
    public void deleteUser(Long userId){
        System.out.println(" ===> SERVICE: Delete User Executed ");
        if (userRepository.findById(userId).isPresent())
            userRepository.deleteById(userId);
        else
            throw new InformationNotFoundException("User with ID " + userId + " not found!");
    }

    public User findUserByEmailAddress(String emailAddress){
        return userRepository.findByEmailAddress(emailAddress);
    }

    public ResponseEntity<?> loginUser(LoginRequest loginRequest){
        System.out.println(" ===> SERVICE: loginUser executed");
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmailAddress(), loginRequest.getPassword()));
            final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmailAddress());
            final String JWT = jwtUtils.generateToken(userDetails);
            return ResponseEntity.ok(new LoginResponse(JWT));
        }catch (NullPointerException e){
            throw new InformationNotFoundException("User with that email address " + loginRequest.getEmailAddress()
            + " not found!");
        }
    }

    public User createProfile(UserProfile newProfile){
        System.out.println(" ===> SERVICE: createProfile executed");
        MyUserDetails userDetails =
                (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails.getUser().getUserProfile() == null) {
            User user = userDetails.getUser();
            user.setUserProfile(newProfile);
            return userRepository.save(user);
        }
        else
            throw new InformationExistException("User profile for User with ID " + userDetails.getUser().getId() +
                    " already exists.");
    }

    public UserProfile updateProfile(UserProfile newProfile){
        System.out.println(" ===> SERVICE: updateProfile executed");
        MyUserDetails userDetails =
                (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails.getUser().getUserProfile() != null) {
            Long id = userDetails.getUser().getUserProfile().getId();
            newProfile.setId(id);
            return profileRepository.save(newProfile);
        }
        else
            throw new InformationNotFoundException("User profile doesn't exist for User with ID " +
                    userDetails.getUser().getId());
    }
}
