package com.coop.api.modules.security.service;

import com.coop.api.core.entity.User;
import com.coop.api.core.service.AuthService;
import com.coop.api.exceptions.ErrorResponse;
import com.coop.api.exceptions.TokenException;
import com.coop.api.modules.security.dto.AuthRequestDto;
import com.coop.api.modules.security.dto.AuthResponseDto;
import com.coop.api.modules.security.dto.OtpRequestDto;
import com.coop.api.modules.security.dto.RefreshRequestDto;
import com.coop.api.modules.security.entity.RefreshToken;
import com.coop.api.modules.security.repository.RefreshTokenRepository;
import com.coop.api.modules.users.repository.UserRepository;
import com.coop.api.utils.JwtUtil;
import com.coop.api.utils.OtpUtil;
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
                            authRequestDto.getUsername(), authRequestDto.getPassword()));

            User user = userRepository.findByUsername(authRequestDto.getUsername());

            if (user.getIsOtpEnabled() != null && user.getIsOtpEnabled()) {
                String otp = otpUtil.createOtp(user);
                otpUtil.sendOtp(user.getEmail(), otp);

                Map<String, Object> response = new HashMap<>();
                response.put("user_id", user.getId());

                return ResponseEntity.ok().body(response);
            }

            String accessToken = jwtUtil.generateToken((UserDetails) authenticate.getPrincipal(), this.jwtAccessExpiration);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
            return ResponseEntity.ok().body(new AuthResponseDto(accessToken, refreshToken.getToken()));

        } catch (BadCredentialsException | InternalAuthenticationServiceException ex) {
            log.warn("Login failed");
            ErrorResponse error = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Username or password incorrect", "BAD_CREDENTIALS");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(error);
        }
    }

    @Override
    public ResponseEntity<AuthResponseDto> refreshToken(RefreshRequestDto refreshRequestDto) {
        log.info("New refresh token attempt");
        String token = refreshRequestDto.getRefreshToken();
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token).orElseThrow(() -> {
            log.warn("Refresh token failed");
           return new TokenException("Refresh token not found");
        });

        refreshTokenService.verifyTokenExpiration(refreshToken);

        User user = refreshToken.getUser();

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getUsername());

        String accessToken = jwtUtil.generateToken(userDetails, this.jwtAccessExpiration);
        return ResponseEntity.ok(new AuthResponseDto(accessToken, refreshToken.getToken()));
    }

    @Override
    public ResponseEntity<String> logout(RefreshRequestDto refreshRequestDto) {
        log.info("New logout attempt");
        String token = refreshRequestDto.getRefreshToken();
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token).get();
        refreshTokenService.deleteRefreshToken(refreshToken);
        return ResponseEntity.ok("User logged out");
    }

    @Override
    public ResponseEntity<AuthResponseDto> verifyOtp(OtpRequestDto otpRequestDto) {
        log.info("New verify OTP attempt");
        String otp = otpRequestDto.getOtp();
        User user = userRepository.findById(otpRequestDto.getUser_id()).get();
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getUsername());
        otpUtil.verifyOtp(user, otp);

        String accessToken = jwtUtil.generateToken(userDetails, this.jwtAccessExpiration);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        return ResponseEntity.ok(new AuthResponseDto(accessToken, refreshToken.getToken()));
    }

    @Override
    public ResponseEntity<?> resendOtp(Map<String, Object> body) {
        log.info("New resend OTP attempt");
        User user = userRepository.findById(Long.valueOf(body.get("user_id").toString())).get();

        String otp = otpUtil.createOtp(user);
        otpUtil.sendOtp(user.getEmail(), otp);

        Map<String, Object> response = new HashMap<>();
        response.put("user_id", user.getId());
        return ResponseEntity.ok().body(response);
    }

    @Override
    public ResponseEntity<User> getMe() {
        log.info("New get me attempt");
        return ResponseEntity.ok().body(customUserDetailsService.getUser());
    }
}
