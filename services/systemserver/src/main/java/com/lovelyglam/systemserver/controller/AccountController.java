package com.lovelyglam.systemserver.controller;

import com.lovelyglam.database.model.dto.request.UserAccountRequest;
import com.lovelyglam.database.model.dto.response.ResponseObject;
import com.lovelyglam.systemserver.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "manage-account")
public class AccountController {
    private final AccountService accountService;
    @PostMapping
    public ResponseEntity<ResponseObject> disableUserAccount(@RequestParam BigDecimal accountId, BindingResult bindingResult) {
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
}
