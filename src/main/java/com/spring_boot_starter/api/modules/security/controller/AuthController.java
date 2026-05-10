package com.spring_boot_starter.api.modules.security.controller;

import com.spring_boot_starter.api.core.entity.User;
import com.spring_boot_starter.api.core.service.AuthService;
import com.spring_boot_starter.api.modules.security.dto.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody() @Valid AuthRequestDto authRequestDto) {
       return  authService.login(authRequestDto);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResponseDto> refreshToken(@RequestBody() @Valid RefreshRequestDto refreshRequestDto) {
        return  authService.refreshToken(refreshRequestDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody() @Valid RefreshRequestDto refreshRequestDto) {
        return  authService.logout(refreshRequestDto);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<TokenResponseDto> verifyOtp(@RequestBody @Valid OtpRequestDto otpRequestDto) {
        return  authService.verifyOtp(otpRequestDto);
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp() {
        return  authService.resendOtp();
    }

    @GetMapping("/me")
    public ResponseEntity<User> getMe(){
        return authService.getMe();
    }
}
