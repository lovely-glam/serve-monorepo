package com.lovelyglam.utils.payment.service.impl;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.springframework.stereotype.Service;
import com.lovelyglam.utils.payment.config.VNPayConfig;
import com.lovelyglam.utils.payment.model.VNPayApiCallback;
import com.lovelyglam.utils.payment.service.VNPayService;
import com.lovelyglam.utils.payment.util.VNPayUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VNPayServiceImpl implements VNPayService {
    private final VNPayConfig vnPayConfig;
    private final VNPayUtils utils;
    
    public String createOrder(int totalAmount, String orderInfo, String returnUri, HttpServletRequest request) {
        String vnpTxnRef = utils.getRandomNumber(8);
        String vnpIpAddr = utils.getIpAddress(request);
        String vnpTmnCode = vnPayConfig.getVnpTmnCode();
        String orderType = "order-type";
        Map<String, String> vnpParams = new HashMap<>();
        vnpParams.put("vnp_Version", "2.1.0");
        vnpParams.put("vnp_Command", "pay");
        vnpParams.put("vnp_TmnCode", vnpTmnCode);
        vnpParams.put("vnp_Amount", String.valueOf(totalAmount * 100));
        vnpParams.put("vnp_CurrCode", "VND");
        vnpParams.put("vnp_TxnRef", vnpTxnRef);
        vnpParams.put("vnp_OrderInfo", orderInfo);
        vnpParams.put("vnp_OrderType", orderType);
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_ReturnUrl", utils.generateCallbackUrl(request, returnUri));
        vnpParams.put("vnp_IpAddr", vnpIpAddr);
        addTimestamp(vnpParams);

        String queryUrl = buildQueryUrl(vnpParams);
        String vnpSecureHash = utils.hmacSHA512(vnPayConfig.getVnpHashSecret(), queryUrl);
        return vnPayConfig.getVnpPayUrl() + "?" + queryUrl + "&vnp_SecureHash=" + vnpSecureHash;
    }

    private void addTimestamp(Map<String, String> params) {
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        params.put("vnp_ExpireDate", vnp_ExpireDate);
    }

    private String buildQueryUrl(Map<String, String> vnpParams) {
        List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
        Collections.sort(fieldNames);
        StringBuilder query = new StringBuilder();
        for (String fieldName : fieldNames) {
            String fieldValue = vnpParams.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                try {
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()))
                         .append('=')
                         .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()))
                         .append('&');
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace(); // Consider proper logging
                }
            }
        }
        query.setLength(query.length() - 1); // Remove the last '&'
        return query.toString();
    }

    public int orderReturn(HttpServletRequest request) {
        Map<String, String> fields = new HashMap<>();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements();) {
            String fieldName = params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnpSecureHash = fields.remove("vnp_SecureHash");
        String signValue = utils.hashAllFields(fields);
        if (signValue.equals(vnpSecureHash)) {
            return "00".equals(request.getParameter("vnp_TransactionStatus")) ? 1 : 0;
        } else {
            return -1;
        }
    }

    @Override
    public VNPayApiCallback convertVNPayCallbackInfo(HttpServletRequest request) {
        int paymentStatus = orderReturn(request);
        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime dateTime = LocalDateTime.parse(paymentTime, formatter);
        return VNPayApiCallback.builder()
        .paymentStatus(paymentStatus == 1)
        .paymentTime(dateTime)
        .totalPrice(new BigDecimal(totalPrice))
        .orderId(orderInfo)
        .transactionId(transactionId)
        .build();
    }
}