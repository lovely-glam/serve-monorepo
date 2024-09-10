package com.lovelyglam.database.repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lovelyglam.database.model.entity.UserAccount;

@Repository
public interface UserAccountRepository extends BaseRepository<UserAccount, BigDecimal> {
    @Query(value = "SELECT ua FROM user_accounts ua WHERE ua.username = :username")
    Optional<UserAccount> findUserAccountByUsername(@Param(value = "username") String username);
}
