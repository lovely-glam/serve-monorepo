package com.lovelyglam.database.repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lovelyglam.database.model.entity.LoginMethod;

@Repository
public interface LoginMethodRepository extends BaseRepository<LoginMethod, BigDecimal> {
    @Query(value = "SELECT lm FROM login_methods lm WHERE lm.externalId = :externalId")
    Optional<LoginMethod> findLoginMethodByExternalId(@Param(value = "externalId") String externalId);
    @Query(value = "SELECT lm FROM login_methods lm WHERE lm.externalId = :externalId AND lm.loginMethodType = :loginMethodType")
    Optional<LoginMethod> findLoginMethodByExternalIdAndLoginMethodType(@Param(value = "externalId") String externalId, @Param(value = "loginMethodType") String loginMethodType);
}
