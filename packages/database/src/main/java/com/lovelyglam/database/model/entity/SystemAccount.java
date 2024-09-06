package com.lovelyglam.database.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "system_accounts")
@Getter
@Setter
@NoArgsConstructor
public class SystemAccount extends BaseEntity {
    @Column(name = "username", unique = true)
    private String username;
    @Column(name = "hash_password", nullable = false)
    private String hashPassword;
    @Column(name = "is_active")
    private boolean isActive;
}
