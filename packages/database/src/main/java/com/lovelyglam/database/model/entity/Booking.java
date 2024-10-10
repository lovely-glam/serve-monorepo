package com.lovelyglam.database.model.entity;

import java.sql.Timestamp;
import java.util.List;

import com.lovelyglam.database.model.constant.AppointmentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "bookings")
public class Booking extends BaseEntity {
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
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AppointmentStatus appointmentStatus;
    @OneToMany(targetEntity = BookingPayment.class, fetch = FetchType.EAGER, mappedBy = "booking")
    private List<BookingPayment> bookingPayments;
}
