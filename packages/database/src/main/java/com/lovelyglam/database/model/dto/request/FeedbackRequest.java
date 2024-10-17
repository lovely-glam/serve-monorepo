package com.lovelyglam.database.model.dto.request;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeedbackRequest {
    private BigDecimal bookId;
    private String comment;
    private Double vote;
}
