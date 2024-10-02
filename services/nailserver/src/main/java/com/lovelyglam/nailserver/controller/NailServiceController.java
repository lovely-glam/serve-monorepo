package com.lovelyglam.nailserver.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lovelyglam.database.model.dto.request.NailServiceRequest;
import com.lovelyglam.database.model.dto.request.SearchRequestParamsDto;
import com.lovelyglam.database.model.dto.response.ResponseObject;
import com.lovelyglam.nailserver.service.NailServiceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("nail-services")
@RequiredArgsConstructor
public class NailServiceController {
    private final NailServiceService nailServiceService;

    @PostMapping
    public ResponseEntity<ResponseObject> createNewService(@RequestBody NailServiceRequest request) {
        var result = nailServiceService.createNailService(request);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .code("CREATE_SERVICE_SUCCESS")
                        .content(result)
                        .message("Create Service Success")
                        .isSuccess(true)
                        .status(HttpStatus.OK)
                        .requestTime(LocalDateTime.now())
                        .build());
    }

    @PutMapping("{id}")
    public ResponseEntity<ResponseObject> updateService(@RequestBody NailServiceRequest request,
            @PathVariable(name = "id") BigDecimal id) {
        var result = nailServiceService.updateNailService(request, id);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .code("UPDATE_SERVICE_SUCCESS")
                        .content(result)
                        .message("Update Service Success")
                        .isSuccess(true)
                        .status(HttpStatus.OK)
                        .requestTime(LocalDateTime.now())
                        .build());
    }

    @GetMapping("{id}")
    public ResponseEntity<ResponseObject> getServiceById(@PathVariable BigDecimal id) {
        var result = nailServiceService.getServiceDetailById(id);
        return ResponseEntity.ok(ResponseObject.builder()
                .code("GET_SERVICE_SUCCESS")
                .content(result)
                .message("Get Service Success")
                .isSuccess(true)
                .status(HttpStatus.OK)
                .requestTime(LocalDateTime.now())
                .build());
    }

    @GetMapping()
    public ResponseEntity<ResponseObject> getServices(
            @PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable,
            @RequestParam(name = "q", required = false) String query) {
        var queryDto = SearchRequestParamsDto.builder()
                .search(query)
                .wrapSort(pageable)
                .build();
        var result = nailServiceService.getNailService(queryDto);
        return ResponseEntity.ok(ResponseObject.builder()
                .code("GET_SERVICE_SUCCESS")
                .content(result)
                .message("Get Service Success")
                .isSuccess(true)
                .status(HttpStatus.OK)
                .requestTime(LocalDateTime.now())
                .build());
    }
}
