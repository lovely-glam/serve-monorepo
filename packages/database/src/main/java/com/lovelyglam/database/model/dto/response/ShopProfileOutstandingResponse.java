package com.lovelyglam.database.model.dto.response;
import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShopProfileOutstandingResponse {
    BigDecimal id;
    String name;
    Double rating;
    Integer feedbackNumber;
    String address;
    List<String> thumbs;
}