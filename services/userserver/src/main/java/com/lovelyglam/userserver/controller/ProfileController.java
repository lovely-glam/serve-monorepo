package com.lovelyglam.userserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lovelyglam.database.model.dto.response.ResponseObject;
import com.lovelyglam.userserver.service.ProfileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "profiles")
public class ProfileController {
    private final ProfileService profileService;
    @GetMapping("me")
    public ResponseEntity<ResponseObject> getMe () {
        return ResponseEntity.ok(ResponseObject.builder()
        
        .build());
    }
}
