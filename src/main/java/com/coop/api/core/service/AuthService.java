package com.coop.api.core.service;

import com.coop.api.core.entity.User;
import com.coop.api.modules.security.dto.AuthRequestDto;
import com.coop.api.modules.security.dto.AuthResponseDto;
import com.coop.api.modules.security.dto.OtpRequestDto;
import com.coop.api.modules.security.dto.RefreshRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface AuthService {
    public ResponseEntity<?> login(@RequestBody() AuthRequestDto authRequestDto);

    public ResponseEntity<AuthResponseDto> refreshToken(@RequestBody() @Valid RefreshRequestDto refreshRequestDto);

    public ResponseEntity<String> logout(@RequestBody() @Valid RefreshRequestDto refreshRequestDto);

    public ResponseEntity<AuthResponseDto> verifyOtp(@RequestBody OtpRequestDto otpRequestDto);

    public ResponseEntity<?> resendOtp(@RequestBody Map<String, Object> body);

    public ResponseEntity<User> getMe();
}
