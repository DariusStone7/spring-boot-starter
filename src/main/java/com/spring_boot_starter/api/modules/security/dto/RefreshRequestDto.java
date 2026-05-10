package com.spring_boot_starter.api.modules.security.dto;

import jakarta.validation.constraints.NotNull;

public record RefreshRequestDto (
        @NotNull String refreshToken
){}

