package com.lovelyglam.database.model.dto.request;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class NailServiceRequest {
    BigDecimal id;
    String name;
    String description;
    BigDecimal basePrice;
    Long duration;
    int maxSlot;
}
