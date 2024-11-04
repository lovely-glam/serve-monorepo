package com.lovelyglam.database.model.dto.request;

import com.lovelyglam.database.model.constant.MerchantType;
import com.lovelyglam.database.model.constant.SubscriptionRole;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubscriptionOrderRequest {
    private SubscriptionRole subscriptionRole;
    private String callbackUrl;
    private MerchantType type;
}
