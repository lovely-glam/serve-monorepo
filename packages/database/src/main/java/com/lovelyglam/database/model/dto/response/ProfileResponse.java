package com.lovelyglam.database.model.dto.response;

import lombok.Builder;

@Builder
public record ProfileResponse(
    String avatarUrl,
    String fullName,
    String email
) {}
