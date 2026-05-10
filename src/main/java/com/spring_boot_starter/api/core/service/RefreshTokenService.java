package com.spring_boot_starter.api.core.service;

import com.spring_boot_starter.api.core.entity.User;
import com.spring_boot_starter.api.modules.security.entity.RefreshToken;

public interface RefreshTokenService {

    public RefreshToken createRefreshToken(User user);

    public void verifyTokenExpiration(RefreshToken token);

    public void deleteRefreshToken(RefreshToken token);
}
