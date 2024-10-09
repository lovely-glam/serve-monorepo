package com.lovelyglam.userserver.controller;

import java.time.LocalDateTime;

import com.lovelyglam.database.model.dto.request.BookingRequest;
import com.lovelyglam.database.model.dto.request.UserAccountRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<ResponseObject> updateProfile(@RequestBody @Valid UserAccountRequest request, BindingResult bindingResult) {
        var result = profileService.updateProfile(request);
        var responseObject = ResponseObject.builder()
                .code("GET_BOOKING_DETAIL_SUCCESS")
                .content(result)
                .isSuccess(true)
                .status(HttpStatus.OK)
                .message("Query Success")
                .build();
        return ResponseEntity.ok().body(responseObject);
    }
}
