package com.lovelyglam.database.model.dto.request;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BookingRequest {
    BigDecimal nailServiceId;
    Date makingDay;
    Timestamp startTime;
}
