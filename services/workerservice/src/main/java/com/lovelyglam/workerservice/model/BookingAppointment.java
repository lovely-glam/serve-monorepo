package com.lovelyglam.workerservice.model;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingAppointment {
    private String shopName;
    private String location;
    private Timestamp meetDate;
    private String email;
}
