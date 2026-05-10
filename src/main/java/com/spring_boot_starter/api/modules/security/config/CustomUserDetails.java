package com.spring_boot_starter.api.modules.security.config;

import com.spring_boot_starter.api.core.entity.User;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();

        // add profile role and access rights
        user.getProfiles().forEach(profile -> {
            // add profile role
            authorities.add(new SimpleGrantedAuthority("ROLE_" + profile.getLabel().toUpperCase()));

            // add profile access rights
            profile.getAccessRights().forEach(accessRight -> {
                authorities.add(new SimpleGrantedAuthority(accessRight.getLabel()));
            });
        });

        // add user access rights
        user.getAccessRights().forEach(accessRight -> {
            authorities.add(new SimpleGrantedAuthority(accessRight.getLabel()));
        });

        return authorities;
    }

    @Override
    public @Nullable String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return "ACTIVE".equals(user.getStatus().name());
    }

    public User getUser() {
        return user;
    }
}
