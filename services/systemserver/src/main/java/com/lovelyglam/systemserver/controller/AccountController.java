package com.lovelyglam.systemserver.controller;

import com.lovelyglam.database.model.dto.request.SearchRequestParamsDto;
import com.lovelyglam.database.model.dto.response.ResponseObject;
import com.lovelyglam.systemserver.service.CustomerAccountService;
import com.lovelyglam.systemserver.service.NailAccountService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "account-management")
public class AccountController {
    private final CustomerAccountService accountService;
    private final NailAccountService nailAccountService;

    @PatchMapping("users/disable/{id}")
    public ResponseEntity<ResponseObject> disableUserAccount(@PathVariable(value = "id") BigDecimal accountId) {
        var result = accountService.disableUserAccount(accountId);
        var responseObject = ResponseObject.builder()
                .code("DISABLE_USER_ACCOUNT_SUCCESS")
                .content(result)
                .isSuccess(true)
                .status(HttpStatus.OK)
                .requestTime(LocalDateTime.now())
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
                .requestTime(LocalDateTime.now())
                .build();

        return ResponseEntity.ok().body(responseObject);
    }

    @PatchMapping("shops/disable/{id}")
    public ResponseEntity<ResponseObject> disableShopAccount(@PathVariable(value = "id") BigDecimal accountId) {
        var result = nailAccountService.disableShopAccount(accountId);
        var responseObject = ResponseObject.builder()
                .code("DISABLE_SHOP_ACCOUNT_SUCCESS")
                .content(result)
                .isSuccess(true)
                .status(HttpStatus.OK)
                .message("Query Success")
                .requestTime(LocalDateTime.now())
                .build();

        return ResponseEntity.ok().body(responseObject);
    }

    @PatchMapping("shops/enable/{id}")
    public ResponseEntity<ResponseObject> enableShopAccount(@PathVariable(value = "id") BigDecimal accountId) {
        var result = nailAccountService.activeShopAccount(accountId);
        var responseObject = ResponseObject.builder()
                .code("ACTIVE_SHOP_ACCOUNT_SUCCESS")
                .content(result)
                .isSuccess(true)
                .status(HttpStatus.OK)
                .message("Query Success")
                .requestTime(LocalDateTime.now())
                .build();

        return ResponseEntity.ok().body(responseObject);
    }

    @GetMapping("shops/{id}")
    public ResponseEntity<ResponseObject> getShopAccount(@PathVariable(value = "id") BigDecimal shopId) {
        var result = nailAccountService.getShopAccount(shopId);
        var responseObject = ResponseObject.builder()
                .code("GET_SHOP_ACCOUNT_SUCCESS")
                .content(result)
                .isSuccess(true)
                .status(HttpStatus.OK)
                .message("Query Success")
                .requestTime(LocalDateTime.now())
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
                .requestTime(LocalDateTime.now())
                .build();

        return ResponseEntity.ok().body(responseObject);
    }

    @GetMapping("users")
    public ResponseEntity<ResponseObject> getUserAccounts(
            @PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable,
            @RequestParam(name = "q", required = false) String query) {
        var queryDto = SearchRequestParamsDto.builder()
                .search(query)
                .wrapSort(pageable)
                .build();
        var result = accountService.getCustomerAccounts(queryDto);
        return ResponseEntity.ok(ResponseObject.builder()
                .code("GET_USER_ACCOUNT_LIST_SUCCESS")
                .content(result)
                .isSuccess(true)
                .status(HttpStatus.OK)
                .message("Query Success")
                .requestTime(LocalDateTime.now())
                .build());
    }

    @GetMapping("shops")
    public ResponseEntity<ResponseObject> getShopAccounts(
            @PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable,
            @RequestParam(name = "q", required = false) String query) {
        var queryDto = SearchRequestParamsDto.builder()
                .search(query)
                .wrapSort(pageable)
                .build();
        var result = nailAccountService.getShopAccounts(queryDto);
        return ResponseEntity.ok(ResponseObject.builder()
                .code("GET_SHOP_ACCOUNT_LIST_SUCCESS")
                .content(result)
                .isSuccess(true)
                .status(HttpStatus.OK)
                .message("Query Success")
                .requestTime(LocalDateTime.now())
                .build());
    }
}
