package com.lovelyglam.workerservice.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lovelyglam.database.model.dto.response.ResponseObject;
import com.lovelyglam.workerservice.service.FileDatabaseService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("files")
@RequiredArgsConstructor
public class FileController {
    private final FileDatabaseService fileDatabaseService;

    @Operation(summary = "Upload a file to S3",
               description = "Uploads a file and returns the URL of the uploaded file.")
    @PostMapping(value = "upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseObject> uploadFile (@RequestPart("file") MultipartFile file) {
        var result = fileDatabaseService.uploadFile(file);
        return ResponseEntity.ok(ResponseObject.builder()
        .code("UPLOAD SUCCESS")
        .content(result)
        .message("UPLOAD SUCCESS")
        .build());
    }
}
