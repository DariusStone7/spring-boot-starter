package com.coop.api.core.service;

import com.coop.api.core.entity.User;
import com.coop.api.modules.security.entity.RefreshToken;

public interface RefreshTokenService {

    public RefreshToken createRefreshToken(User user);

    public void verifyTokenExpiration(RefreshToken token);

    public void deleteRefreshToken(RefreshToken token);
}
