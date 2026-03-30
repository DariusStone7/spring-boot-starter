package com.coop.api.modules.users.dto;

import com.coop.api.modules.users.entity.AccessRight;
import com.coop.api.modules.users.entity.Profile;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @NotNull
    private String name;
    @NotNull
    private String username;
    @NotNull @Email
    private String email;
    private String phone;
    @NotNull
    private String status;
    @NotNull
    private String password;
    @NotNull
    private Set<Profile> profiles = new HashSet<>();
    @NotNull
    private boolean isOtpEnabled;
}
