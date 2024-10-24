package com.lovelyglam.database.model.entity;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopServiceFeedbackId implements Serializable {
    @ManyToOne(targetEntity = ShopService.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "shop_service_id", nullable = false)
    private ShopService shopService;
    @ManyToOne(targetEntity = Booking.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "service_booking_id", nullable = false)
    private Booking serviceBooking;
    @ManyToOne(targetEntity = UserAccount.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_account_id")
    private UserAccount userAccount;
}
