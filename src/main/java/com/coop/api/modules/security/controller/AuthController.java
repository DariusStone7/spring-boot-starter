package com.coop.api.modules.security.controller;

import com.coop.api.core.entity.User;
import com.coop.api.core.service.AuthService;
import com.coop.api.modules.security.dto.AuthRequestDto;
import com.coop.api.modules.security.dto.OtpRequestDto;
import com.coop.api.modules.security.dto.AuthResponseDto;
import com.coop.api.modules.security.dto.RefreshRequestDto;
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
    public ResponseEntity<AuthResponseDto> refreshToken(@RequestBody() @Valid RefreshRequestDto refreshRequestDto) {
        return  authService.refreshToken(refreshRequestDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody() @Valid RefreshRequestDto refreshRequestDto) {
        return  authService.logout(refreshRequestDto);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<AuthResponseDto> verifyOtp(@RequestBody @Valid OtpRequestDto otpRequestDto) {
        return  authService.verifyOtp(otpRequestDto);
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@RequestBody Map<String, Object> body) {
        return  authService.resendOtp(body);
    }

    @GetMapping("/me")
    public ResponseEntity<User> getMe(){
        return authService.getMe();
    }
}
