package com.lovelyglam.workerservice.service;

import org.springframework.web.multipart.MultipartFile;

import com.lovelyglam.filecloud.model.FileObjectResponse;

public interface FileDatabaseService {
    FileObjectResponse uploadFile (MultipartFile file);
}
