package com.lovelyglam.database.repository;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lovelyglam.database.model.entity.ChatBox;

@Repository
public interface ChatBoxRepository extends BaseRepository<ChatBox, BigDecimal> {
    @Query(value = "SELECT cb FROM chat_boxes cb WHERE cb.shopProfile.id = :shopId AND cb.userAccount.id = :userId")
    Optional<ChatBox> findChatBoxByShopIdAndUserId (
        @Param(value = "shopId") BigDecimal shopId,
        @Param(value = "userId") BigDecimal userId
    );

    @Query(value = "SELECT cb FROM chat_boxes cb WHERE cb.shopProfile.id = :shopId")
    Collection<ChatBox> findChatBoxByShopId (@Param(value = "shopId") BigDecimal shopId);
}
