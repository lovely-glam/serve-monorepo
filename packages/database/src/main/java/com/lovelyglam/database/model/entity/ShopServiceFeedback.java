package com.lovelyglam.database.model.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
@Entity(name = "service_feedbacks")
public class ShopServiceFeedback {
    @EmbeddedId
    private ShopServiceFeedbackId id;
    @Column(name = "id", nullable = false, unique = true)
    private String subId;
    private int vote;
    private String comment;
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;
    @Column(name = "updated_date", nullable = false)
    private LocalDateTime updatedDate;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
        updatedDate = LocalDateTime.now();
        subId = UUID.randomUUID().toString();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedDate = LocalDateTime.now();
    }
}
