package com.spring_boot_starter.api.core.entity;

import com.spring_boot_starter.api.core.enums.UserStatus;
import com.spring_boot_starter.api.modules.security.entity.Otp;
import com.spring_boot_starter.api.modules.security.entity.RefreshToken;
import com.spring_boot_starter.api.modules.users.entity.AccessRight;
import com.spring_boot_starter.api.modules.users.entity.Profile;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(min = 3)
    private String name;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(unique = true)
    @Email
    private String email;
    @Column(unique = true)
    @Size(min = 9)
    private String phone;
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.ACTIVE;
    @Column(name = "is_otp_enabled")
    private Boolean isOtpEnabled = false;

    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_profiles", // Name of the join table
            joinColumns = @JoinColumn(name = "user_id"), // Column in join table referring to User
            inverseJoinColumns = @JoinColumn(name = "profile_id") // Column in join table referring to Profile
    )
    private Set<Profile> profiles = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_access_rights", // Name of the join table
            joinColumns = @JoinColumn(name = "user_id"), // Column in join table referring to User
            inverseJoinColumns = @JoinColumn(name = "access_right_id") // Column in join table referring to AccessRight
    )
    private Set<AccessRight> accessRights = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private RefreshToken refreshToken;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private Set <Otp> otp = new HashSet<>();

}
