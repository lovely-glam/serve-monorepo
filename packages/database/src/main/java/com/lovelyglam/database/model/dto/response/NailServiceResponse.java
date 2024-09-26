package com.lovelyglam.database.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
public class NailServiceResponse {
    BigDecimal id;
    String name;
    String description;
    BigDecimal basePrice;
    Timestamp duration;
}
