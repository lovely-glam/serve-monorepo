package com.lovelyglam.database.model.entity;

import com.lovelyglam.database.model.constant.LoginMethodType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Entity(name = "login_methods")
public class LoginMethod extends BaseEntity {
    @ManyToOne(targetEntity = UserAccount.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserAccount user;

    @Column(name = "login_type")
    @Enumerated(EnumType.STRING)
    private LoginMethodType loginType;

    @Column(name = "username", nullable = true)
    private String username;

    @Column(name = "hash_password", nullable = true)
    private String hashPassword;

    @Column(name = "external_id", nullable = true)
    private String externalId;

    @Column(name = "external_id", nullable = true)
    private String externalEmail;

    @Column(name = "is_enabled")
    private boolean isEnabled;
}
