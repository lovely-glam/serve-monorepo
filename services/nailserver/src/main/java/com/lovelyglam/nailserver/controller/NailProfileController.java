package com.lovelyglam.nailserver.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lovelyglam.database.model.dto.request.ShopDetailRequest;
import com.lovelyglam.database.model.dto.response.ResponseObject;
import com.lovelyglam.nailserver.service.NailProfileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("profiles")
@RequiredArgsConstructor
public class NailProfileController {
    private final NailProfileService nailProfileService;
    @GetMapping(path = "me")
    public ResponseEntity<ResponseObject> getMe () {
        var result = nailProfileService.getMe();
        return ResponseEntity.ok(ResponseObject.builder()
            .code("GET_PROFILE_SUCCESS")
            .message("Get shop profile success")
            .content(result)
            .requestTime(LocalDateTime.now())
            .isSuccess(true)
            .status(HttpStatus.OK)
        .build());
    }

    @PutMapping
    public ResponseEntity<ResponseObject> updateBasicProfile (@RequestBody ShopDetailRequest request) {
        var result = nailProfileService.updateProfile(request);
        return ResponseEntity.ok(ResponseObject.builder()
            .code("UPDATE_PROFILE_SUCCESS")
            .message("Update shop profile success")
            .content(result)
            .requestTime(LocalDateTime.now())
            .isSuccess(true)
            .status(HttpStatus.OK)
        .build());
    }
}
