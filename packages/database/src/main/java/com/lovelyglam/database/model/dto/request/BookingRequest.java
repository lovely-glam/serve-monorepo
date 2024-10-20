package com.lovelyglam.database.model.dto.request;

import com.lovelyglam.database.model.constant.AppointmentStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

@Builder
@Data
public class BookingRequest {
    BigDecimal nailServiceId;
    Date makingDay;
    Timestamp startTime;
    AppointmentStatus status;
}
