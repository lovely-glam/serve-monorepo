package com.lovelyglam.database.model.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerAccountManagementResponse {
    BigDecimal id;
    String username;
    String fullName;
    String email;
    boolean status;
    LocalDateTime createdDate;
}
