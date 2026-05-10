package com.spring_boot_starter.api.modules.security.entity;

import com.spring_boot_starter.api.core.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "otp")
public class Otp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false)
    private String otp_hash;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(nullable = false)
    private int attempts = 0;

    @Column(nullable = false)
    private boolean used;

    @Column(name = "created_at")
    private Instant createdAt;

    public void incrementAttempt() {
        this.attempts += 1;
    }

    @Override
    public String toString() {
        return String.format("OTP { id = %d, user = %s, otp_hash = %s, expires_at = %s, attempts = %d, used = %b, created_at = %s }", id, user.getUsername(), otp_hash, expiresAt, attempts, used, createdAt);
    }
}
