package com.lovelyglam.userserver.controller;

import com.lovelyglam.database.model.dto.request.SearchRequestParamsDto;
import org.springframework.data.domain.Pageable;
import com.lovelyglam.database.model.dto.response.ResponseObject;
import com.lovelyglam.userserver.service.ShopProfileService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "profiles")
public class ShopProfileController {
    private final ShopProfileService shopService;
    @GetMapping
    public ResponseEntity<ResponseObject> getAllStore(
            @Schema
            @PageableDefault(page = 0, size = 10)Pageable pageable,
            @RequestParam(name = "q", required = false) String query){
        var searchQuery = SearchRequestParamsDto.builder()
                .search(query)
                .pageable(pageable)
                .build();
        var result = shopService.getShops(searchQuery);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .code("GET_SHOP_PROFILES_SUCCESS")
                        .content(result)
                        .status(HttpStatus.OK)
                        .isSuccess(true)
                        .message("Query Success")
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getShopDetail(@PathVariable(value = "id") BigDecimal id){
        var result = shopService.getShopDetailById(id);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .code("GET_SHOP_DETAIL_SUCCESS")
                        .content(result)
                        .isSuccess(true)
                        .status(HttpStatus.OK)
                        .message("Query Success")
                        .build()
        );
    }
}
