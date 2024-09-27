package com.lovelyglam.filecloud.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.lovelyglam.filecloud.model.FileObject;
import com.lovelyglam.filecloud.model.FileObjectResponse;
import com.lovelyglam.filecloud.service.FileService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FireServiceImpl implements FileService {
    private final AmazonS3 s3Service;
    @Value("${aws.s3.bucket}")
    private String bucket;

    public FileObjectResponse uploadFile(FileObject file) {
        var fileName = file.getFileName();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(file.getFile());
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getFile().length);
        PutObjectRequest request = new PutObjectRequest(bucket, fileName, inputStream, metadata);
        s3Service.putObject(request);
        var url = s3Service.getUrl(bucket, fileName).toString();
        return returnToFileObjectResponse(file, url);
    }

    public FileObjectResponse downloadFile(FileObject file) {
        var fileName = file.getFileName();
        try {
            S3Object s3Object = s3Service.getObject(bucket, fileName);
            InputStream stream = s3Object.getObjectContent();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] read_buf = new byte[1024];
            int read_len;
            while ((read_len = stream.read(read_buf)) > 0) {
                outputStream.write(read_buf, 0, read_len);
            }
            var response = new FileObjectResponse();
            response.setUrl(s3Service.getUrl(bucket, file.getFileName()).toString());
            response.setFileName(file.getFileName());
            response.setFile(outputStream.toByteArray());
            return response;
        } catch (AmazonS3Exception e) {
            if (e.getStatusCode() == 404) {
                System.out.println("[ERROR] Can't Found File");
            } else if (e.getStatusCode() == 403) {
                System.out.println("[ERROR] Don't Have Permission");
            }
            return null;
        } catch (Exception ex) {
            return null;
        }
    }

    public FileObjectResponse interactFile (Function<AmazonS3, FileObjectResponse> callFunction) {
        return callFunction.apply(s3Service);
    }

    public List<FileObjectResponse> interactFiles (Function<AmazonS3, List<FileObjectResponse>> callFunction) {
        return callFunction.apply(s3Service);
    }

    public FileObjectResponse returnToFileObjectResponse(FileObject file, String url) {
        var response = new FileObjectResponse();
        response.setUrl(url);
        response.setFileName(file.getFileName());
        response.setFile(file.getFile());
        return response;
    }
}
