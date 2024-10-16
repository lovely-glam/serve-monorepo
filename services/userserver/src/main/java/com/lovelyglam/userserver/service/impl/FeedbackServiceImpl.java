package com.lovelyglam.userserver.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lovelyglam.database.model.dto.response.FeedbackResponse;
import com.lovelyglam.database.repository.NailServiceFeedbackRepository;
import com.lovelyglam.database.repository.ShopRepository;
import com.lovelyglam.userserver.service.FeedbackService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {
    private final ShopRepository shopRepository;
    private final NailServiceFeedbackRepository nailServiceFeedbackRepository;

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
}
