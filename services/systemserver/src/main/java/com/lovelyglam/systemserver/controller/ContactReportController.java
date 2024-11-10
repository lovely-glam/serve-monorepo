package com.lovelyglam.systemserver.controller;

import java.time.LocalDateTime;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lovelyglam.database.model.dto.request.SearchRequestParamsDto;
import com.lovelyglam.database.model.dto.response.ResponseObject;
import com.lovelyglam.systemserver.service.ContactReportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "contacts")
public class ContactReportController {
    private final ContactReportService contactReportService;
    @GetMapping
    public ResponseEntity<ResponseObject> getContacts (@PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable,
            @RequestParam(name = "q", required = false) String query) {
        var queryDto = SearchRequestParamsDto.builder()
                .search(query)
                .wrapSort(pageable)
                .build();
        var result = contactReportService.getContacts(queryDto);
        return ResponseEntity.ok(ResponseObject.builder()
                .code("GET_CONTACT_SUCCESS")
                .content(result)
                .message("Query Success")
                .isSuccess(true)
                .status(HttpStatus.OK)
                .requestTime(LocalDateTime.now())
                .build());
    }
}
