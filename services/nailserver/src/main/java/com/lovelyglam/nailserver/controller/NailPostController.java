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

import com.lovelyglam.database.model.dto.request.NailPostRequest;
import com.lovelyglam.database.model.dto.request.SearchRequestParamsDto;
import com.lovelyglam.database.model.dto.response.ResponseObject;
import com.lovelyglam.nailserver.service.NailPostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("posts")
@RequiredArgsConstructor
public class NailPostController {
    private final NailPostService nailPostService;

    @GetMapping()
    public ResponseEntity<ResponseObject> getServices(
            @PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable,
            @RequestParam(name = "q", required = false) String query) {
        var queryDto = SearchRequestParamsDto.builder()
                .search(query)
                .wrapSort(pageable)
                .build();
        var result = nailPostService.getNailPosts(queryDto);
        return ResponseEntity.ok(ResponseObject.builder()
                .code("GET_POST_SUCCESS")
                .content(result)
                .message("Get Post Success")
                .isSuccess(true)
                .status(HttpStatus.OK)
                .requestTime(LocalDateTime.now())
                .build());
    }

    @GetMapping("{id}")
    public ResponseEntity<ResponseObject> createPost(@PathVariable BigDecimal id) {
        var result = nailPostService.getNailById(id);
        return ResponseEntity.ok(ResponseObject.builder()
        .code("POST_DETAIL_GET_SUCCESS")
        .content(result)
        .message("Get Post Success")
        .isSuccess(true)
        .status(HttpStatus.OK)
        .requestTime(LocalDateTime.now())
        .build());
    }

    @PostMapping()
    public ResponseEntity<ResponseObject> createPost(@RequestBody NailPostRequest request) {
        var result = nailPostService.createNailPost(request);
        return ResponseEntity.ok(ResponseObject.builder()
        .code("POST_CREATE_SUCCESS")
        .content(result)
        .message("Create Post Success")
        .isSuccess(true)
        .status(HttpStatus.OK)
        .requestTime(LocalDateTime.now())
        .build());
    }

    @PutMapping("{id}")
    public ResponseEntity<ResponseObject> updatePost (@RequestBody NailPostRequest request, @PathVariable BigDecimal id) {
        var result = nailPostService.updateNailPost(request, id);
        return ResponseEntity.ok(ResponseObject.builder()
        .code("POST_UPDATE_SUCCESS")
        .content(result)
        .message("Update Post Success")
        .isSuccess(true)
        .status(HttpStatus.OK)
        .requestTime(LocalDateTime.now())
        .build());
    }
}
