package com.lovelyglam.database.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Entity(name = "system_contact_report")
public class SystemContactReport {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;
    @Column(name = "contact_name", nullable = false)
    private String contactName;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "message", columnDefinition = "TEXT", nullable = false)
    private String message;
    @Column(name = "feedback", columnDefinition = "TEXT", nullable = true)
    private String feedback;
    @Column(name = "is_read")
    private boolean read;
}
