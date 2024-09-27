package com.lovelyglam.filecloud.service;

import java.util.List;
import java.util.function.Function;

import com.amazonaws.services.s3.AmazonS3;
import com.lovelyglam.filecloud.model.FileObject;
import com.lovelyglam.filecloud.model.FileObjectResponse;

public interface FileService {
    FileObjectResponse uploadFile (FileObject file);
    FileObjectResponse returnToFileObjectResponse(FileObject file, String url);
    FileObjectResponse downloadFile(FileObject file);
    FileObjectResponse interactFile (Function<AmazonS3, FileObjectResponse> callFunction);
    List<FileObjectResponse> interactFiles (Function<AmazonS3, List<FileObjectResponse>> callFunction);
}
