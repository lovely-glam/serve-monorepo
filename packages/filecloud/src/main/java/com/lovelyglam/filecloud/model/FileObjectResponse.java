package com.lovelyglam.filecloud.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class FileObjectResponse extends FileObject {
    private String url;
}
