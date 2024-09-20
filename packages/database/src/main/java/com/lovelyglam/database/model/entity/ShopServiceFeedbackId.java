package com.lovelyglam.database.model.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Embeddable
public class ShopServiceFeedbackId {
    @ManyToOne(targetEntity = ShopService.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "shop_service_id", nullable = false)
    private ShopService shopService;
    @ManyToOne(targetEntity = ServiceBooking.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "service_booking_id", nullable = false)
    private ServiceBooking serviceBooking;
    @ManyToOne(targetEntity = UserAccount.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_account_id")
    private UserAccount userAccount;
}
