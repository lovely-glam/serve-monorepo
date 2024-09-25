package com.lovelyglam.database.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "system_accounts")
public class SystemAccount extends BaseEntity {
    @Column(name = "username", unique = true)
    private String username;
    @Column(name = "hash_password", nullable = false)
    private String hashPassword;
    @Column(name = "is_active")
    private boolean isActive;
}
