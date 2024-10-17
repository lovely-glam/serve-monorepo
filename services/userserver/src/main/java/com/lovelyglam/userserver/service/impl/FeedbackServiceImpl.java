package com.lovelyglam.userserver.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lovelyglam.database.model.constant.AppointmentStatus;
import com.lovelyglam.database.model.dto.request.FeedbackRequest;
import com.lovelyglam.database.model.dto.response.FeedbackResponse;
import com.lovelyglam.database.model.entity.ShopServiceFeedback;
import com.lovelyglam.database.model.entity.ShopServiceFeedbackId;
import com.lovelyglam.database.model.exception.ActionFailedException;
import com.lovelyglam.database.model.exception.NotFoundException;
import com.lovelyglam.database.model.exception.ValidationFailedException;
import com.lovelyglam.database.repository.BookingRepository;
import com.lovelyglam.database.repository.NailServiceFeedbackRepository;
import com.lovelyglam.userserver.service.FeedbackService;
import com.lovelyglam.userserver.util.AuthUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {
    private final BookingRepository bookingRepository;
    private final NailServiceFeedbackRepository nailServiceFeedbackRepository;
    private final AuthUtils authUtils;

    public List<FeedbackResponse> getFeedbacks() {
        return nailServiceFeedbackRepository.findRandomFeedback(9).stream().map((entity) -> {
            var shopProfile = entity.getId().getShopService().getShopProfile();
            var userAccount = entity.getId().getUserAccount();
            var reviewNumber = nailServiceFeedbackRepository.calculateTotalVoteOfShop(shopProfile.getId());
            return FeedbackResponse.builder()
                    .id(entity.getSubId())
                    .shopId(shopProfile.getId())
                    .shopName(shopProfile.getName())
                    .rating(shopProfile.getVote())
                    .reviewNumber(reviewNumber)
                    .customerName(userAccount.getFullname())
                    .joinDate(userAccount.getCreatedDate())
                    .feedback(entity.getComment())
                    .build();
        }).toList();
    }

    public FeedbackResponse createFeedback(FeedbackRequest request) {
        var bookingEntity = bookingRepository.findById(request.getBookId()).orElseThrow(() -> new NotFoundException("Not found booking with this id"));
        if (bookingEntity.getAppointmentStatus() != AppointmentStatus.DONE) {
            throw new ValidationFailedException("Only The Booking Done Can Comment");
        }
        var userEntity =bookingEntity.getUserAccount();
        var userSession = authUtils.getUserAccountFromAuthentication();
        if (!userEntity.getId().equals(userSession.getId())) {
            throw new ValidationFailedException("You Are Not The Owner Of This Booking");
        }
        var feedbackEntity = ShopServiceFeedback.builder()
        .id(ShopServiceFeedbackId.builder()
            .userAccount(userSession)
            .shopService(bookingEntity.getShopService())
            .serviceBooking(bookingEntity)
        .build())
        .comment(request.getComment())
        .vote(request.getVote())
        .build();
        try {
            var feedbackResult = nailServiceFeedbackRepository.save(feedbackEntity);
            var shopProfile = bookingEntity.getShopService().getShopProfile();
            var reviewNumber = nailServiceFeedbackRepository.calculateTotalVoteOfShop(shopProfile.getId());
            return FeedbackResponse.builder()
            .id(feedbackResult.getSubId())
            .shopId(shopProfile.getId())
            .shopName(shopProfile.getName())
            .rating(shopProfile.getVote())
            .reviewNumber(reviewNumber)
            .customerName(userSession.getFullname())
            .joinDate(userSession.getCreatedDate())
            .feedback(feedbackResult.getComment())
            .build();
        } catch (Exception ex) {
            throw new ActionFailedException(String.format("Can't Create Feedback With Reason: %s", ex.getMessage()));
        }
    }
}
