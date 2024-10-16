package com.lovelyglam.userserver.service;

import java.util.List;

import com.lovelyglam.database.model.dto.response.FeedbackResponse;

public interface FeedbackService {
    List<FeedbackResponse> getFeedbacks();
}
