package com.lovelyglam.database.model.entity;

import java.sql.Timestamp;

import com.lovelyglam.database.model.constant.AppointmentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "appointments")
public class Appointment extends BaseEntity {
    @ManyToOne(targetEntity = ShopService.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_service_id")
    private ShopService shopService;
    @ManyToOne(targetEntity = UserAccount.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserAccount userAccount;
    @Column(name = "making_day")
    private Timestamp makingDay;
    @Column(name = "start_time")
    private Timestamp startTime;
    @Column(name = "end_time")
    private Timestamp endTime;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AppointmentStatus appointmentStatus;
}
