package com.lovelyglam.utils.payment.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PaymentAPIRequest {
    private int totalAmount;
    private String orderInfo;
    private String callBackUrl;
    private String merchantCallBackUrl;
    private int orderId;
}
