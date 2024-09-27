package com.lovelyglam.workerservice.service.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lovelyglam.filecloud.model.FileObject;
import com.lovelyglam.filecloud.model.FileObjectResponse;
import com.lovelyglam.filecloud.service.FileService;
import com.lovelyglam.workerservice.service.FileDatabaseService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FireDatabaseServiceImpl implements FileDatabaseService {
    private final FileService fileService;

    public FileObjectResponse uploadFile (MultipartFile file) {
        try {
            var uuid = UUID.randomUUID().toString();
            var nameUpdate = String.format("%s-%s", uuid, file.getOriginalFilename());
            var fileObject = new FileObject();
            fileObject.setFile(file.getBytes());
            fileObject.setFileName(nameUpdate);
            return fileService.uploadFile(fileObject);
        } catch (Exception ex ) {
            return null;
        }
    }
}
