package com.lovelyglam.workerservice.service.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.lovelyglam.database.model.constant.FileCloudStatus;
import com.lovelyglam.database.model.entity.FileCloud;
import com.lovelyglam.database.model.exception.ActionFailedException;
import com.lovelyglam.database.repository.FileCloudRepository;
import com.lovelyglam.filecloud.model.FileObject;
import com.lovelyglam.filecloud.model.FileObjectResponse;
import com.lovelyglam.filecloud.service.FileService;
import com.lovelyglam.workerservice.service.FileDatabaseService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FireDatabaseServiceImpl implements FileDatabaseService {
    private final FileService fileService;
    private final FileCloudRepository fileCloudRepository;

    @Transactional(rollbackFor = {ActionFailedException.class})
    public FileObjectResponse uploadFile (MultipartFile file) {
        try {
            var uuid = UUID.randomUUID().toString();
            var nameUpdate = String.format("%s-%s", uuid, file.getOriginalFilename());
            var fileDb = FileCloud.builder()
            .extension(file.getContentType())
            .fileCloudId(uuid)
            .fileName(nameUpdate)
            .isRemoved(false)
            .status(FileCloudStatus.UPLOADED)
            .build();
            fileCloudRepository.save(fileDb);
            var fileObject = new FileObject();
            fileObject.setFile(file.getBytes());
            fileObject.setFileName(nameUpdate);
            return fileService.uploadFile(fileObject);
        } catch (Exception ex ) {
            throw new ActionFailedException("Failed to upload file");
        }
    }
}
