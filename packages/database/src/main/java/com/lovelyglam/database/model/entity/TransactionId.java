package com.lovelyglam.database.model.entity;

import com.lovelyglam.database.model.constant.MerchantType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "transaction_id")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionId extends BaseEntity {
    @Column(name = "merchant_type", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private MerchantType merchantType;
}
