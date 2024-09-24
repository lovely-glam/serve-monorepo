package com.lovelyglam.database.repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lovelyglam.database.model.entity.SystemAccount;

@Repository
public interface SystemAccountRepository extends BaseRepository<SystemAccount, BigDecimal> {
    @Query(value = "SELECT sa FROM system_accounts sa WHERE sa.username = :username")
    Optional<SystemAccount> findSystemAccountByUsername(@Param("username") String username);
}
