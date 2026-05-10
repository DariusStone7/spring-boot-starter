package com.spring_boot_starter.api.modules.security.dto;

public record TokenResponseDto(
        String accessToken,
        String refreshToken
) {}
