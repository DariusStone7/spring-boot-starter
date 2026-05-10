package com.spring_boot_starter.api.modules.security.dto;

public record AuthResponseDto (
        String accessToken,
        String refreshToken,
        Boolean isOtpEnabled
){}
