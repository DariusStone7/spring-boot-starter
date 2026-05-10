package com.spring_boot_starter.api.modules.security.service;

import com.spring_boot_starter.api.core.entity.User;
import com.spring_boot_starter.api.core.service.AuthService;
import com.spring_boot_starter.api.exceptions.ErrorResponse;
import com.spring_boot_starter.api.exceptions.ResourceNotFoundException;
import com.spring_boot_starter.api.exceptions.TokenException;
import com.spring_boot_starter.api.modules.security.dto.*;
import com.spring_boot_starter.api.modules.security.entity.RefreshToken;
import com.spring_boot_starter.api.modules.security.repository.RefreshTokenRepository;
import com.spring_boot_starter.api.modules.users.repository.UserRepository;
import com.spring_boot_starter.api.utils.JwtUtil;
import com.spring_boot_starter.api.utils.OtpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private RefreshTokenServiceImpl refreshTokenService;

    @Autowired
    private OtpUtil otpUtil;

    @Value("${jwt.access.expiration}")
    private int jwtAccessExpiration;

    @Override
    public ResponseEntity<?> login(AuthRequestDto authRequestDto) {
        try {
            log.info("New login attempt");
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequestDto.username(), authRequestDto.password()));

            User user = userRepository.findByUsername(authRequestDto.username());

            String accessToken = jwtUtil.generateToken((UserDetails) authenticate.getPrincipal(), this.jwtAccessExpiration);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);


            boolean isOtpEnabled = false;
            if (user.getIsOtpEnabled() != null && user.getIsOtpEnabled()) {
                isOtpEnabled = true;
                String otp = otpUtil.createOtp(user);
                otpUtil.sendOtp(user.getEmail(), otp);
            }

            AuthResponseDto response = new AuthResponseDto(accessToken, refreshToken.getToken(), isOtpEnabled);

            return ResponseEntity.ok().body(response);

        } catch (BadCredentialsException | InternalAuthenticationServiceException ex) {
            log.warn("Login failed");
            ErrorResponse error = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Username or password incorrect", "BAD_CREDENTIALS");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(error);
        }
    }

    @Override
    public ResponseEntity<TokenResponseDto> refreshToken(RefreshRequestDto refreshRequestDto) {
        log.info("New refresh token attempt");
        String token = refreshRequestDto.refreshToken();
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token).orElseThrow(() ->
                new TokenException("Refresh token not found")
        );

        refreshTokenService.verifyTokenExpiration(refreshToken);

        User user = refreshToken.getUser();

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getUsername());

        String accessToken = jwtUtil.generateToken(userDetails, this.jwtAccessExpiration);
        return ResponseEntity.ok(new TokenResponseDto(accessToken, refreshToken.getToken()));
    }

    @Override
    public ResponseEntity<String> logout(RefreshRequestDto refreshRequestDto) {
        log.info("New logout attempt");
        String token = refreshRequestDto.refreshToken();
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token).orElseThrow(()->new ResourceNotFoundException("Refresh token not found"));;
        refreshTokenService.deleteRefreshToken(refreshToken);
        return ResponseEntity.ok("User logged out");
    }

    @Override
    public ResponseEntity<TokenResponseDto> verifyOtp(OtpRequestDto otpRequestDto) {
        log.info("New verify OTP attempt");
        String otp = otpRequestDto.otp();
        User user = customUserDetailsService.getUser();

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getUsername());
        otpUtil.verifyOtp(user, otp);

        String accessToken = jwtUtil.generateToken(userDetails, this.jwtAccessExpiration);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        return ResponseEntity.ok(new TokenResponseDto(accessToken, refreshToken.getToken()));
    }

    @Override
    public ResponseEntity<String> resendOtp() {
        log.info("New resend OTP attempt");
        User user = customUserDetailsService.getUser();

        String otp = otpUtil.createOtp(user);
        otpUtil.sendOtp(user.getEmail(), otp);

        return ResponseEntity.ok("OTP resented successfully");
    }

    @Override
    public ResponseEntity<User> getMe() {
        log.info("New get me attempt");
        return ResponseEntity.ok().body(customUserDetailsService.getUser());
    }
}
