package com.lovelyglam.workerservice.cron;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lovelyglam.database.repository.ShopRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShopProfileVoteTrackingService {
    private final ShopRepository shopRepository;
    @Transactional
    @Scheduled(fixedDelay = 60000)
    public void updateAllProfilesAverageVotes() {
        shopRepository.updateAllAverageVotes();
        System.out.println("Update Shop Vote");
    }
}
