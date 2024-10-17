package com.lovelyglam.userserver.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lovelyglam.database.model.dto.request.FeedbackRequest;
import com.lovelyglam.database.model.dto.response.ResponseObject;
import com.lovelyglam.userserver.service.FeedbackService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feedbacks")
public class FeedbackController {
    private final FeedbackService feedbackService;

    @GetMapping(path = "/home")
    public ResponseEntity<ResponseObject> getShopProfileOutstanding() {
        var result = feedbackService.getFeedbacks();
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .code("GET_HOME_SHOP_FEEDBACK_SUCCESS")
                        .content(result)
                        .isSuccess(true)
                        .requestTime(LocalDateTime.now())
                        .status(HttpStatus.OK)
                        .message("Query Success")
                        .build());
    }

    @PostMapping
    public ResponseEntity<ResponseObject> postFeedback(@RequestBody FeedbackRequest request) {
        var result = feedbackService.createFeedback(request);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .code("CREATE_FEEDBACK_SUCCESS")
                        .content(result)
                        .isSuccess(true)
                        .requestTime(LocalDateTime.now())
                        .status(HttpStatus.OK)
                        .message("Insert Success")
                        .build());
    }

    @GetMapping(path = "/shops/{shopProfileId}")
    public ResponseEntity<ResponseObject> getFeedbackByShopId(
            @PathVariable(value = "shopProfileId") BigDecimal shopProfileId,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        var result = feedbackService.getFeedbackByShopId(shopProfileId, null);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .code("GET_SHOP_FEEDBACK_SUCCESS")
                        .content(result)
                        .isSuccess(true)
                        .requestTime(LocalDateTime.now())
                        .status(HttpStatus.OK)
                        .message("Query Success")
                        .build());
    }
}
