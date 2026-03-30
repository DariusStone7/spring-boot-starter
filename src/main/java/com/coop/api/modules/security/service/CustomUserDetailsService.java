package com.coop.api.modules.security.service;

import com.coop.api.modules.security.config.CustomUserDetails;
import com.coop.api.core.entity.User;
import com.coop.api.exceptions.ResourceNotFoundException;
import com.coop.api.modules.users.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @NonNull
    @Transactional
    public UserDetails loadUserByUsername(@NonNull String username) throws ResourceNotFoundException {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new ResourceNotFoundException(String.format("User %s not found", username));
        }

        return new CustomUserDetails(user);
    }

    @Transactional
    public User getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
                ("anonymousUser").equals(authentication.getPrincipal())) {
            throw new InternalAuthenticationServiceException("No user logged in");
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // Get refresh user data
        return userRepository.findById(userDetails.getUser().getId())
                .orElseThrow(() -> new InternalAuthenticationServiceException("User not found"));

    }
}
