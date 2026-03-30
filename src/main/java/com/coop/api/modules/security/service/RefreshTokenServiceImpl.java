package com.coop.api.modules.security.service;

import com.coop.api.core.entity.User;
import com.coop.api.core.service.RefreshTokenService;
import com.coop.api.exceptions.TokenException;
import com.coop.api.modules.security.entity.RefreshToken;
import com.coop.api.modules.security.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${jwt.refresh.expiration}")
    private int jwtRefreshExpiration;

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public RefreshToken createRefreshToken(User user) {

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUser(user);
        if (refreshToken.isPresent()) {
            deleteRefreshToken(refreshToken.get());
        }
        var token = new RefreshToken();
        token.setUser(user);
        token.setExpiryDate(Instant.now().plusMillis(this.jwtRefreshExpiration));
        token.setToken(UUID.randomUUID().toString());

        return refreshTokenRepository.save(token);
    }

    @Override
    public void verifyTokenExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            deleteRefreshToken(token);
            throw new TokenException("Refresh token is expired:  RefreshToken expired at " + token
                    .getExpiryDate().toString());
        }
    }

    @Override
    public void deleteRefreshToken(RefreshToken token) {
        if (token != null) {
            refreshTokenRepository.delete(token);
        }
    }
}
