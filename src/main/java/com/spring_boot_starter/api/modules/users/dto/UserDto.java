package com.spring_boot_starter.api.modules.users.dto;

import com.spring_boot_starter.api.modules.users.entity.Profile;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record UserDto (
        @NotNull String name,
        @NotNull String username,
        @NotNull @Email String email,
        String phone,
        @NotNull String status,
        @NotNull String password,
        @NotNull Set<Profile> profiles,
        @NotNull boolean isOtpEnabled
){}
