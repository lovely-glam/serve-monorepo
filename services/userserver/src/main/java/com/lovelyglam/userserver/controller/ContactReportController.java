package com.lovelyglam.userserver.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lovelyglam.database.model.dto.request.ContactReportRequest;
import com.lovelyglam.database.model.dto.response.ResponseObject;
import com.lovelyglam.userserver.service.ContactReportService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "contacts")
public class ContactReportController {
    private final ContactReportService contactReportService;
    @PostMapping
    public ResponseEntity<ResponseObject> makeContact(@RequestBody @Valid ContactReportRequest request) {
        var result = contactReportService.createContactReport(request);
        return ResponseEntity.ok(ResponseObject
        .builder()
            .content(result)
            .code("CREATE_CONTACT_SUCCESS")
            .message("Create Success")
            .isSuccess(true)
            .status(HttpStatus.OK)
            .requestTime(LocalDateTime.now())
        .build());
    }
    
}
