package com.lovelyglam.database.repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lovelyglam.database.model.entity.ShopAccount;

@Repository
public interface ShopAccountRepository extends BaseRepository<ShopAccount, BigDecimal> {
    @Query(value = "SELECT sa FROM shop_accounts sa WHERE sa.username = :username")
    Optional<ShopAccount> findShopAccountByUsername(@Param("username") String username);
    @Query(value = "SELECT sa FROM shop_accounts sa WHERE sa.email = :email")
    Optional<ShopAccount> findShopAccountByBusinessEmail(@Param("email") String email);
}
