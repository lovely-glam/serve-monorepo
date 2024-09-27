package com.lovelyglam.filecloud.model;

import lombok.Data;

@Data
public class FileObject {
    private String fileName;
    private byte[] file;
    private String extension;
    private String path;
}
