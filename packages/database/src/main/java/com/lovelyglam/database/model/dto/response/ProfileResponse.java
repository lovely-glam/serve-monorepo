package com.lovelyglam.database.model.dto.response;

import lombok.Builder;
import java.math.BigDecimal;
@Builder
public record ProfileResponse(
    BigDecimal id,
    String username,
    String avatarUrl,
    String fullName,
    String email
) {}
