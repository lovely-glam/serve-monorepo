package com.lovelyglam.userserver.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lovelyglam.database.model.dto.request.UserAccountRequest;
import com.lovelyglam.database.model.dto.response.ResponseObject;
import com.lovelyglam.userserver.service.ProfileService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "profiles")
public class ProfileController {
    private final ProfileService profileService;
    @GetMapping("me")
    public ResponseEntity<ResponseObject> getMe () {
        var result = profileService.getMe();
        return ResponseEntity.ok(ResponseObject.builder()
        .code("GET_PROFILE_SUCCESS")
        .content(result)
        .isSuccess(true)
        .message("Get Profile Success")
        .requestTime(LocalDateTime.now())
        .build());
    }

    @PutMapping
    public ResponseEntity<ResponseObject> updateProfile(@RequestBody @Valid UserAccountRequest request, BindingResult bindingResult) {
        var result = profileService.updateProfile(request);
        var responseObject = ResponseObject.builder()
                .code("UPDATE_PROFILE_SUCCESS")
                .content(result)
                .isSuccess(true)
                .status(HttpStatus.OK)
                .message("Query Success")
                .build();
        return ResponseEntity.ok().body(responseObject);
    }

    @PutMapping("change-pass")
    public ResponseEntity<ResponseObject> changePassword(@RequestParam String password,
                                                         @RequestParam String rePassword) {
        var result = profileService.changePassword(password, rePassword);
        var responseObject = ResponseObject.builder()
                .code("CHANGE_PASSWORD_SUCCESS")
                .content(result)
                .isSuccess(true)
                .status(HttpStatus.OK)
                .message("Password changed successfully")
                .build();
        return ResponseEntity.ok().body(responseObject);
    }

}
