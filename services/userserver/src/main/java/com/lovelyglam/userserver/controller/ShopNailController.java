package com.lovelyglam.userserver.controller;

import com.lovelyglam.database.model.dto.request.SearchRequestParamsDto;
import com.lovelyglam.database.model.dto.response.ResponseObject;
import com.lovelyglam.userserver.service.ShopNailService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "nail-services")
public class ShopNailController {
    private final ShopNailService shopNailService;
    @GetMapping
    public ResponseEntity<ResponseObject> getAllNailServices(
            @Schema
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam(name = "q", required = false) String query){
        var searchQuery = SearchRequestParamsDto.builder()
                .search(query)
                .pageable(pageable)
                .build();
        var result = shopNailService.getShopNailServices(searchQuery);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .code("GET_NAIL_SERVICES_SUCCESS")
                        .content(result)
                        .status(HttpStatus.OK)
                        .isSuccess(true)
                        .message("Query Success")
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getNailServiceDetail(@PathVariable(value = "id") BigDecimal id){
        var result = shopNailService.getShopNailServiceById(id);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .code("GET_NAIL_SERVICE_SUCCESS")
                        .content(result)
                        .isSuccess(true)
                        .status(HttpStatus.OK)
                        .message("Query Success")
                        .build()
        );
    }

    @GetMapping("/{shopId}/services")
    public ResponseEntity<ResponseObject> getShopServices(
            @PathVariable BigDecimal shopId,
            @Schema
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam(name = "q", required = false) String query) {

        // Construct the search request params
        SearchRequestParamsDto request = SearchRequestParamsDto.builder()
                .search(query)
                .pageable(pageable)
                .build();
        var result = shopNailService.getShopNailServicesByShopId(shopId, request);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .code("GET_NAIL_SERVICES_SUCCESS")
                        .content(result)
                        .status(HttpStatus.OK)
                        .isSuccess(true)
                        .message("Query Success")
                        .build()
        );
    }
}
