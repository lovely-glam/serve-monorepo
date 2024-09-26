package com.lovelyglam.database.model.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "service_booking")
public class ServiceBooking extends BaseEntity {
    @ManyToOne(targetEntity = UserAccount.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_account_id", nullable = false)
    private UserAccount userAccount;
    @ManyToOne(targetEntity = ShopService.class, fetch = FetchType.EAGER)
    private ShopService shopService;
    @Column(name = "completed_time")
    private Timestamp completeTime;
    @Column(name = "place_price")
    private BigDecimal placePrice;
}
