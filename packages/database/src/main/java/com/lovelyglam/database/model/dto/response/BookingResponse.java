package com.lovelyglam.database.model.dto.response;

import com.lovelyglam.database.model.constant.AppointmentStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Builder
@Data
public class BookingResponse {
    BigDecimal id;
    String shopServiceName;
    String userAccountName;
    Timestamp makingDay;
    Timestamp startTime;
    AppointmentStatus status;
}
