package com.spring_boot_starter.api.core.service;

import com.spring_boot_starter.api.core.entity.User;
import com.spring_boot_starter.api.modules.security.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface AuthService {
    public ResponseEntity<?> login(@RequestBody() AuthRequestDto authRequestDto);

    public ResponseEntity<TokenResponseDto> refreshToken(@RequestBody() @Valid RefreshRequestDto refreshRequestDto);

    public ResponseEntity<String> logout(@RequestBody() @Valid RefreshRequestDto refreshRequestDto);

    public ResponseEntity<TokenResponseDto> verifyOtp(@RequestBody OtpRequestDto otpRequestDto);

    public ResponseEntity<String> resendOtp();

    public ResponseEntity<User> getMe();
}
