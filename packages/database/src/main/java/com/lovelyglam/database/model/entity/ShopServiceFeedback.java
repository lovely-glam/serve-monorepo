package com.lovelyglam.database.model.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "service_feedbacks")
public class ShopServiceFeedback {
    @EmbeddedId
    private ShopServiceFeedbackId id;
    @Column(name = "id", nullable = false, unique = true)
    private String subId;
    @Column(name = "vote", columnDefinition = "NUMERIC(20,1)", nullable = false)
    private Double vote;
    @Column(name = "comment", columnDefinition = "TEXT", nullable = false)
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
