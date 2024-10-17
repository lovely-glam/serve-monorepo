package com.lovelyglam.userserver.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.lovelyglam.database.model.dto.request.FeedbackRequest;
import com.lovelyglam.database.model.dto.response.FeedbackResponse;
import com.lovelyglam.database.model.dto.response.PaginationResponse;

public interface FeedbackService {
    List<FeedbackResponse> getFeedbacks();
    FeedbackResponse createFeedback(FeedbackRequest request);
    PaginationResponse<FeedbackResponse> getFeedbackByShopId (BigDecimal shopProfileId, Pageable pageable);
}
