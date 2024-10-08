package com.lovelyglam.database.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class NailServiceResponse {
    BigDecimal id;
    String name;
    String description;
    BigDecimal basePrice;
    Long duration;
}
