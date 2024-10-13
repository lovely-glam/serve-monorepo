package com.lovelyglam.systemserver.controller;


import com.lovelyglam.database.model.dto.response.ResponseObject;
import com.lovelyglam.systemserver.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "manage-account")
public class AccountController {
    private final AccountService accountService;
    @PatchMapping("users/disable/{id}")
    public ResponseEntity<ResponseObject> disableUserAccount(@PathVariable(value = "id") BigDecimal accountId) {
        var result = accountService.disableUserAccount(accountId);
        var responseObject = ResponseObject.builder()
                .code("DISABLE_USER_ACCOUNT_SUCCESS")
                .content(result)
                .isSuccess(true)
                .status(HttpStatus.OK)
                .message("Query Success")
                .build();

        return ResponseEntity.ok().body(responseObject);
    }
    @PatchMapping("users/enable/{id}")
    public ResponseEntity<ResponseObject> enableUserAccount(@PathVariable(value = "id") BigDecimal accountId) {
        var result = accountService.activeUserAccount(accountId);
        var responseObject = ResponseObject.builder()
                .code("ACTIVE_USER_ACCOUNT_SUCCESS")
                .content(result)
                .isSuccess(true)
                .status(HttpStatus.OK)
                .message("Query Success")
                .build();

        return ResponseEntity.ok().body(responseObject);
    }
    @PatchMapping("shops/disable/{id}")
    public ResponseEntity<ResponseObject> disableShopAccount(@PathVariable(value = "id") BigDecimal accountId) {
        var result = accountService.disableShopAccount(accountId);
        var responseObject = ResponseObject.builder()
                .code("DISABLE_SHOP_ACCOUNT_SUCCESS")
                .content(result)
                .isSuccess(true)
                .status(HttpStatus.OK)
                .message("Query Success")
                .build();

        return ResponseEntity.ok().body(responseObject);
    }
    @PatchMapping("shops/enable/{id}")
    public ResponseEntity<ResponseObject> enableShopAccount(@PathVariable(value = "id") BigDecimal accountId) {
        var result = accountService.activeShopAccount(accountId);
        var responseObject = ResponseObject.builder()
                .code("ACTIVE_SHOP_ACCOUNT_SUCCESS")
                .content(result)
                .isSuccess(true)
                .status(HttpStatus.OK)
                .message("Query Success")
                .build();

        return ResponseEntity.ok().body(responseObject);
    }
    @GetMapping("shops/{id}")
    public ResponseEntity<ResponseObject> getShopAccount(@PathVariable(value = "id") BigDecimal shopId) {
        var result = accountService.getShopAccount(shopId);
        var responseObject = ResponseObject.builder()
                .code("GET_SHOP_ACCOUNT_SUCCESS")
                .content(result)
                .isSuccess(true)
                .status(HttpStatus.OK)
                .message("Query Success")
                .build();

        return ResponseEntity.ok().body(responseObject);
    }
    @GetMapping("users/{id}")
    public ResponseEntity<ResponseObject> getUserAccount(@PathVariable(value = "id") BigDecimal userId) {
        var result = accountService.getUserAccount(userId);
        var responseObject = ResponseObject.builder()
                .code("GET_USER_ACCOUNT_SUCCESS")
                .content(result)
                .isSuccess(true)
                .status(HttpStatus.OK)
                .message("Query Success")
                .build();

        return ResponseEntity.ok().body(responseObject);
    }
}
