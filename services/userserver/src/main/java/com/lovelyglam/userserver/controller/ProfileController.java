package com.lovelyglam.userserver.controller;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lovelyglam.database.model.dto.response.ResponseObject;
import com.lovelyglam.userserver.service.ProfileService;

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
}
