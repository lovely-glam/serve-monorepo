package com.lovelyglam.database.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSubscriptionResponse {
    private String callbackUrl;
    private boolean status;
    private String orderId;
    private String transactionId;
}
