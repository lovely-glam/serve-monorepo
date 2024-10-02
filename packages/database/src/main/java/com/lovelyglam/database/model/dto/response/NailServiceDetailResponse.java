package com.lovelyglam.database.model.dto.response;

import java.math.BigDecimal;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NailServiceDetailResponse {
    BigDecimal id;
    String name;
    String description;
    BigDecimal basePrice;
    Long duration;
    List<BookingResponse> bookingList;
}
