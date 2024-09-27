package com.lovelyglam.database.model.dto.request;

import com.lovelyglam.database.model.constant.AppointmentStatus;
import com.lovelyglam.database.model.entity.ShopService;
import com.lovelyglam.database.model.entity.UserAccount;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Builder
@Data
public class BookingRequest {
    BigDecimal nailServiceId;
    BigDecimal userAccountId;
    Timestamp makingDay;
    Timestamp startTime;
    AppointmentStatus status;
}
