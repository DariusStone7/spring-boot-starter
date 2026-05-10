package com.spring_boot_starter.api.modules.security.repository;

import com.spring_boot_starter.api.core.entity.User;
import com.spring_boot_starter.api.modules.security.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUser(User user);

    void deleteByUser(User user);
}
