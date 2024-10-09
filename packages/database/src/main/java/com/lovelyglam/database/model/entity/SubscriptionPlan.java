package com.lovelyglam.database.model.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.lovelyglam.database.model.constant.SubscriptionPlanStatus;
import com.lovelyglam.database.model.constant.SubscriptionRole;

import jakarta.persistence.CascadeType;
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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "subscription_plans")
public class SubscriptionPlan extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private SubscriptionRole role;
    @OneToMany(targetEntity = SubscriptionPayment.class, fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST}, mappedBy = "subscriptionPlan")
    private List<SubscriptionPayment> payments;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SubscriptionPlanStatus status;
    @Column(name = "ex_datetime")
    private LocalDateTime exTime;
    @ManyToOne(targetEntity = ShopAccount.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_account_id")
    private ShopAccount shopAccount;
}
