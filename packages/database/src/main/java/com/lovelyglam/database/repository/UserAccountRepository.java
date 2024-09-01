package com.lovelyglam.database.repository;

import java.math.BigDecimal;

import org.springframework.stereotype.Repository;

import com.lovelyglam.database.model.entity.UserAccount;

@Repository
public interface UserAccountRepository extends BaseRepository<UserAccount, BigDecimal> {
    
}
